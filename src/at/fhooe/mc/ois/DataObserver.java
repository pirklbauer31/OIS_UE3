package at.fhooe.mc.ois;

import java.awt.image.BufferedImage;

/**
 * Created by Pirklbauer on 05.04.2017.
 */

/**
 * Interface for Model-View connection, contains update method.
 */
public interface DataObserver {
    /**
     * Used to update observers from model
     * @param _data
     */
    public void update(BufferedImage _data);
}
