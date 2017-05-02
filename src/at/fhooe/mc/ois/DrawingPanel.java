package at.fhooe.mc.ois;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Pirklbauer on 05.04.2017.
 */
public class DrawingPanel extends Panel {

    /**
     * The current image to draw when repaint is called.
     */
    private BufferedImage mImage;

    public DrawingPanel() {

    }

    /**
     * Draws the Image stored in mImage on the DrawingPanel
     * @param g The current paint context
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if(mImage != null) {
            g.drawImage(mImage, 0,0, null);
        }

    }

    //getters and setters

    public BufferedImage getmImage() {
        return mImage;
    }

    public void setmImage(BufferedImage _image) {
        this.mImage = _image;
    }
}
