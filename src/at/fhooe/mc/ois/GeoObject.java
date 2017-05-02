package at.fhooe.mc.ois;

import java.awt.*;
import java.util.Vector;

/**
 * Created by Pirklbauer on 19.04.2017.
 */
public class GeoObject {

    //Die ID des GeoObject
    private String mId;
    //Der Typ des GeoObject
    private int mType;
    //Vektor mit enthalten GeoObjectParts
    private Vector<GeoObjectPart> mGeometry = new Vector<>();

    /**
     * Konstruktor
     * @param _id Die Id des Objektes
     * @param _type Der Typ des Objektes
     * @param _geometry Die Geometrie des Objektes
     */
    //public GeoObject(String _id, int _type, Polygon _poly) {
    //    mId = _id;
    //    mType = _type;
    //    mPoly.add(_poly);
    //}

    /**
     * Konstruktor
     * @param _id Die Id des Objektes
     * @param _type Der Typ des Objektes
     * @param _geometry Die Geometrie(GeoObjectPart Vektor) des Objektes
     */
    public GeoObject(String _id, int _type, Vector<GeoObjectPart> _geometry) {
        mId = _id;
        mType = _type;
        mGeometry = _geometry;
    }
    /**
     * Liefert die Id des Geo-Objektes
     * @return Die Id des Objektes
     * @see String
     */
    public String getId() {
        return mId;
    }
    /**
     * Liefert den Typ des Geo-Objektes
     * @return Der Typ des Objektes
     */
    public int getType() {
        return mType;
    }
    /**
     * Liefert die Geometrie des Geo-Objektes
     * @return das Polygon des Objektes
     */
    public Vector<GeoObjectPart> getGeometry() {
        return mGeometry;
    }

    /**
     * Liefert die Bounding Box der Geometrie
     * @return die Boundin Box der Geometrie als Rechteckobjekt
     * @see Rectangle
     */
    public Rectangle getBounds() {
        Rectangle currentBounds;
        Rectangle overallBounds = mGeometry.firstElement().getBounds();

        for (int i = 1; i < mGeometry.size(); i++) {
            currentBounds = mGeometry.get(i).getBounds();
            overallBounds = overallBounds.union(currentBounds);
        }
        return overallBounds;
    }
    /**
     * Gibt die internen Informationen des Geo-Objektes als
     * String zurueck
     * @return Der Inhalt des Objektes in Form eines Strings
     */
    public String toString() {
        String retString = "ID: " + mId + ", Type: " + mType + "\n";
        retString += mGeometry.toString();
        return retString;
    }
    /**
     * Das Geo-Objekt verwendet den uebergebenen Graphik-Kontext,
     * um sich zu zeichnen
     * @param _g Der Graphik-Kontext in dem sich das Objekt zeichnen soll
     * @param _m Die Transformationsmatrix, die für die Zeichenoperation
     * verwendet werden soll
     * @param _schema Das PresentationsSchema zum Zeichnen
     * @see Graphics
     * @see at.fhooe.mc.ois.Matrix
     */
    public void paint(Graphics _g, Matrix _m, PresentationSchema _schema) {
        for (GeoObjectPart tempGeo:mGeometry) {
            tempGeo.draw(_g, _m, _schema);
        }
    }


}
