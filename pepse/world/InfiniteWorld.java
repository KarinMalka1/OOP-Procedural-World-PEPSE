package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.util.Vector2;
import pepse.world.trees.Flora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The infinite world class.
 */
public class InfiniteWorld extends GameObject {

    // Class constants.
    private static final int RENDER_DISTANCE = 1000;
    private static final int LEAF_LAYER_OFFSET = 100;
    private static final String TRUNK_TAG = "trunk";
    private static final String LEAF_TAG = "leaf";
    private static final String FRUIT_TAG = "fruit";

    // Member variables.
    private final GameObjectCollection gameObjects;
    private final GameObject avatar;
    private final Terrain terrain;
    private final Flora flora;
    private final Map<Integer, List<GameObject>> worldCache = new HashMap<>();

    /**
     * Constructs an InfiniteWorld manager that dynamically generates and removes
     * world objects (terrain, trees) based on the avatar's position.
     * @param gameObjects The collection to which generated objects are added.
     * @param avatar      The avatar object used to determine the visible world range.
     * @param terrain     The terrain generator.
     * @param flora       The flora (tree) generator.
     */
    public InfiniteWorld(GameObjectCollection gameObjects,
                         GameObject avatar,
                         Terrain terrain,
                         Flora flora) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.gameObjects = gameObjects;
        this.avatar = avatar;
        this.terrain = terrain;
        this.flora = flora;

        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Updates the world state by generating new columns of terrain and trees
     * entering the render distance and removing those that have moved out of range.
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        float avatarX = avatar.getCenter().x();

        int minX = (int) (avatarX - RENDER_DISTANCE);
        int maxX = (int) (avatarX + RENDER_DISTANCE);

        int startBlockX = (minX / Block.SIZE) * Block.SIZE;
        int endBlockX = (maxX / Block.SIZE) * Block.SIZE;

        for (int x = startBlockX; x <= endBlockX; x += Block.SIZE) {
            if (!worldCache.containsKey(x)) {
                createColumn(x);
            }
        }

        removeFarObjects(minX, maxX);
    }

    private void createColumn(int x) {
        List<GameObject> newObjects = new ArrayList<>();

        List<Block> groundBlocks = terrain.createInRange(x, x + Block.SIZE);
        for (Block b : groundBlocks) {
            gameObjects.addGameObject(b, Layer.STATIC_OBJECTS);
            newObjects.add(b);
        }

        List<GameObject> treeObjects = flora.createInRange(x, x + Block.SIZE);
        for (GameObject t : treeObjects) {
            if (t.getTag().equals(TRUNK_TAG)) {
                gameObjects.addGameObject(t, Layer.STATIC_OBJECTS);
            } else if (t.getTag().equals(LEAF_TAG)) {
                gameObjects.addGameObject(t, Layer.BACKGROUND +
                        LEAF_LAYER_OFFSET);
            } else if (t.getTag().equals(FRUIT_TAG)) {
                gameObjects.addGameObject(t, Layer.STATIC_OBJECTS);
            }
            newObjects.add(t);
        }

        worldCache.put(x, newObjects);
    }

    private void removeFarObjects(int minX, int maxX) {
        List<Integer> keysToRemove = new ArrayList<>();

        for (int x : worldCache.keySet()) {
            if (x < minX || x > maxX) {
                keysToRemove.add(x);
            }
        }

        for (int x : keysToRemove) {
            List<GameObject> objs = worldCache.get(x);
            for (GameObject obj : objs) {
                gameObjects.removeGameObject(obj, Layer.STATIC_OBJECTS);
                gameObjects.removeGameObject(obj, Layer.BACKGROUND +
                        LEAF_LAYER_OFFSET);
            }
            worldCache.remove(x);
        }
    }
}