package at.fhooe.mc.ois;

/**
 * Created by Kevin on 26.04.2017.
 */
public class GeoDoublePoint {

    private double mX, mY;

    /**
     * Der Konstruktor.
     *
     * @param _x x-Koordinate.
     * @param _y y-Koordinate.
     */
    public GeoDoublePoint(double _x, double _y) {
        mX = _x;
        mY = _y;
    }
    /**
     * Berechnet die Länge des Vektors vom Ursprung
     * zum Punkt.
     *
     * @return Die Länge des Vektors
     */
    public double length() {
        return Math.sqrt(Math.pow(mX,2) + Math.pow(mY,2));
    }
    /**
     * Gibt den Punkt in Form eines Strings zurück.
     *
     * @return Punkt in Form eines Strings
     */
    public String toString() {
        return "(" + mX + "/" + mY + ")";
    }

    //getters and setters

    public double getmX() {
        return mX;
    }

    public void setmX(double _X) {
        this.mX = _X;
    }

    public double getmY() {
        return mY;
    }

    public void setmY(double _Y) {
        this.mY = _Y;
    }
}
