package at.fhooe.mc.ois;

import java.awt.*;
import java.util.Vector;

/**
 * Created by Pirklbauer on 30.04.2017.
 */
public class GeoLine extends GeoObjectPart {

    //Vektor mit den enthaltenen GeoPoints
    private Vector<GeoPoint> mGeometry;

    /**
     * Standard Konstruktor
     */
    public GeoLine() {
        mGeometry = new Vector<>();
    }

    /**
     * Konstruktor zum initialisieren mit GeoPoints Vektor
     * @param _geometry Der GeoPoints Vektor zum initialisieren
     */
    public GeoLine(Vector<GeoPoint> _geometry) {
        mGeometry = _geometry;
    }

    /**
     * Ruft die draw Methode der GeoPoints auf und zeichnet Linien dazwischen.
     * @param _g Der Graphics-Kontext indem zu zeichnen ist
     * @param _m Die Transformationsmatrix, mit der die Daten multipliziert werden
     * @param _schema Das zu verwendene Presentationsschema
     */
    @Override
    void draw(Graphics _g, Matrix _m, PresentationSchema _schema) {
        GeoPoint currentPoint = mGeometry.firstElement();
        for (int i = 1; i < mGeometry.size() ; i++) {
            GeoPoint nextPoint = mGeometry.get(i);
            currentPoint.draw(_g, _m, _schema);
            if(_schema != null) {
                _g.setColor(_schema.getmLineColor());
            }
            else {
                _g.setColor(Color.black);
            }
            Point transCurr = _m.multiply(currentPoint.getmGeometry());
            Point transNxt = _m.multiply(nextPoint.getmGeometry());
            _g.drawLine(transCurr.x, transCurr.y, transNxt.x, transNxt.y);
            nextPoint.draw(_g, _m, _schema);
            currentPoint = nextPoint;
        }
    }

    /**
     * Erstellt eine Boundingbox für die GeoPoints im Vektor mGeometry
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
     * Checkt ob ein übergebener Punkt in den GeoPoints enthalten ist.
     * @param _pt Der Punkt, der überprüft werden soll
     * @return True falls in GeoPoints enthalten, false falls nicht
     */
    @Override
    boolean contains(Point _pt) {
        for (GeoPoint point : mGeometry) {
            if(point.contains(_pt)) {
                return true;
            }
        }

        return false;
    }
}
