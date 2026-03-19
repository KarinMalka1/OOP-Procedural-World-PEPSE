package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.InfiniteWorld;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.avatar.Avatar;
import pepse.world.avatar.EnergyMeter;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.util.List;
import java.util.Random;


/**
 * The pepse game manager class.
 */
public class PepseGameManager extends GameManager {

    // The class constants.
    private static final int SKY_OBJECTS_LAYER_OFFSET = 30;
    private static final int SUN_LAYER_OFFSET = 20;
    private static final int HALO_LAYER_OFFSET = 10;
    private static final float DAY_CYCLE_LENGTH = 30f;

    private static final float ENERGY_METER_X = 50f;
    private static final float ENERGY_METER_Y = 50f;
    private static final float AVATAR_Y_OFFSET = 50f;

    private static final float CAMERA_CENTER_FACTOR = 0.5f;

    /**
     * The main entry point for the Pepse game.
     * @param args command line arguments (unused).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game world, including sky, terrain, avatar, and other objects.
     * This method sets up the game environment, physics, and interactions.
     *
     * @param imageReader      Contains methods for reading images from the disk.
     * @param soundReader      Contains methods for reading sound files from the disk.
     * @param inputListener    Contains methods for reading user input (keyboard/mouse).
     * @param windowController Contains methods for controlling the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        Sky.createSkyObjects(gameObjects(), windowDimensions, Layer.BACKGROUND
                + SKY_OBJECTS_LAYER_OFFSET, imageReader);

        int seed = new Random().nextInt();
        Terrain terrain = new Terrain(windowDimensions, seed);

        GameObject sun = Sun.create(windowDimensions, DAY_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND + SUN_LAYER_OFFSET);

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND +
                HALO_LAYER_OFFSET);

        GameObject night = Night.create(windowDimensions, DAY_CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.FOREGROUND);

        Vector2 energyPos = new Vector2(ENERGY_METER_X, ENERGY_METER_Y);
        EnergyMeter energyMeter = new EnergyMeter(energyPos);
        gameObjects().addGameObject(energyMeter, Layer.UI);

        float screenCenterX = windowDimensions.x() * CAMERA_CENTER_FACTOR;
        float groundHeightAtCenter = terrain.groundHeightAt(screenCenterX);

        Vector2 startPos = new Vector2(screenCenterX, groundHeightAtCenter
                - AVATAR_Y_OFFSET);

        Avatar avatar = new Avatar(startPos, inputListener, imageReader,
                energyMeter::updateEnergy);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        Flora flora = new Flora(
                terrain::groundHeightAt,
                avatar::addEnergy,
                seed
        );

        Vector2 initialAvatarLocation = new Vector2(screenCenterX,
                windowDimensions.y() * CAMERA_CENTER_FACTOR);
        Vector2 cameraOffset = windowDimensions.mult(CAMERA_CENTER_FACTOR).
                subtract(initialAvatarLocation);

        setCamera(new Camera(
                avatar,
                cameraOffset,
                windowDimensions,
                windowDimensions
        ));

        InfiniteWorld infiniteWorld = new InfiniteWorld(
                gameObjects(),
                avatar,
                terrain,
                flora
        );

        gameObjects().addGameObject(infiniteWorld, Layer.STATIC_OBJECTS);
    }
}



