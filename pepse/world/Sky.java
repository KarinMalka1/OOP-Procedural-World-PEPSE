package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;

import java.awt.Color;
import java.util.Random;

public class Sky {

    // Class constants.
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    private static final String SKY_TAG = "sky";

    private static final String CLOUD_IMAGE_PATH = "assets/cloud.png";
    private static final String PLANE_IMAGE_PATH = "assets/plane3.png";
    private static final String BIRD_IMAGE_PATH = "assets/bird3.jpeg";

    private static final int NUM_CLOUDS = 5;
    private static final Vector2 CLOUD_DIMENSIONS = new Vector2(100, 40);
    private static final int CLOUD_MAX_Y_DIVISOR = 3;
    private static final int CLOUD_BASE_VELOCITY = 20;
    private static final int CLOUD_VELOCITY_VARIANCE = 20;

    private static final Vector2 PLANE_DIMENSIONS = new Vector2(60, 20);
    private static final Vector2 PLANE_START_POS = new Vector2(0, 50);
    private static final Vector2 PLANE_VELOCITY = new Vector2(100, 0);

    private static final int NUM_BIRDS = 3;
    private static final Vector2 BIRD_DIMENSIONS = new Vector2(15, 10);
    private static final int BIRD_MAX_Y_DIVISOR = 2;
    private static final int BIRD_BASE_VELOCITY = 40;
    private static final int BIRD_VELOCITY_VARIANCE = 30;

    /**
     * Creates the background sky object.
     * @param windowDimensions The dimensions of the game window.
     * @return A GameObject representing the fixed background sky.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(SKY_TAG);
        return sky;
    }

    /**
     * Creates and adds atmospheric objects (clouds, planes, birds) to the game.
     * These objects loop horizontally across the screen.
     * @param gameObjects      The collection to add the objects to.
     * @param windowDimensions The dimensions of the game window.
     * @param layer            The layer index to add these objects to.
     * @param imageReader      The image reader for loading object textures.
     */
    public static void createSkyObjects(GameObjectCollection gameObjects,
                                        Vector2 windowDimensions, int layer,
                                        ImageReader imageReader) {
        Random random = new Random();

        Renderable cloudImage = imageReader.readImage(CLOUD_IMAGE_PATH,
                true);

        for (int i = 0; i < NUM_CLOUDS; i++) {
            Vector2 pos = new Vector2(
                    random.nextInt((int) windowDimensions.x()),
                    random.nextInt((int) windowDimensions.y() / CLOUD_MAX_Y_DIVISOR)
            );

            GameObject cloud = new LoopingGameObject(pos, CLOUD_DIMENSIONS,
                    cloudImage, windowDimensions);
            cloud.setVelocity(new Vector2(CLOUD_BASE_VELOCITY +
                    random.nextInt(CLOUD_VELOCITY_VARIANCE), 0));
            cloud.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            gameObjects.addGameObject(cloud, layer);
        }

        Renderable planeImage = imageReader.readImage(PLANE_IMAGE_PATH,
                true);

        GameObject plane = new LoopingGameObject(PLANE_START_POS,
                PLANE_DIMENSIONS, planeImage, windowDimensions);
        plane.setVelocity(PLANE_VELOCITY);
        plane.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(plane, layer);

        Renderable birdImage = imageReader.readImage(BIRD_IMAGE_PATH,
                true);

        for (int i = 0; i < NUM_BIRDS; i++) {
            Vector2 pos = new Vector2(
                    random.nextInt((int) windowDimensions.x()),
                    random.nextInt((int) windowDimensions.y() /
                            BIRD_MAX_Y_DIVISOR)
            );

            GameObject bird = new LoopingGameObject(pos, BIRD_DIMENSIONS,
                    birdImage, windowDimensions);
            bird.setVelocity(new Vector2(BIRD_BASE_VELOCITY +
                    random.nextInt(BIRD_VELOCITY_VARIANCE), 0));
            bird.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            gameObjects.addGameObject(bird, layer);
        }
    }

    private static class LoopingGameObject extends GameObject {
        private final Vector2 windowDimensions;

        public LoopingGameObject(Vector2 topLeftCorner, Vector2 dimensions,
                                 Renderable renderable, Vector2 windowDimensions) {
            super(topLeftCorner, dimensions, renderable);
            this.windowDimensions = windowDimensions;
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            if (getTopLeftCorner().x() > windowDimensions.x()) {
                setTopLeftCorner(new Vector2(-getDimensions().x(),
                        getTopLeftCorner().y()));
            }
        }
    }
}