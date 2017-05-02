package at.fhooe.mc.ois;

import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Pirklbauer on 05.04.2017.
 */
public class GISView implements DataObserver {

    /**
     * The Drawing Panel to paint images in.
     */
    private DrawingPanel mPanel = new DrawingPanel();
    /**
     * Wird für zeichnen des Aufzieh-Rechtecks verwendet
     */
    private Rectangle mOldRect = null;
    /**
     * Das GISDialog Objekt, welches bei Doppelclick zu öffnen ist.
     */
    private GISDialog mDialog;

    public GISView (GISController _controller) {
        Frame mFrame = new Frame();
        mFrame.setSize(600,400);

        mFrame.addWindowListener(_controller);

        Button mButtonL = new Button();
        mButtonL.setLabel("Load");
        mButtonL.setBackground(Color.YELLOW);
        mButtonL.setActionCommand("l");
        mButtonL.addActionListener(_controller);
        Button mButtonLO = new Button();
        mButtonLO.setLabel("LoadOSM");
        mButtonLO.setBackground(Color.YELLOW);
        mButtonLO.setActionCommand("lo");
        mButtonLO.addActionListener(_controller);
        Button mButtonZTF = new Button();
        mButtonZTF.setLabel("ZTF");
        mButtonZTF.setBackground(Color.YELLOW);
        mButtonZTF.setActionCommand("ztf");
        mButtonZTF.addActionListener(_controller);
        Button mButtonZoomIn = new Button();
        mButtonZoomIn.setLabel("+");
        mButtonZoomIn.setBackground(Color.YELLOW);
        mButtonZoomIn.setActionCommand("+");
        mButtonZoomIn.addActionListener(_controller);
        Button mButtonZoomOut = new Button();
        mButtonZoomOut.setLabel("-");
        mButtonZoomOut.setBackground(Color.YELLOW);
        mButtonZoomOut.setActionCommand("-");
        mButtonZoomOut.addActionListener(_controller);
        Button mButtonScrollUp = new Button();
        mButtonScrollUp.setLabel("N");
        mButtonScrollUp.setBackground(Color.YELLOW);
        mButtonScrollUp.setActionCommand("N");
        mButtonScrollUp.addActionListener(_controller);
        Button mButtonScrollDown = new Button();
        mButtonScrollDown.setLabel("S");
        mButtonScrollDown.setBackground(Color.YELLOW);
        mButtonScrollDown.setActionCommand("S");
        mButtonScrollDown.addActionListener(_controller);
        Button mButtonScrollLeft = new Button();
        mButtonScrollLeft.setLabel("W");
        mButtonScrollLeft.setBackground(Color.YELLOW);
        mButtonScrollLeft.setActionCommand("W");
        mButtonScrollLeft.addActionListener(_controller);
        Button mButtonScrollRight = new Button();
        mButtonScrollRight.setLabel("E");
        mButtonScrollRight.setBackground(Color.YELLOW);
        mButtonScrollRight.setActionCommand("E");
        mButtonScrollRight.addActionListener(_controller);
        Button mButtonRotateRight = new Button();
        mButtonRotateRight.setLabel("Rotate right");
        mButtonRotateRight.setBackground(Color.YELLOW);
        mButtonRotateRight.setActionCommand("RR");
        mButtonRotateRight.addActionListener(_controller);
        Button mButtonRotateLeft = new Button();
        mButtonRotateLeft.setLabel("Rotate left");
        mButtonRotateLeft.setBackground(Color.YELLOW);
        mButtonRotateLeft.setActionCommand("RL");
        mButtonRotateLeft.addActionListener(_controller);
        Button mButtonSticky = new Button();
        mButtonSticky.setLabel("Sticky");
        mButtonSticky.setBackground(Color.YELLOW);
        mButtonSticky.setActionCommand("sticky");
        mButtonSticky.addActionListener(_controller);
        Button mButtonStore = new Button();
        mButtonStore.setLabel("Store");
        mButtonStore.setBackground(Color.YELLOW);
        mButtonStore.setActionCommand("store");
        mButtonStore.addActionListener(_controller);


        mPanel.setBackground(Color.lightGray);
        mPanel.addComponentListener(_controller);
        mPanel.addMouseListener(_controller);
        mPanel.addMouseMotionListener(_controller);
        mPanel.addMouseWheelListener(_controller);

        Panel navigationPanel = new Panel();
        navigationPanel.setLayout(new FlowLayout());
        navigationPanel.setBackground(Color.white);

        mFrame.setLayout(new BorderLayout());
        mFrame.add(mPanel, BorderLayout.CENTER);
        mFrame.add(navigationPanel, BorderLayout.SOUTH);
        navigationPanel.add(mButtonL);
        navigationPanel.add(mButtonLO);
        navigationPanel.add(mButtonZTF);
        navigationPanel.add(mButtonZoomIn);
        navigationPanel.add(mButtonZoomOut);
        navigationPanel.add(mButtonScrollUp);
        navigationPanel.add(mButtonScrollDown);
        navigationPanel.add(mButtonScrollLeft);
        navigationPanel.add(mButtonScrollRight);
        navigationPanel.add(mButtonRotateLeft);
        navigationPanel.add(mButtonRotateRight);
        navigationPanel.add(mButtonSticky);
        navigationPanel.add(mButtonStore);
        //mFrame.add(mButtonD, BorderLayout.SOUTH);
        //mFrame.add(mButtonZTF,BorderLayout.SOUTH);

        mFrame.setVisible(true);
    }

    /**
     * Sets the Image to paint in the DrawingPanel and calls repaint on it.
     * @param _data The image to paint in the panel
     */
    @Override
    public void update(BufferedImage _data) {
        mPanel.setmImage(_data);
        mPanel.repaint();
    }


    //getters and setters

    public DrawingPanel getmPanel() {
        return mPanel;
    }

    public void setmPanel(DrawingPanel _mPanel) {
        this.mPanel = _mPanel;
    }

    public Rectangle getmOldRect() {
        return mOldRect;
    }

    public void setmOldRect(Rectangle _OldRect) {
        this.mOldRect = _OldRect;
    }

    public GISDialog getmDialog() {
        return mDialog;
    }

    public void setmDialog(GISDialog _Dialog) {
        this.mDialog = _Dialog;
    }
}
