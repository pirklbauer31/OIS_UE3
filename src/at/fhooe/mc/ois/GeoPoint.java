package at.fhooe.mc.ois;

import java.awt.*;

/**
 * Created by Pirklbauer on 30.04.2017.
 */
public class GeoPoint extends GeoObjectPart {

    //Der awt.Point des Objektes
    private Point mGeometry;

    /**
     * Konstruktor zum initialisieren mit awt.Point
     * @param _geometry Der awt.Point zum initialisieren
     */
    public GeoPoint(Point _geometry) {
        mGeometry = _geometry;
    }

    /**
     * Multipliziert den awt.Point in mGeometry mit der übergenen Transformationsmatrix
     * und zeichnet sie in den übergebenen Graghics-Kontext unter Verwendung eines übergebenen
     * Presentationschema.
     * @param _g Der Graphics-Kontext indem zu zeichnen ist
     * @param _m Die Transformationsmatrix, mit der der awt.Point multipliziert wird
     * @param _schema Das zu verwendene Presentationsschema
     */
    @Override
    void draw(Graphics _g, Matrix _m, PresentationSchema _schema) {
        Point transformedPoint = _m.multiply(mGeometry);
        if(_schema != null) {
            _g.setColor(_schema.getmFillColor());
            _g.fillArc(transformedPoint.x, transformedPoint.y, (int)_schema.getmLineWidth(), (int)_schema.getmLineWidth(), 0, 360);
            _g.setColor(_schema.getmLineColor());
            _g.drawArc(transformedPoint.x, transformedPoint.y, (int)_schema.getmLineWidth(), (int)_schema.getmLineWidth(), 0, 360);
        }
        else {
            _g.setColor(Color.black);
            _g.drawArc(transformedPoint.x, transformedPoint.y, 2, 2, 0, 360);
        }

    }

    /**
     * Überprüft ob der übergebene Punkt gleich dem GeoPoint ist
     * @param _pt Der zu überprüfende Punkt
     * @return
     */
    boolean contains(Point _pt) {
        if(mGeometry.x == _pt.x && mGeometry.y == _pt.y) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Erstellt eine Boundingbox für den GeoPoint
     * @return Die erstellte Boundingbox
     */
    @Override
    Rectangle getBounds() {
        return new Rectangle(mGeometry.x, mGeometry.y, 1,1);
    }

    //getters and setters

    public Point getmGeometry() {
        return mGeometry;
    }

    public void setmGeometry(Point _geometry) {
        this.mGeometry = _geometry;
    }
}
