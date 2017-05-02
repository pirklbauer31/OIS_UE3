package at.fhooe.mc.ois;

import java.awt.*;
import java.util.Vector;

/**
 * Created by Pirklbauer on 30.04.2017.
 */
public abstract class GeoObjectPart {

    private Vector<GeoObjectPart> mHoles;

    public GeoObjectPart() {
        mHoles = new Vector<>();
    }

    public GeoObjectPart(Vector<GeoObjectPart> _holes) {
        mHoles = _holes;
    }

    /**
     * Draw Methode zum Zeichnen der Objekte. Wird in vererbten Objekten überschrieben.
     * @param _g Graphics-Kontext in dem gezeichnet werden soll
     * @param _m Die Transformationsmatrix
     * @param _schema Das zu verwendene Presentationsschema
     */
    abstract void draw(Graphics _g, Matrix _m, PresentationSchema _schema);

    /**
     * Methode zur Erstellung der Boundigbox. Wird in vererbten Objekten überschrieben.
     * @return Die Boundingbox.
     */
    abstract Rectangle getBounds();

    /**
     * Methode zur Überprüfung ob ein Punkt in Objekten enthalten ist. Wird in vererbten Objekten überschrieben.
     * @param _pt Der zu überprüfende Punkt
     * @return True falls enthalten, false falls nicht
     */
    abstract boolean contains(Point _pt);


    //getters and setters
    public Vector<GeoObjectPart> getmHoles() {
        return mHoles;
    }

    public void setmHoles(Vector<GeoObjectPart> _holes) {
        this.mHoles = _holes;
    }
}
