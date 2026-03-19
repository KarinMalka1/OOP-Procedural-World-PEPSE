package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The night class.
 */
public class Night {

    // Class constants.
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final Float NOON_OPACITY = 0f;
    private static final String NIGHT_TAG = "night";
    private static final int HALF_CYCLE_DIVISOR = 2;

    /**
     * Creates a GameObject representing the night.
     * The object is a black rectangle that covers the screen and changes opacity cyclically.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The duration of a full day-night cycle in seconds.
     * @return The created night GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        Renderable nightRenderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                nightRenderable
        );

        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);

        new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                NOON_OPACITY,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / HALF_CYCLE_DIVISOR,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );

        return night;
    }
}