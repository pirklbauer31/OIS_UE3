package at.fhooe.mc.ois;

import java.awt.*;

/**
 * Created by Pirklbauer on 06.04.2017.
 */
public class Matrix {
    double m11, m12, m13, m21, m22, m23, m31, m32, m33;


    /**
     * Standardkonstruktor
     */
    public Matrix() {

    }
    /**
     * Standardkonstruktor
     * @param _m11 Der Wert des Matrix Feldes 1x1 (Zeile1/Spalte1)
     * @param _m12 Der Wert des Matrix Feldes 1x2 (Zeile1/Spalte2)
     * …
     */
    public Matrix(double _m11, double _m12, double _m13,
                  double _m21, double _m22, double _m23,
                  double _m31, double _m32, double _m33) {
        m11 = _m11;
        m12 = _m12;
        m13 = _m13;
        m21 = _m21;
        m12 = _m12;
        m22 = _m22;
        m23 = _m23;
        m31 = _m31;
        m32 = _m32;
        m33 = _m33;
    }

    /**
     * Liefert eine String-Repräsentation der Matrix
     *
     * @return Ein String mit dem Inhalt der Matrix
     * @see String
     */
    public String toString() {
        String matrixStr = "[" + m11 +  " " + m12 + " " + m13 + "]\n";
        matrixStr += "[" + m21 + " " + m22 + " " + m23 + "]\n";
        matrixStr += "[" + m31 + " " + m32 + " " + m33 + "]\n";
        return matrixStr;
    }

    /**
     * Liefert die Invers-Matrix der Transformationsmatrix
     * @return Die Invers-Matrix
     */
    public Matrix invers() {
        double b11 = m22*m33-m23*m32;
        double b12 = m13*m32-m12*m33;
        double b13 = m12*m23-m13*m22;
        double b21 = m23*m31-m21*m33;
        double b22 = m11*m33-m13*m31;
        double b23 = m13*m21-m11*m23;
        double b31 = m21*m32-m22*m31;
        double b32 = m12*m31-m11*m32;
        double b33 = m11*m22-m12*m21;
        double amount = m11*m22*m33 + m12*m23*m31 + m13*m21*m32 - m11*m23*m32 - m12*m21*m33 - m13*m22*m31;
        b11 *= 1/amount;
        b12 *= 1/amount;
        b13 *= 1/amount;
        b21 *= 1/amount;
        b22 *= 1/amount;
        b23 *= 1/amount;
        b31 *= 1/amount;
        b32 *= 1/amount;
        b33 *= 1/amount;
        Matrix invMat = new Matrix(b11,b12,b13,b21,b22,b23,b31,b32,b33);
        return invMat;
    }

    /**
     * Liefert eine Matrix, die das Ergebnis einer Matrizen-
     * multiplikation zwischen dieser und der übergebenen Matrix
     * ist
     *
     * @param _other Die Matrix mit der Multipliziert werden soll
     * @return Die Ergebnismatrix der Multiplikation
     */
    public Matrix multiply(Matrix _other) {
        double b11 = m11*_other.m11 + m12*_other.m21 + m13*_other.m31;
        double b12 = m11*_other.m12 + m12*_other.m22 + m13*_other.m32;
        double b13 = m11*_other.m13 + m12*_other.m23 + m13*_other.m33;

        double b21 = m21*_other.m11 + m22*_other.m21 + m23*_other.m31;
        double b22 = m21*_other.m12 + m22*_other.m22 + m23*_other.m32;
        double b23 = m21*_other.m13 + m22*_other.m23 + m23*_other.m33;

        double b31 = m31*_other.m11 + m32*_other.m21 + m33*_other.m31;
        double b32 = m31*_other.m12 + m32*_other.m22 + m33*_other.m32;
        double b33 = m31*_other.m13 + m32*_other.m23 + m33*_other.m33;

        return new Matrix(b11,b12,b13,b21,b22,b23,b31,b32,b33);
    }

    /**
     * Multipliziert einen Punkt mit der Matrix und liefert das
     * Ergebnis der Multiplikation zurück
     *
     * @param _pt Der Punkt, der mit der Matrix multipliziert
     * werden soll
     * @return Ein neuer Punkt, der das Ergebnis der
     * Multiplikation repräsentiert
     * @see Point
     */
    public Point multiply(Point _pt) {
        double x = m11*_pt.getX() + m12*_pt.getY() + m13*1;
        double y = m21*_pt.getX() + m22*_pt.getY() + m23*1;

        Point retPoint = new Point();
        retPoint.setLocation((int)x,(int)y);
        return retPoint;
    }

    /**
     * Multipliziert ein Rechteck mit der Matrix und liefert das
     * Ergebnis der Multiplikation zurück
     *
     * @param _rect Das Rechteck, das mit der Matrix multipliziert
     * werden soll
     * @return Ein neues Rechteck, das das Ergebnis der
     * Multiplikation repräsentiert
     * @see Rectangle
     */
    public Rectangle multiply(Rectangle _rect) {
        Point p1 = new Point(_rect.x, _rect.y);
        Point p2 = new Point();
        p2.setLocation(_rect.x+_rect.getWidth(), _rect.y+_rect.getHeight());
        p1 = multiply(p1);
        p2 = multiply(p2);
        Rectangle retRect = new Rectangle(p1);
        retRect.add(p2);
        return retRect;
    }

    /**
     * Multipliziert ein Polygon mit der Matrix und liefert das
     * Ergebnis der Multiplikation zurück
     *
     * @param _poly Das Polygon, das mit der Matrix multipliziert
     * werden soll
     * @return Ein neues Polygon, das das Ergebnis der
     * Multiplikation repräsentiert
     * @see Polygon
     */
    public Polygon multiply(Polygon _poly) {
        Polygon retPoly = new Polygon();
        for (int i = 0; i < _poly.npoints; i++) {
            double x = m11*_poly.xpoints[i] + m12*_poly.ypoints[i] + m13*1;
            double y = m21*_poly.xpoints[i] + m22*_poly.ypoints[i] + m23*1;
            retPoly.addPoint((int)x, (int)y);
        }

        return retPoly;
    }

    /**
     * Liefert eine Translationsmatrix
     *
     * @param _x Der Translationswert der Matrix in X-Richtung
     * @param _y Der Translationswert der Matrix in Y-Richtung
     * @return Die Translationsmatrix
     */
    public static Matrix translate(double _x, double _y) {
        return new Matrix(1,0,_x,0,1,_y,0,0,1);
    }

    /**
     * Liefert eine Translationsmatrix
     *
     * @param _pt Ein Punkt, der die Translationswerte enthält
     * @return Die Translationsmatrix
     * @see Point
     */
    public static Matrix translate(Point _pt) {
        return new Matrix(1,0,_pt.x,0,1,_pt.y,0,0,1);
    }

    /**
     * Liefert eine Skalierungsmatrix
     *
     * @param _scaleVal Der Skalierungswert der Matrix
     * @return Die Skalierungsmatrix
     */
    public static Matrix scale(double _scaleVal) {
        return new Matrix(_scaleVal,0,0,0,_scaleVal,0,0,0,1);
    }

    /**
     * Liefert eine Spiegelungsmatrix (X-Achse)
     *
     * @return Die Spiegelungsmatrix
     */
    public static Matrix mirrorX() {
        return new Matrix(1,0,0,0,-1,0,0,0,1);
    }

    /**
     * Liefert eine Spiegelungsmatrix (Y-Achse)
     *
     * @return Die Spiegelungsmatrix
     */
    public static Matrix mirrorY() {
        return new Matrix(-1,0,0,0,1,0,0,0,1);
    }

    /**
     * Liefert eine Rotationsmatrix
     *
     * @param _alpha Der Winkel (in rad), um den rotiert werden
     * soll
     * @return Die Rotationsmatrix
     */
    public static Matrix rotate(double _alpha) {
        return new Matrix(Math.cos(_alpha), -Math.sin(_alpha),0,Math.sin(_alpha), Math.cos(_alpha),0,0,0,1);
    }

    /**
     * Liefert den Faktor, der benötigt wird, um das _world-
     * Rechteck in das _win-Rechteck zu skalieren (einzupassen)
     * bezogen auf die X-Achse  Breite
     *
     * @param _world Das Rechteck in Weltkoordinaten
     * @param _win Das Rechteck in Bildschirmkoordinaten
     * @return Der Skalierungsfaktor
     * @see Rectangle
     */
    public static double getZoomFactorX(Rectangle _world, Rectangle _win) {
        return _win.getWidth()/_world.getWidth();
    }

    /**
     * Liefert den Faktor, der benötigt wird, um das _world-
     * Rechteck in das _win-Rechteck zu skalieren (einzupassen)
     * bezogen auf die Y-Achse  Höhe
     *
     * @param _world Das Rechteck in Weltkoordinaten
     * @param _win Das Rechteck in Bildschirmkoordinaten
     * @return Der Skalierungsfaktor
     * @see Rectangle
     */
    public static double getZoomFactorY(Rectangle _world, Rectangle _win) {
        return _win.getHeight()/_world.getHeight();
    }

    /**
     * Liefert eine Matrix, die alle notwendigen Transformationen
     * beinhaltet (Translation, Skalierung, Spiegelung und
     * Translation), um ein _world-Rechteck in ein _win-Rechteck
     * abzubilden
     *
     * @param _world Das Rechteck in Weltkoordinaten
     * @param _win Das Rechteck in Bildschirmkoordinaten
     * @return Die Transformationsmatrix
     * @see Rectangle
     */
    public static Matrix zoomToFit(Rectangle _world,
                                   Rectangle _win) {
        double zoomX = Matrix.getZoomFactorX(_world, _win);
        double zoomY = Matrix.getZoomFactorY(_world, _win);
        double scaleFactor;

        Matrix translationZero = translate(-_world.getCenterX(), -_world.getCenterY());
        if(zoomX < zoomY) {
            scaleFactor = zoomX;
        }
        else {
            scaleFactor = zoomY;
        }
        Matrix scale = scale(scaleFactor);
        Matrix mirrorX = mirrorX();
        Matrix translationMid = translate(_win.getCenterX(), _win.getCenterY());

        Matrix transformation = translationMid.multiply(mirrorX);
        transformation = transformation.multiply(scale);
        transformation = transformation.multiply(translationZero);

        return  transformation;
    }

    /**
     * Liefert eine Matrix, die eine vorhandene Transformations-
     * matrix erweitert, um an einem bestimmten Punkt um einen
     * bestimmten Faktor in die Karte hinein- bzw. heraus zu
     * zoomen
     *
     * @param _old Die zu erweiternde Transformationsmatrix
     * @param _zoomPt Der Punkt an dem gezoomt werden soll
     * @param _zoomScale Der Zoom-Faktor um den gezoomt werden
     * soll
     * @return Die neue Transformationsmatrix
     * @see Point
     */
    public static Matrix zoomPoint(Matrix _old,
                                   Point _zoomPt,
                                   double _zoomScale) {
        Matrix translationZero = translate(-_zoomPt.x, -_zoomPt.y);
        Matrix scale = scale(_zoomScale);
        Matrix translationToPoint = translate(_zoomPt);

        Matrix transformationNew = translationToPoint.multiply(scale.multiply(translationZero.multiply(_old)));

        return transformationNew;
    }

    /**
     * Liefert einen GeoDoublePoint, der mit der vorhandenen Transformationsmatrix multipliziert wurde.
     * @param _pt Der GeoDoublePoint der zu multiplizieren ist
     * @return Der transformierte GeoDoublePoint
     */
    public GeoDoublePoint multiply(GeoDoublePoint _pt) {
        double srcx = _pt.getmX();
        double srcy = _pt.getmY();
        double destx = m11 * srcx + m12 * srcy;
        double desty = m21 * srcx + m22 * srcy;
        return new GeoDoublePoint(destx,desty);
    }


}
