package at.fhooe.mc.ois;

import java.awt.*;

/**
 * Created by Kevin on 23.04.2017.
 */
public class PresentationSchema {
    //Die Farbe, in der die Linien der Polygone zu zeichnen sind
    private Color mLineColor = null;
    //Die Farbe, mit der die Polygone gefüllt werden sollen
    private Color mFillColor = null;
    //Die Liniendicke der Polygone
    private float mLineWidth = -1.0f;

    /**
     * Konstruktor
     * @param _linecolor Farbe zum Zeichnen von Linien
     * @param _fillcolor Farbe zum Ausfüllen der Objekte
     * @param _linewidth Die zu verwendene Linienbreite
     */
    public PresentationSchema(Color _linecolor, Color _fillcolor, Float _linewidth) {
        mLineColor = _linecolor;
        mFillColor = _fillcolor;
        mLineWidth = _linewidth;
    }

    //getters and setters

    public Color getmLineColor() {
        return mLineColor;
    }

    public void setmLineColor(Color _LineColor) {
        this.mLineColor = _LineColor;
    }

    public Color getmFillColor() {
        return mFillColor;
    }

    public void setmFillColor(Color _FillColor) {
        this.mFillColor = _FillColor;
    }

    public float getmLineWidth() {
        return mLineWidth;
    }

    public void setmLineWidth(float _LineWidth) {
        this.mLineWidth = _LineWidth;
    }
}
