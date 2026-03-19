package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.utils.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;
import java.util.Random;

/**
 * The leaf class.
 */
public class Leaf extends GameObject {

    // Class constants.
    private static final Color BASE_LEAF_COLOR = new
            Color(50, 200, 30);
    private static final String LEAF_TAG = "leaf";
    private static final float SWING_START_ANGLE = -5f;
    private static final float SWING_END_ANGLE = 5f;
    private static final float SWING_TRANSITION_TIME = 2f;
    private static final float WIDTH_TRANSITION_TIME = 3f;
    private static final float WIDTH_SHRINK_AMOUNT = 2f;

    /**
     * Constructs a Leaf GameObject at a specific location.
     * The leaf initializes with a wind animation that affects its
     * angle and dimensions.
     * @param topLeftCorner The top-left position of the leaf.
     */
    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, Vector2.ONES.mult(Block.SIZE),
                new RectangleRenderable(ColorSupplier.
                        approximateColor(BASE_LEAF_COLOR)));

        setTag(LEAF_TAG);
        startWindAnimation();
    }

    private void startWindAnimation() {
        Random random = new Random();
        float delay = random.nextFloat();

        new ScheduledTask(this, delay, false, () ->
                new Transition<>(
                        this,
                        this.renderer()::setRenderableAngle,
                        SWING_START_ANGLE,
                        SWING_END_ANGLE,
                        Transition.LINEAR_INTERPOLATOR_FLOAT,
                        SWING_TRANSITION_TIME,
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                        null
                )
        );

        new ScheduledTask(this, delay, false, () ->
                new Transition<>(
                        this,
                        (Float width) -> this.setDimensions(new Vector2(width,
                                this.getDimensions().y())),
                        (float) Block.SIZE,
                        (float) Block.SIZE - WIDTH_SHRINK_AMOUNT,
                        Transition.LINEAR_INTERPOLATOR_FLOAT,
                        WIDTH_TRANSITION_TIME,
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                        null
                )
        );
    }
}