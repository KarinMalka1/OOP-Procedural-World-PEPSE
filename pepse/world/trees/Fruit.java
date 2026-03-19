package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.function.Consumer;

/**
 * The fruit class.
 */
public class Fruit extends GameObject {

    // Class constants.
    private static final float FRUIT_SIZE = 15f;
    private static final Color FRUIT_COLOR = Color.RED;
    private static final String FRUIT_TAG = "fruit";
    private static final String AVATAR_TAG = "avatar";
    private static final float ENERGY_GAIN = 10f;
    private static final float RESPAWN_TIME = 30f;
    private static final float INVISIBLE_OPACITY = 0f;
    private static final float VISIBLE_OPACITY = 1f;

    // Member variables.
    private final Consumer<Float> onEat;

    /**
     * Constructs a new Fruit object that gives energy when collected.
     * @param topLeftCorner The position of the fruit in the world.
     * @param onEat         Callback to execute when the fruit is eaten (adds energy).
     */
    public Fruit(Vector2 topLeftCorner, Consumer<Float> onEat) {
        super(topLeftCorner, Vector2.ONES.mult(FRUIT_SIZE), new OvalRenderable(FRUIT_COLOR));
        this.onEat = onEat;
        this.setTag(FRUIT_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (other.getTag().equals(AVATAR_TAG)) {
            onEat.accept(ENERGY_GAIN);
            renderer().setOpaqueness(INVISIBLE_OPACITY);

            new ScheduledTask(
                    this,
                    RESPAWN_TIME,
                    false,
                    () -> renderer().setOpaqueness(VISIBLE_OPACITY)
            );
        }
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(AVATAR_TAG) && renderer().getOpaqueness()
                == VISIBLE_OPACITY;
    }
}