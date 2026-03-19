package pepse.world.trees;

import danogl.GameObject;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The flora class.
 */
public class Flora {

    // Class constants.
    private static final float TREE_PLANTING_PROBABILITY = 0.1f;

    // Member variables.
    private final Function<Float, Float> groundHeightFunc;
    private final Consumer<Float> addEnergyCallback;
    private final int gameSeed;

    /**
     * Constructs a Flora manager for creating trees.
     * @param groundHeightFunc  A function that returns the ground height
     *                         at a given x-coordinate.
     * @param addEnergyCallback A callback to execute when a fruit is
     *                         eaten (adds energy to avatar).
     * @param gameSeed          The base seed for deterministic random generation.
     */
    public Flora(Function<Float, Float> groundHeightFunc, Consumer<Float>
            addEnergyCallback, int gameSeed) {
        this.groundHeightFunc = groundHeightFunc;
        this.addEnergyCallback = addEnergyCallback;
        this.gameSeed = gameSeed;
    }

    /**
     * Creates trees within a specified range of x-coordinates.
     * @param minX The starting x-coordinate.
     * @param maxX The ending x-coordinate.
     * @return A list of game objects representing the tree
     * components (trunks, leaves, fruits).
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        List<GameObject> allTreeParts = new ArrayList<>();

        int startX = (minX / Block.SIZE) * Block.SIZE;
        int endX = (maxX / Block.SIZE) * Block.SIZE;

        for (int x = startX; x < endX; x += Block.SIZE) {
            Random random = new Random(Objects.hash(x, gameSeed));

            if (random.nextFloat() < TREE_PLANTING_PROBABILITY) {
                float groundY = groundHeightFunc.apply((float) x);
                int groundBlockY = (int) Math.floor(groundY / Block.SIZE) * Block.SIZE;

                List<GameObject> newTree = Tree.create(x, groundBlockY,
                        addEnergyCallback, Objects.hash(x, gameSeed));
                allTreeParts.addAll(newTree);
            }
        }

        return allTreeParts;
    }
}