package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * The sun halo class.
 */
public class SunHalo {

    // Class contants.
    private static final Color HALO_COLOR = new Color(255,
            255, 0, 20);
    private static final Vector2 HALO_DIMENSIONS = new Vector2(200,
            200);
    private static final String SUN_HALO_TAG = "sunHalo";

    /**
     * Creates a halo GameObject that constantly follows the sun.
     * @param sun The sun object that this halo should surround.
     * @return The created halo GameObject.
     */
    public static GameObject create(GameObject sun) {
        OvalRenderable haloRenderer = new OvalRenderable(HALO_COLOR);
        GameObject sunHalo = new GameObject(Vector2.ZERO, HALO_DIMENSIONS,
                haloRenderer);

        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);

        sunHalo.addComponent((deltaTime) -> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;
    }
}