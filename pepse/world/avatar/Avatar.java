package pepse.world.avatar;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * The avatar class.
 */
public class Avatar extends GameObject {

    // Class constants.
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_JUMP = -650;
    private static final float GRAVITY = 600;

    private static final float MAX_ENERGY = 100f;
    private static final float ENERGY_GAIN_IDLE = 1f;
    private static final float ENERGY_COST_RUN = 2f;
    private static final float ENERGY_COST_JUMP = 20f;
    private static final float ENERGY_COST_DOUBLE_JUMP = 50f;
    private static final float AVATAR_SIZE = 50;
    private static final float TIME_BETWEEN_CLIPS = 0.1f;

    private static final String GROUND_TAG = "ground";
    private static final String AVATAR_TAG = "avatar";
    private static final String IDLE_0_IMAGE = "assets/idle_0.png";

    private static final String[] IDLE_ASSETS = {"assets/idle_0.png",
            "assets/idle_1.png", "assets/idle_2.png", "assets/idle_3.png"};
    private static final String[] RUN_ASSETS = {"assets/run_0.png",
            "assets/run_1.png", "assets/run_2.png", "assets/run_3.png",
            "assets/run_4.png", "assets/run_5.png"};
    private static final String[] JUMP_ASSETS = {"assets/jump_0.png",
            "assets/jump_1.png", "assets/jump_2.png", "assets/jump_3.png"};

    // member variables.
    private final UserInputListener inputListener;
    private float energy;
    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable runAnimation;
    private final AnimationRenderable jumpAnimation;
    private final Consumer<Float> onEnergyChange; // Callback for UI

    // State.
    private enum State { IDLE, RUN, JUMP }
    private State currentState = State.IDLE;

    /**
     * @param topLeftCorner Starting position
     * @param inputListener For reading keys
     * @param imageReader For loading images
     * @param onEnergyChange Callback to update the UI (optional, can be null)
     */
    public Avatar(Vector2 topLeftCorner, UserInputListener inputListener,
                  ImageReader imageReader, Consumer<Float> onEnergyChange) {
        super(topLeftCorner, Vector2.ONES.mult(AVATAR_SIZE),
                imageReader.readImage(IDLE_0_IMAGE,
                        true));
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        this.onEnergyChange = onEnergyChange;

        this.idleAnimation = new AnimationRenderable(IDLE_ASSETS,
                imageReader, true, TIME_BETWEEN_CLIPS);
        this.runAnimation = new AnimationRenderable(RUN_ASSETS,
                imageReader, true, TIME_BETWEEN_CLIPS);
        this.jumpAnimation = new AnimationRenderable(JUMP_ASSETS,
                imageReader, true, TIME_BETWEEN_CLIPS);

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        setTag(AVATAR_TAG);
    }

    /**
     * Overrides the update method.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float xVel = 0;
        boolean isGrounded = getVelocity().y() == 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (!isGrounded || energy >= ENERGY_COST_RUN) {
                xVel += VELOCITY_X;
                renderer().setIsFlippedHorizontally(false);
            }
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (!isGrounded || energy >= ENERGY_COST_RUN) {
                xVel -= VELOCITY_X;
                renderer().setIsFlippedHorizontally(true);
            }
        }

        transform().setVelocityX(xVel);

        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            if (isGrounded && energy >= ENERGY_COST_JUMP) {
                transform().setVelocityY(VELOCITY_JUMP);
                updateEnergy(-ENERGY_COST_JUMP);
            }
            else if (!isGrounded && getVelocity().y() > 0 && energy >=
                    ENERGY_COST_DOUBLE_JUMP) {
                transform().setVelocityY(VELOCITY_JUMP);
                updateEnergy(-ENERGY_COST_DOUBLE_JUMP);
            }
        }

        if (isGrounded) {
            if (xVel == 0) {
                currentState = State.IDLE;
                if (energy < MAX_ENERGY) {
                    updateEnergy(ENERGY_GAIN_IDLE);
                }
            } else {
                currentState = State.RUN;
                if (energy >= ENERGY_COST_RUN) {
                    updateEnergy(-ENERGY_COST_RUN);
                }
            }
        } else {
            currentState = State.JUMP;
        }
        updateAnimation();
    }

    private void updateEnergy(float delta) {
        this.energy += delta;
        this.energy = Math.min(energy, MAX_ENERGY);
        this.energy = Math.max(energy, 0);

        if (onEnergyChange != null) {
            onEnergyChange.accept(this.energy);
        }
    }

    private void updateAnimation() {
        switch (currentState) {
            case IDLE:
                renderer().setRenderable(idleAnimation);
                break;
            case RUN:
                renderer().setRenderable(runAnimation);
                break;
            case JUMP:
                renderer().setRenderable(jumpAnimation);
                break;
        }
    }

    private boolean isFalling() {
        return getVelocity().y() > 0;
    }

    /**
     * Overrides the onCollisionEnter method.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals(GROUND_TAG) && isFalling()) {
            System.out.println();
            this.transform().setVelocityY(0);
        }
    }

    /**
     * Public method to add energy (used by fruits).
     */
    public void addEnergy(float amount) {
        updateEnergy(amount);
    }
}