package at.fhooe.mc.ois;


import java.awt.*;
import java.util.Hashtable;

public class DrawingContext {

    /**
     * Ein Hashtable, der die verschiedenen PresentationsSchemata enthält
     */
    private Hashtable<Integer, PresentationSchema> mSchemata = new Hashtable<>();

    public DrawingContext() {
        mSchemata.put(233, new PresentationSchema(Color.black, Color.white, 2.0f));
        mSchemata.put(931, new PresentationSchema(Color.black, Color.red, 3.0f));
        mSchemata.put(932, new PresentationSchema(Color.red, Color.orange, 5.0f));
        mSchemata.put(1101, new PresentationSchema(Color.green, Color.magenta, 1.0f));

        //boundary
        mSchemata.put(8005, new PresentationSchema(Color.black, Color.lightGray, 2.0f));

        //building
        mSchemata.put(9099, new PresentationSchema(Color.black, Color.orange, 3.0f));
        mSchemata.put(9001, new PresentationSchema(Color.black, Color.orange, 3.0f));
        mSchemata.put(9002, new PresentationSchema(Color.black, Color.orange, 3.0f));
        mSchemata.put(9003, new PresentationSchema(Color.black, Color.orange, 3.0f));


        //natural
        mSchemata.put(6001, new PresentationSchema(Color.black, Color.green, 2.0f));
        mSchemata.put(6002, new PresentationSchema(Color.black, new Color(139,69,19), 3.0f));
        mSchemata.put(6005, new PresentationSchema(Color.blue, Color.blue, 2.0f));

        //landuse
        mSchemata.put(5001, new PresentationSchema(Color.black, Color.white, 1.0f));
        mSchemata.put(5002, new PresentationSchema(Color.black, Color.red, 1.0f));
        mSchemata.put(5003, new PresentationSchema(Color.black, Color.cyan, 1.0f));
        mSchemata.put(5004, new PresentationSchema(Color.black, new Color(139,69,19), 1.0f));
        mSchemata.put(5006, new PresentationSchema(Color.black, Color.green, 1.0f));

        //highway
        mSchemata.put(1100, new PresentationSchema(Color.black, Color.black, 2.0f));
        mSchemata.put(1120, new PresentationSchema(Color.green, Color.black, 2.0f));
        mSchemata.put(1130, new PresentationSchema(new Color(139,69,19), Color.black, 2.0f));
        mSchemata.put(1040, new PresentationSchema(Color.orange, Color.black, 2.0f));
        mSchemata.put(1030, new PresentationSchema(Color.orange, Color.black, 2.0f));
        mSchemata.put(1080, new PresentationSchema(Color.black, Color.black, 2.0f));
        mSchemata.put(1070, new PresentationSchema(Color.black, Color.black, 2.0f));
        mSchemata.put(1010, new PresentationSchema(Color.red, Color.black, 2.0f));
        mSchemata.put(1020, new PresentationSchema(Color.red, Color.black, 2.0f));




    }

    /**
     * Liefert das zum übergebenen Typ passende PresentationsSchema aus dem Hashtable
     * @param _type Der Typ des gewünschten Schemas
     * @return Das gewünschte PresentationsSchema
     */
    public PresentationSchema getSchema(int _type) {
        return mSchemata.get(_type);
    }
}
