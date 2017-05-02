package at.fhooe.mc.ois;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Pirklbauer on 05.04.2017.
 */
public class GISController extends WindowAdapter implements ActionListener, ComponentListener, MouseListener, MouseMotionListener, MouseWheelListener{

    /**
     * Object of the observer-pattern model
     */
    private GISModel mModel;
    //Startpunkt für Mausbewegungen
    private Point mStartPoint;
    //Startpunkt für Mouse-dragged
    private Point mOriginPoint;
    //Gedrückte Maustaste
    private int mButtonPressed;

    /**
     * Constructor to initialize the controller with a given model
     * @param _model The model to use
     */
    public GISController (GISModel _model) {
        mModel = _model;
    }

    /**
     * Called when trying to close the frame. Exits the program.
     * @param _e The window event that has been triggered
     */
    @Override
    public void windowClosing(WindowEvent _e) {
        System.exit(0);
    }

    /**
     * Called when actionlistener triggers an event (Button in the frame has been pressed)
     * @param _e The ActionEvent that has been triggered
     */
    @Override
    public void actionPerformed(ActionEvent _e) {
        String eventCommand = _e.getActionCommand();

        switch (eventCommand) {
            case "d" : {
                System.out.println("Draw has been pressed!");
                mModel.setMouseEvent(false);
                break;
            }
            case "l" : {
                System.out.println("Load has been pressed!");
                mModel.setMouseEvent(false);
                mModel.loadData();
                //mModel.initPostGIS();
                break;
            }
            case "lo" : {
                System.out.println("LoadOSM has been pressed!");
                mModel.setMouseEvent(false);
                //mModel.loadData();
                mModel.initPostGIS();
                break;
            }
            case "ztf" : {
                System.out.println("ZoomToFit has been pressed!");
                mModel.zoomToFit();
                break;
            }
            case "+" : {
                System.out.println("ZoomIn has been pressed!");
                mModel.zoom(1.3);
                break;
            }
            case "-" : {
                System.out.println("ZoomOut has been pressed!");
                mModel.zoom(1/1.3);
                break;
            }
            case "N" : {
                System.out.println("ScrollUp has been pressed!");
                mModel.scrollVertical(20);

                break;
            }
            case "S" : {
                System.out.println("ScrollDown has been pressed!");
                mModel.scrollVertical(-20);
                break;
            }
            case "W" : {
                System.out.println("ScrollLeft has been pressed!");
                mModel.scrollHorizontal(20);
                break;
            }
            case "E" : {
                System.out.println("ScrollRight has been pressed!");
                mModel.scrollHorizontal(-20);
                break;
            }
            case "RL" : {
                System.out.println("RotateLeft has been pressed!");
                mModel.rotate(-20*Math.PI/180);
                break;
            }
            case "RR" : {
                System.out.println("RotateRight has been pressed!");
                mModel.rotate(20*Math.PI/180);
                break;
            }
            case "sticky": {
                if(mModel.mSticky == true) {
                    mModel.mSticky = false;
                    System.out.println("Sticky deactivated");
                }
                else {
                    mModel.mSticky = true;
                    mModel.getStickyBounds();
                    System.out.println("Sticky activated");
                }
                break;
            }
            case "store": {
                try {
                    BufferedImage imgToStore = mModel.getmObservers().get(0).getmPanel().getmImage();
                    File outputfile = new File("saved.png");
                    ImageIO.write(imgToStore, "png", outputfile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "scale": {
                String[] scaleValues = mModel.getmObservers().get(0).getmTxtScale().getText().split(":");
                if (isNumeric(scaleValues[0])) {
                    if (scaleValues.length > 1) {
                        if (isNumeric(scaleValues[1])) {
                            mModel.zoom((double) mModel.calculateScale() / (double) Integer.valueOf(scaleValues[1]));
                        }
                    } else if (scaleValues.length == 1) {
                        if (!scaleValues[0].equals("")) {
                            mModel.zoom((double) mModel.calculateScale() / (double) Integer.valueOf(scaleValues[0]));
                        }
                    }
                }
            } break;
        }


    }

    /**
     * Called when frame is resized. Calls setSize method of the model
     * @param _e The component event that has been triggered.
     */
    @Override
    public void componentResized(ComponentEvent _e) {
        int compWidth = _e.getComponent().getWidth();
        int compHeight = _e.getComponent().getHeight();
        mModel.setSize(compWidth, compHeight);
    }

    /**
     * Implementation unchanged. See componentMoved Documentation for reference.
     * @param _e
     */
    @Override
    public void componentMoved(ComponentEvent _e) {

    }

    /**
     * Implementation unchanged. See componentShown Documenatation for reference.
     * @param _e
     */
    @Override
    public void componentShown(ComponentEvent _e) {

    }

    /**
     * Implementation unchanged. See componentHidden Documentation for reference.
     * @param _e
     */
    @Override
    public void componentHidden(ComponentEvent _e) {

    }

    /**
     * Öffnet Dialog bei Doppleclick, Speichert die Koordinaten beim Mausclick im Zwischenspeicher.
     * @param _e
     */
    @Override
    public void mouseClicked(MouseEvent _e) {
        if (_e.getClickCount() == 2) {
            Vector<GeoObject> selectedObjects = mModel.initSelection(_e.getPoint());
            mModel.getmObservers().get(0).setmDialog(new GISDialog(selectedObjects));
        }

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        if((_e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
            Transferable t = cb.getContents(this);
            try {
                String oldClip = (String)t.getTransferData(DataFlavor.stringFlavor);
                cb.setContents(new StringSelection(oldClip + "(" + mStartPoint.x + "/" + mStartPoint.y + ")\n"), new StringSelection("Coords"));

            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            cb.setContents(new StringSelection("(" + mStartPoint.x + "/" + mStartPoint.y + ")\n"), new StringSelection("Coords"));
        }

    }

    /**
     * Speichert die Position des Mauszeigers beim Mausclick.
     * @param _e
     */
    @Override
    public void mousePressed(MouseEvent _e) {
        mModel.setmMousePosXStart(_e.getX());
        mModel.setmMousePosYStart(_e.getY());
        //System.out.println(_e.getX() + " " + _e.getY());
        mStartPoint = _e.getPoint();
        mOriginPoint = _e.getPoint();
        mButtonPressed = _e.getButton();
    }

    /**
     * Zeichnet Aufzieh-Rechteck bei Linkslick, Scrollt bei Rechtsclick
     * @param _e
     */
    @Override
    public void mouseReleased(MouseEvent _e) {
        mModel.setMouseEvent(true);
        //mModel.generateOnMousePos(_e.getX(), _e.getY(), _e.getButton());
        mModel.setmMousePosXEnd(_e.getX());
        mModel.setmMousePosYEnd(_e.getY());
        //mModel.scrollWithMouse();
        if(mButtonPressed == 1) {
            Rectangle zoomRectangle = new Rectangle(mStartPoint);
            zoomRectangle.add(_e.getPoint());
            //System.out.println(zoomRectangle.toString());

            if(zoomRectangle.getWidth() > 10 && zoomRectangle.getHeight() > 10) {
                mModel.zoomRect(zoomRectangle);
            }

            mModel.getmObservers().get(0).setmOldRect(null);
        }
        else if(mButtonPressed == 3) {
            double dragDistanceX = _e.getX()-mOriginPoint.x;
            double dragDistanceY = _e.getY()-mOriginPoint.y;

            mModel.scrollHorizontal((int)dragDistanceX);
            mModel.scrollVertical((int)dragDistanceY);
        }
        _e.getComponent().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Implementation unchanged. See mouseEntered Documentation for reference.
     * @param _e
     */
    @Override
    public void mouseEntered(MouseEvent _e) {

    }

    /**
     * Implementation unchanged. See mouseExited Documentation for reference.
     * @param _e
     */
    @Override
    public void mouseExited(MouseEvent _e) {

    }


    /**
     * Zeichnet zoom-Rechteck bei Linksclick, verschiebt Bild bei Rechtsclick.
     * @param _e
     */
    @Override
    public void mouseDragged(MouseEvent _e) {
        mModel.setmMousePosXCurrent(_e.getX());
        mModel.setmMousePosYCurrent(_e.getY());

        Graphics g = _e.getComponent().getGraphics();
        //leftclick, draw zooming rectangle
        if(mButtonPressed == 1) {
            _e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            Rectangle r = new Rectangle(mStartPoint);
            r.add(_e.getPoint());
            //System.out.println(r.toString());
            g.setXORMode(Color.white);
            GISView view = mModel.getmObservers().get(0);
            if(view.getmOldRect() != null) {
                Rectangle oldRect = view.getmOldRect();
                g.drawRect(oldRect.x, oldRect.y, (int)oldRect.getWidth(), (int)oldRect.getHeight());
            }
            g.drawRect(r.x, r.y, (int)r.getWidth(), (int)r.getHeight());
            view.setmOldRect(r);
            g.setPaintMode();
        }

        //rightclick, drag image
        else if(mButtonPressed == 3) {
            _e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            double dragDistanceX = _e.getX()-mStartPoint.x;
            double dragDistanceY = _e.getY()-mStartPoint.y;

            int delta = 2;

            //north
            g.clearRect(0,0, _e.getComponent().getWidth(),delta);
            //east
            g.clearRect(_e.getComponent().getWidth()-delta,0, delta, _e.getComponent().getHeight());
            //south
            g.clearRect(0, _e.getComponent().getHeight()-delta, _e.getComponent().getWidth(),delta);
            //west
            g.clearRect(0,0,delta, _e.getComponent().getHeight()-delta);

            g.copyArea(0,0, _e.getComponent().getWidth(), _e.getComponent().getHeight(), (int)dragDistanceX, (int)dragDistanceY);

            mStartPoint = _e.getPoint();
        }
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    /**
     * Implementation unchanged, see mouseMoved for reference.
     * @param _e
     */
    @Override
    public void mouseMoved(MouseEvent _e) {

    }

    /**
     * Zoomt das Bild bei Mausradbewegung entsprechend der Scroll-Richtung.
     * @param _e
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent _e) {
        //wheel up
        if(_e.getWheelRotation() < 0) {
            mModel.zoom(1.3);
        }
        //wheel down
        else {
            mModel.zoom(1/1.3);
        }
    }
}
