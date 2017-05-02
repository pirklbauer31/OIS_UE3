package at.fhooe.mc.ois;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * Created by Pirklbauer on 25.04.2017.
 */
public class GISDialog extends JDialog implements ItemListener,WindowListener {

    //Das DrawingPanel Objekt
    private DrawingPanel mDrawingPanel;
    //Label für die ID
    private Label mTxtId;
    //Label für den ObjektTyp
    private Label mTxtType;
    //Vektor mit GeoObjects
    private Vector<GeoObject> mObjects;
    //Das Fenster-Objekt
    private Frame mFrame;

    public GISDialog(Vector<GeoObject> _objects) {
        mFrame = new Frame();
        mFrame.setSize(500,300);
        mFrame.addWindowListener(this);

        Panel mDataPanel = new Panel();
        mDataPanel.setBackground(Color.lightGray);
        mDataPanel.setLayout(new FlowLayout());

        Label lblId = new Label("object id");
        Label lblType = new Label("object type");
        Label lblGeoType = new Label("object geometry type");

        mTxtId = new Label();
        mTxtType = new Label();
        Label txtGeoType = new Label();

        Choice cboObjects = new Choice();
        cboObjects.addItemListener(this);

        Panel selectionPanel = new Panel();
        selectionPanel.setBackground(Color.white);

        mDrawingPanel = new DrawingPanel();
        mDrawingPanel.setBackground(Color.lightGray);
        mDrawingPanel.setSize(500, 100);

        mFrame.setLayout(new BorderLayout());
        mFrame.add(selectionPanel, BorderLayout.NORTH);
        mFrame.add(mDataPanel, BorderLayout.SOUTH);
        mFrame.add(mDrawingPanel, BorderLayout.CENTER);
        mDataPanel.add(lblId);
        mDataPanel.add(mTxtId);
        mDataPanel.add(lblType);
        mDataPanel.add(mTxtType);
        mDataPanel.add(lblGeoType);
        selectionPanel.add(cboObjects);
        mDataPanel.add(txtGeoType);

        if (_objects != null) {
            //txtId.setText(_objects.get(0).getId());
            //txtType.setText(String.valueOf(_objects.get(0).getType()));
            mObjects = _objects;
            for (GeoObject tempgeo: _objects) {
                cboObjects.add(tempgeo.getId());
            }
            loadObject(_objects.firstElement());
        }

        mFrame.setResizable(false);
        mFrame.setVisible(true);
    }

    /**
     * Lädt die Daten des übergeben Objekts in die Label und zeichnet die Polygone in das DrawingPanel.
     * @param _object
     */
    private void loadObject(GeoObject _object) {
        mTxtId.setText(_object.getId());
        mTxtType.setText(String.valueOf(_object.getType()));

        Matrix mMatrixTrans = Matrix.zoomToFit(_object.getBounds(), new Rectangle(0, 0, mDrawingPanel.getWidth(), mDrawingPanel.getHeight()));

        BufferedImage mImage = new BufferedImage(mDrawingPanel.getWidth(), mDrawingPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = (Graphics2D) mImage.getGraphics();
        graphics2D.setColor(Color.lightGray);
        graphics2D.fillRect(0,0, mDrawingPanel.getWidth(), mDrawingPanel.getHeight());
        //graphics2D.setColor(Color.white);
        _object.paint(graphics2D, mMatrixTrans, null);

        mDrawingPanel.setmImage(mImage);
        mDrawingPanel.repaint();
    }

    /**
     * Ruft die loadObject Methode je nach ausgewähltem ComboBox Objekt auf.
     * @param e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        for (int i = 0; i < mObjects.size(); i++) {
            if(mObjects.get(i).getId().equals(e.getItem().toString())) {
                loadObject(mObjects.get(i));
            }
        }


    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        mFrame.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
