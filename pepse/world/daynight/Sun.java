package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * The sun class.
 */
public class Sun {

    // Class constants.
    private static final float SUN_SIZE = 100f;
    private static final String SUN_TAG = "sun";
    private static final float WINDOW_CENTER_X_FACTOR = 0.5f;
    private static final float CYCLE_CENTER_Y_FACTOR = 2f / 3f;
    private static final float INITIAL_SUN_Y_FACTOR = 1f / 3f;
    private static final float INITIAL_ANGLE = 0f;
    private static final float FINAL_ANGLE = 360f;

    /**
     * Creates a GameObject representing the sun.
     * The sun revolves around a calculated cycle center in a circular path.
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The duration of a full day-night cycle in seconds.
     * @return The created sun GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        OvalRenderable sunRenderer = new OvalRenderable(Color.YELLOW);

        Vector2 cycleCenter = new Vector2(
                windowDimensions.x() * WINDOW_CENTER_X_FACTOR,
                windowDimensions.y() * CYCLE_CENTER_Y_FACTOR
        );

        Vector2 initialSunCenter = new Vector2(
                windowDimensions.x() * WINDOW_CENTER_X_FACTOR,
                windowDimensions.y() * INITIAL_SUN_Y_FACTOR
        );

        GameObject sun = new GameObject(
                initialSunCenter,
                new Vector2(SUN_SIZE, SUN_SIZE),
                sunRenderer
        );

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        new Transition<>(
                sun,
                (Float angle) -> sun.setCenter(
                        initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)
                ),
                INITIAL_ANGLE,
                FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        return sun;
    }
}