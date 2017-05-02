package at.fhooe.mc.ois;

import java.awt.*;
import java.util.Vector;

/**
 * Created by Pirklbauer on 30.04.2017.
 */
public class GeoArea extends GeoObjectPart {

    //Vector mit den enthaltenen Polygonen
    private Vector<Polygon> mGeometry;

    /**
     * Standard Konstruktor
     */
    public GeoArea() {
        mGeometry = new Vector<>();
    }

    /**
     * Konstruktor zum initialisieren mit Polygon Vektor
     * @param _geometry Der Polygon Vektor zum initialisieren
     */
    public GeoArea(Vector<Polygon> _geometry) {
        mGeometry = _geometry;
    }

    /**
     * Multipliziert die Polygone im Vektor mGeometry mit der übergenen Transformationsmatrix
     * und zeichnet sie in den übergebenen Graghics-Kontext unter Verwendung eines übergebenen
     * Presentationschema.
     * @param _g Der Graphics-Kontext indem zu zeichnen ist
     * @param _m Die Transformationsmatrix, mit der die Polygone multipliziert werden
     * @param _schema Das zu verwendene Presentationsschema
     */
    @Override
    void draw(Graphics _g, Matrix _m, PresentationSchema _schema) {
        for (Polygon poly : mGeometry) {
            Polygon transformedPoly = _m.multiply(poly);
            if (_schema != null) {
                _g.setColor(_schema.getmFillColor());
                _g.fillPolygon(transformedPoly);
                _g.setColor(_schema.getmLineColor());
                _g.drawPolygon(transformedPoly);
            }
            else {
                _g.setColor(Color.black);
                _g.drawPolygon(transformedPoly);
            }
        }
    }

    /**
     * Erstellt eine Boundingbox für die Polygone im Vektor mGeometry
     * @return Die Bounding Box als Rechteck
     */
    @Override
    Rectangle getBounds() {
        Rectangle currentBounds;
        Rectangle overallBounds = mGeometry.firstElement().getBounds();

        for (int i = 1; i < mGeometry.size(); i++) {
            currentBounds = mGeometry.get(i).getBounds();
            overallBounds = overallBounds.union(currentBounds);
        }
        return overallBounds;
    }

    /**
     * Checkt ob ein übergebener Punkt in den Polygonen enthalten ist.
     * @param _pt Der Punkt, der überprüft werden soll
     * @return True falls in Polygonen enthalten, false falls nicht
     */
    public boolean contains(Point _pt) {
        for (Polygon poly :getmGeometry()) {
            if(poly.contains(_pt)) {
                return true;
            }
        }

        return false;
    }

    //getters and setters

    public Vector<Polygon> getmGeometry() {
        return mGeometry;
    }

    public void setmGeometry(Vector<Polygon> _geometry) {
        this.mGeometry = _geometry;
    }


}
