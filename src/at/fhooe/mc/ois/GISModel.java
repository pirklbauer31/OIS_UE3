package at.fhooe.mc.ois;

import de.intergis.JavaClient.comm.*;
import de.intergis.JavaClient.gui.IgcConnection;
import org.postgis.Geometry;
import org.postgis.LinearRing;
import org.postgis.MultiPoint;
import org.postgis.PGgeometry;
import org.postgresql.PGConnection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Pirklbauer on 05.04.2017.
 */
public class GISModel {

    /**
     * The ArrayList containing the observers
     */
    private ArrayList<GISView> mObservers = new ArrayList<>();
    /**
     * Width of the image to draw in the panel
     */
    private int mImageWidth;
    /**
     * Height of the image to draw in the panel
     */
    private int mImageHeight;

    /**
     * Keeps track if mouse event has been triggered
     */
    private boolean mMouseEvent;
    /**
     * X Position of the mouse cursor when clicked
     */
    private int mMousePosXEnd;
    /**
     * Y Position of the mouse cursor when released
     */
    private int mMousePosYEnd;
    /**
     * X Position of the mouse cursor when released
     */
    private int mMousePosXStart;
    /**
     * Y Position of the mouse cursor when clicked
     */
    private int mMousePosYStart;
    private int mMousePosXCurrent;
    private int mMousePosYCurrent;
    /**
     * Which mouse button has been clicked on event
     */
    private int mMouseButton;

    /**
     * Enthält die interne TransformationsMatrix
     */
    private Matrix mMatrixTrans;
    /**
     * Enthält die zu zeichnenden Geoobjekte
     */
    private Vector<GeoObject> mGeoObjects = new Vector<>();

    /**
     * Enthält den Zeichenkontext (in welcher Farbe Objekte zu zeichnen sind)
     */
    private DrawingContext mContext = new DrawingContext();

    // die Verbindung zum Geo-Server
    private CgGeoConnection m_geoConnection  = null;
    // das Anfrage-Interface des Geo-Servers
    private CgGeoInterface m_geoInterface   = null;

    //Boolean Variable für den StickyModus
    public boolean mSticky;
    //BoundingBox für den Sticky Modus
    private Rectangle mStickyBounds;

    public GISModel() {
    }

    /**
     * Extrahiert einige Geoobjekte aus dem Geodaten Server.
     */
    protected Vector extractData(String _stmt) {
        try {
            CgStatement stmt            = m_geoInterface.Execute(_stmt);
            CgResultSet cursor          = stmt.getCursor();
            Vector<GeoObject>      objectContainer = new Vector();
            while (cursor.next()) {
                CgIGeoObject obj = cursor.getObject();
                System.out.println("NAME --> " + obj.getName());
                System.out.println("TYP  --> " + obj.getCategory());
                CgIGeoPart[] parts = obj.getParts();
                for (int i = 0 ; i < parts.length ; i++){
                    System.out.println("PART " + i);
                    int   pointCount = parts[i].getPointCount();
                    int[] xArray     = parts[i].getX();
                    int[] yArray     = parts[i].getY();
                    Polygon poly = new Polygon(xArray, yArray, pointCount);
                    for (int j = 0 ; j < pointCount ; j++) {
                        System.out.println("[" + xArray[j] + " ; " + yArray[j] + "]");
                    } // for j
                    Vector<Polygon> polys = new Vector<>();
                    polys.add(poly);
                    Vector<GeoObjectPart> geoparts = new Vector<>();
                    geoparts.add(new GeoArea(polys));
                    GeoObject geo = new GeoObject(obj.getName(), obj.getCategory(), geoparts);
                    objectContainer.addElement(geo);
                } // for i
                System.out.println();
            } // while cursor
            return objectContainer;
        } catch (Exception _e) { _e.printStackTrace(); }
        return null;
    }

    /**
     * Initialisiert die Verbindung zum Geodaten Server.
     * @return True falls Verbindung erfolgreich, sonst false.
     */
    public boolean init() {
        try {
            // der Geo-Server wird initialisiert
            m_geoConnection =
                    new IgcConnection(new CgConnection("admin",
                            "admin",
                            "T:localhost:4949",
                            null));
            // das Anfrage-Interface des Servers wird abgeholt
            m_geoInterface = m_geoConnection.getInterface();
            return true;
        } catch (Exception _e) {_e.printStackTrace();}
        return false;
    }

    /**
     * Initialisiert eine Verbindung zum PostGIS OSM Server.
     * Danach ruft es die extractDataPostGIS Methode für die verschieden SQL Statements auf.
     */
    public void initPostGIS() {
        mGeoObjects.clear();
        Connection conn;
        try {
            /* Load theJDBC driverandestablisha connection. */
            Class.forName("org.postgresql.Driver");
            //String url= "jdbc:postgresql://localhost:5432/osm";
            //String url= "jdbc:postgresql://localhost:5432/osm_4326";
            String url= "jdbc:postgresql://localhost:5432/osm_hagenberg_3857";
            //String url= "jdbc:postgresql://localhost:5432/osm_hawaii_3857";
            conn= DriverManager.getConnection(url, "geo", "geo");

            /* Add the geometry types to the connection. */
            PGConnection c = (PGConnection) conn;
            c.addDataType("geometry", org.postgis.PGgeometry.class);
            c.addDataType("box2d", org.postgis.PGbox2d.class);

            if(mSticky) {
                extractDataPostGIS(conn, "SELECT * FROM osm_boundary AS a WHERE a.geom && ST_MakeEnvelope("+ mStickyBounds.x
                        + ", "+ mStickyBounds.y+", "+(mStickyBounds.x+mStickyBounds.getWidth())+" ,"+ (mStickyBounds.getHeight()+mStickyBounds.y) +");");
                extractDataPostGIS(conn, "SELECT * FROM osm_landuse AS a WHERE a.geom && ST_MakeEnvelope("+ mStickyBounds.x
                        + ", "+ mStickyBounds.y+", "+(mStickyBounds.x+mStickyBounds.getWidth())+" ,"+ (mStickyBounds.getHeight()+mStickyBounds.y) +");");
                extractDataPostGIS(conn, "SELECT * FROM osm_natural AS a WHERE a.geom && ST_MakeEnvelope("+ mStickyBounds.x
                        + ", "+ mStickyBounds.y+", "+(mStickyBounds.x+mStickyBounds.getWidth())+" ,"+ (mStickyBounds.getHeight()+mStickyBounds.y) +");");
                extractDataPostGIS(conn, "SELECT * FROM osm_highway AS a WHERE a.geom && ST_MakeEnvelope("+ mStickyBounds.x
                        + ", "+ mStickyBounds.y+", "+(mStickyBounds.x+mStickyBounds.getWidth())+" ,"+ (mStickyBounds.getHeight()+mStickyBounds.y) +");");
                extractDataPostGIS(conn, "SELECT * FROM osm_building AS a WHERE a.geom && ST_MakeEnvelope("+ mStickyBounds.x
                        + ", "+ mStickyBounds.y+", "+(mStickyBounds.x+mStickyBounds.getWidth())+" ,"+ (mStickyBounds.getHeight()+mStickyBounds.y) +");");
            }
            else {
                extractDataPostGIS(conn, "SELECT * FROM osm_boundary;");
                extractDataPostGIS(conn, "SELECT * FROM osm_landuse AS a WHERE a.type IN (5001,5002,5003,5004,5006);");
                extractDataPostGIS(conn, "SELECT * FROM osm_natural AS a WHERE a.type IN (6001,6002,6005);");
                extractDataPostGIS(conn, "SELECT * FROM osm_highway AS a WHERE a.type IN (1100,1120,1130,1040,1030,1080,1070,1010,1020);");
                extractDataPostGIS(conn, "SELECT * FROM osm_building AS a WHERE a.type IN (9001,9002,9003,9099);");
            }
            //s.execute("SELECT ST_Transform(ST_SetSRID(ST_MakePoint(?,?),4326),?) as geom;");

            conn.close();
            zoomToFit();

        } catch( Exception _e ) { _e.printStackTrace(); }
    }

    /**
     * Extrahiert Daten aus dem PostGIS Server
     * @param _conn Das Verbindungsobjekt
     * @param _query Das auszuführende SQL statement
     */
    private void extractDataPostGIS (Connection _conn, String _query) {
         /* Create a statementandexecutea selectquery. */
        try {
            Statement s = _conn.createStatement();
            ResultSet r = s.executeQuery(_query);
            while( r.next() ) {
                String id = r.getString("id");
                int type = r.getInt("type");
                PGgeometry geom = (PGgeometry)r.getObject("geom");

                switch(geom.getGeoType()) {
                    case Geometry.POLYGON : {
                        String wkt = geom.toString();
                        org.postgis.Polygon p = new org.postgis.Polygon(wkt);
                        if (p.numRings() >= 1) {
                            Polygon poly = new Polygon();
                            // Ring 0 --> main polygon .. rest should be holes
                            LinearRing ring = p.getRing(0);
                            for(int i = 0; i < ring.numPoints(); i++) {
                                org.postgis.Point pPG = ring.getPoint(i);
                                poly.addPoint((int)pPG.x, (int)pPG.y);
                            }
                            GeoArea geopart = new GeoArea();
                            geopart.getmGeometry().add(poly);
                            Vector<GeoObjectPart> geoparts = new Vector<>();
                            geoparts.add(geopart);
                            GeoObject newGeo = new GeoObject(id, type, geoparts);
                            mGeoObjects.add(newGeo);
                        }
                    } break;
                    case Geometry.MULTIPOLYGON: {
                        String wkt = geom.toString();
                        org.postgis.MultiPolygon mp = new org.postgis.MultiPolygon(wkt);
                        Vector<Polygon> geoPolys = new Vector<>();
                        for (int i = 0; i < mp.numPolygons(); i++) {
                            org.postgis.Polygon p = mp.getPolygon(i);
                            if (p.numRings() >= 1) {
                                Polygon poly = new Polygon();
                                // Ring 0 --> main polygon .. rest should be holes
                                LinearRing ring = p.getRing(0);
                                for(int j = 0; j < ring.numPoints(); j++) {
                                    org.postgis.Point pPG = ring.getPoint(j);
                                    poly.addPoint((int)pPG.x, (int)pPG.y);
                                }
                                geoPolys.add(poly);
                            }
                        }
                        GeoArea geopart = new GeoArea(geoPolys);
                        Vector<GeoObjectPart> geoparts = new Vector<>();
                        geoparts.add(geopart);
                        GeoObject newGeo = new GeoObject(id, type, geoparts);
                        mGeoObjects.add(newGeo);
                    } break;
                    case Geometry.MULTIPOINT: {
                        String wkt = geom.toString();
                        org.postgis.MultiPoint mp = new org.postgis.MultiPoint(wkt);
                        Vector<GeoObjectPart> geoparts = new Vector<>();
                        for (int i = 0; i < mp.numPoints(); i++) {
                            org.postgis.Point p = mp.getPoint(i);
                            GeoPoint geopoint = new GeoPoint(new Point((int)p.getX(), (int)p.getY()));
                            geoparts.add(geopoint);
                        }
                        GeoObject newGeo = new GeoObject(id, type, geoparts);
                        mGeoObjects.add(newGeo);
                    } break;
                    case Geometry.POINT: {
                        String wkt = geom.toString();
                        org.postgis.Point p = new org.postgis.Point(wkt);
                        Vector<GeoObjectPart> geoparts = new Vector<>();
                        GeoPoint geopoint = new GeoPoint(new Point((int)p.getX(), (int)p.getY()));
                        geoparts.add(geopoint);
                        GeoObject newGeo = new GeoObject(id, type, geoparts);
                        mGeoObjects.add(newGeo);
                    } break;
                    case Geometry.LINESTRING: {
                        String wkt = geom.toString();
                        org.postgis.LineString l = new org.postgis.LineString(wkt);
                        Vector<GeoObjectPart> geoparts = new Vector<>();
                        Vector<GeoPoint> geopoints = new Vector<>();
                        for (int i = 0; i < l.numPoints(); i++) {
                            org.postgis.Point p = l.getPoint(i);
                            GeoPoint geopoint = new GeoPoint(new Point((int)p.getX(), (int)p.getY()));
                            geopoints.add(geopoint);
                        }
                        GeoLine geoline = new GeoLine(geopoints);
                        geoparts.add(geoline);
                        GeoObject newGeo = new GeoObject(id, type, geoparts);
                        mGeoObjects.add(newGeo);
                    } break;
                }
            }
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Erstellt die BoundingBox für die aktuell am Bildschirm sichtbaren Objekte
     * und ruft die initPostGIS Methode auf.
     */
    public void getStickyBounds () {
        Rectangle screenBounds = new Rectangle(getMapPoint(new Point(0,0)));
        screenBounds.add(getMapPoint(new Point(mImageWidth, mImageHeight)));
        mStickyBounds = screenBounds;
        initPostGIS();
    }

    /**
     * Ruft die extractData Methode auf, speichert die Objekte in dem geoObjects Vektor
     * und ruft danach die zoomToFit Methode auf.
     */
    public void loadData() {
        if(init()) {
            mGeoObjects = extractData("SELECT * FROM data WHERE type in (233, 931, 932, 933, 934, 1101)");
            System.out.println("loaded");
        }

        if (mGeoObjects != null) {
            zoomToFit();
        }
    }



    /**
     * Called on componentResize action, sets the size for the images to create to given size.
     * @param _width Width for images to create
     * @param _height Height for images to create
     */
    public void setSize(int _width, int _height) {
        mImageWidth = _width;
        mImageHeight = _height;
    }

    /**
     * Called on mouseRelease action, stores mouse cursor postion and the clicked button, calls generateRndHome afterwards.
     * @param _posX X-Position of the mouse cursor on click
     * @param _posY Y-Position of the mouse cursor on click
     * @param _clickedButton The button that has been clicked
     */
    public void generateOnMousePos(int _posX, int _posY, int _clickedButton) {
        mMouseButton = _clickedButton;
    }

    /**
     * Adds a given GISView object to the observer array list.
     * @param _view The given view to add as observer
     */
    public void addObserver(GISView _view) {
        mObservers.add(_view);
    }

    /**
     * Creates a BufferedImage and draws the geoObjects inside. Calls the update method for all observers.
     */
    public void updateObservers () {
        for (GISView observer : mObservers) {

            BufferedImage mImage = new BufferedImage(mImageWidth, mImageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = (Graphics2D) mImage.getGraphics();
            graphics2D.setColor(Color.lightGray);
            graphics2D.fillRect(0,0, mImageWidth, mImageHeight);
            //graphics2D.setColor(Color.white);
            for (GeoObject tempGeo:mGeoObjects) {
                tempGeo.paint(graphics2D, mMatrixTrans, mContext.getSchema(tempGeo.getType()));
            }
            observer.update(mImage);
        }
    }

    /**
     * Stellt intern eine Transformationsmatrix zur Verfuegung, die so
     * skaliert, verschiebt und spiegelt, dass die zu zeichnenden Polygone
     * komplett in den Anzeigebereich passen
     */
    public void zoomToFit() {
        mMatrixTrans = Matrix.zoomToFit(getMapBounds(mGeoObjects), new Rectangle(0,0, mImageWidth, mImageHeight));
        mObservers.get(0).getmTxtScale().setText("1:"+ calculateScale());
        updateObservers();
    }

    /**
     * Veraendert die interne Transformationsmatrix so, dass in das
     * Zentrum des Anzeigebereiches herein- bzw. herausgezoomt wird
     *
     * @param _factor Der Faktor um den herein- bzw. herausgezoomt wird
     */
    public void zoom(double _factor) {
        if(mMatrixTrans == null) {
            loadData();
        }

        mMatrixTrans = Matrix.zoomPoint(mMatrixTrans,new Point(mImageWidth/2,mImageHeight/2),_factor);
        mObservers.get(0).getmTxtScale().setText("1:"+ calculateScale());
        updateObservers();
    }
    /**
     * Veraendert die interne Transformationsmatrix so, dass an dem
     * uebergebenen Punkt herein- bzw. herausgezoomt wird
     *
     * @param _pt Der Punkt an dem herein- bzw. herausgezoomt wird
     * @param _factor Der Faktor um den herein- bzw. herausgezoomt wird
     */
    public void zoom(Point _pt, double _factor) {
        if(mMatrixTrans == null) {
            loadData();
        }
        mMatrixTrans = Matrix.zoomPoint(mMatrixTrans,_pt,_factor);
        updateObservers();
    }

    /**
     * Ermittelt die gemeinsame BoundingBox der uebergebenen Polygone
     *
     * @param _poly Die Polygone, fuer die die BoundingBox berechnet
     * werden soll
     * @return Die BoundingBox
     */
    public Rectangle getMapBounds(Vector<GeoObject> _poly) {
        Rectangle currentBounds;
        Rectangle overallBounds = _poly.get(0).getBounds();

        for (int i = 1; i < _poly.size(); i++) {
            currentBounds = _poly.get(i).getBounds();
            overallBounds = overallBounds.union(currentBounds);
        }
        return overallBounds;
    }
    /**
     * Veraendert die interne Transformationsmatrix so, dass
     * die zu zeichnenden Objekt horizontal verschoben werden.
     *
     * @param _delta Die Strecke, um die verschoben werden soll
     */
    public void scrollHorizontal(int _delta) {
        if(mMatrixTrans == null) {
            loadData();
        }
        mMatrixTrans = Matrix.translate(_delta,0).multiply(mMatrixTrans);
        updateObservers();
    }
    /**
     * Veraendert die interne Transformationsmatrix so, dass
     * die zu zeichnenden Objekt horizontal verschoben werden.
     *
     * @param _delta Die Strecke, um die verschoben werden soll
     */
    public void scrollVertical(int _delta) {
        if(mMatrixTrans == null) {
            loadData();
        }
        mMatrixTrans = Matrix.translate(0,_delta).multiply(mMatrixTrans);
        updateObservers();
    }

    /**
     * Veraendert die interne Transformationsmatrix so, dass
     * die zu zeichnenden Objekte um die Distanz zwischen MousePressed
     * und Mousereleased verschoben werden.
     */
    public void scrollWithMouse() {
        if(mMatrixTrans != null) {
            int distanceX = 0, distanceY = 0;
            distanceX = mMousePosXEnd - mMousePosXStart;
            distanceY = mMousePosYEnd - mMousePosYStart;

            scrollHorizontal(distanceX);
            scrollVertical(distanceY);
            updateObservers();
        }
    }

    /**
     * Veraendert die interne Transformationsmatrix so, dass
     * die zu zeichnenden Objekt nach rechts oder links rotiert werden.
     * @param _alpha Der Winkel um den rotiert werden soll
     */
    public void rotate(double _alpha) {
        Rectangle bounds = new Rectangle(0,0,mImageWidth, mImageHeight);
        Matrix translZero = Matrix.translate(-bounds.getCenterX(), -bounds.getCenterY());
        Matrix rotation = Matrix.rotate(_alpha);
        Matrix tranlsMid = Matrix.translate(bounds.getCenterX(), bounds.getCenterY());

        mMatrixTrans = tranlsMid.multiply(rotation).multiply(translZero).multiply(mMatrixTrans);
        updateObservers();
    }

    /**
     * Ermittelt die Geo-Objekte, die den Punkt (in Bildschirmkoordinaten)
     * enthalten
     * @param _pt Ein Selektionspunkt im Bildschirmkoordinatensystem
     * @return Ein Vektor von Geo-Objekte, die den Punkt enthalten
     * @see java.awt.Point
     * @see GeoObject
     */
    public Vector<GeoObject> initSelection(Point _pt) {
        Vector<GeoObject> retGeos = new Vector<>();
        Point worldPoint = getMapPoint(_pt);

        for (GeoObject tempGeo: mGeoObjects) {
            for (GeoObjectPart geoObjectPart: tempGeo.getGeometry()) {
                if (geoObjectPart.contains(worldPoint)) {
                    retGeos.add(tempGeo);
                }
            }

        }
        return retGeos;
    }
    /**
     * Stellt intern eine Transformationsmatrix zur Verfuegung, die so
     * skaliert, verschiebt und spiegelt, dass die zu zeichnenden Polygone
     * innerhalb eines definierten Rechtecks (_mapBounds) komplett in den
     * Anzeigebereich (die Zeichenflaeche) passen
     * @param _mapBounds Der darzustellende Bereich in Welt-Koordinaten
     */
    public void zoomRect(Rectangle _mapBounds) {
        mMatrixTrans = mMatrixTrans.invers();
        Rectangle mapBoundsWorld = mMatrixTrans.multiply(_mapBounds);
        mMatrixTrans = Matrix.zoomToFit(mapBoundsWorld, new Rectangle(0,0, mImageWidth, mImageHeight));
        updateObservers();
    }
    /**
     * Liefert zu einem Punkt im Bildschirmkoordinatensystem den passenden
     * Punkt im Kartenkoordinatensystem
     * @param _pt Der umzuwandelnde Punkt im Bildschirmkoordinatensystem
     * @return Der gleiche Punkt im Weltkoordinatensystem
     * @see java.awt.Point
     */
    public Point getMapPoint(Point _pt) {
        Matrix inversMatrix = mMatrixTrans.invers();
        return inversMatrix.multiply(_pt);
    }

    /**
     * Berechnet den aktuellen Skalierungsfaktor
     * @return Der Skalierungsfaktor
     */
    protected int calculateScale() {
        GeoDoublePoint vector = new GeoDoublePoint(0,1.0);
        GeoDoublePoint vector_transformed = mMatrixTrans.multiply(vector);
        return (int) ((1/vector_transformed.length())*(72/2.54));
    }

    //Getters and setters


    public boolean isMouseEvent() {
        return mMouseEvent;
    }

    public void setMouseEvent(boolean _mouseEvent) {
        this.mMouseEvent = _mouseEvent;
    }

    public void setmMousePosXEnd(int _MousePosXEnd) {
        this.mMousePosXEnd = _MousePosXEnd;
    }

    public void setmMousePosYEnd(int _MousePosYEnd) {
        this.mMousePosYEnd = _MousePosYEnd;
    }

    public void setmMousePosXStart(int _MousePosXStart) {
        this.mMousePosXStart = _MousePosXStart;
    }

    public void setmMousePosYStart(int _MousePosYStart) {
        this.mMousePosYStart = _MousePosYStart;
    }

    public int getmMousePosXEnd() {
        return mMousePosXEnd;
    }

    public int getmMousePosYEnd() {
        return mMousePosYEnd;
    }

    public int getmMousePosXStart() {
        return mMousePosXStart;
    }

    public int getmMousePosYStart() {
        return mMousePosYStart;
    }

    public int getmMousePosXCurrent() {
        return mMousePosXCurrent;
    }

    public void setmMousePosXCurrent(int _MousePosXCurrent) {
        this.mMousePosXCurrent = _MousePosXCurrent;
    }

    public int getmMousePosYCurrent() {
        return mMousePosYCurrent;
    }

    public void setmMousePosYCurrent(int _MousePosYCurrent) {
        this.mMousePosYCurrent = _MousePosYCurrent;
    }

    public ArrayList<GISView> getmObservers() {
        return mObservers;
    }

    public void setmObservers(ArrayList<GISView> _Observers) {
        this.mObservers = _Observers;
    }

}
