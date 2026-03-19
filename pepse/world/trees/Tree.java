package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.utils.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * The tree class.
 */
public class Tree {

    // Class constants.
    private static final Color BASE_TRUNK_COLOR = new
            Color(100, 50, 20);
    private static final int MIN_TREE_HEIGHT = 8;
    private static final int MAX_TREE_HEIGHT = 12;
    private static final int CANOPY_RADIUS = 2;
    private static final float LEAF_DENSITY_THRESHOLD = 0.85f;
    private static final float FRUIT_CREATION_PROBABILITY = 0.1f;
    private static final float FRUIT_OFFSET = 5f;
    private static final String TRUNK_TAG = "trunk";

    /**
     * Creates a single tree consisting of a trunk, leaves, and fruits at a specified location.
     * @param rootX             The x-coordinate of the tree's root.
     * @param groundY           The y-coordinate of the ground level where the tree is planted.
     * @param addEnergyCallback A callback to execute when a fruit from this tree is eaten.
     * @param seed              A seed for deterministic random generation of the tree's structure.
     * @return A list of GameObjects constituting the tree.
     */
    public static List<GameObject> create(int rootX, int groundY,
                                          Consumer<Float> addEnergyCallback,
                                          int seed) {
        List<GameObject> treeParts = new ArrayList<>();
        Random random = new Random(seed);

        int treeHeight = random.nextInt(MAX_TREE_HEIGHT - MIN_TREE_HEIGHT)
                + MIN_TREE_HEIGHT;
        int trunkTopY = groundY;

        for (int i = 0; i < treeHeight; i++) {
            int currentY = groundY - (i * Block.SIZE) - Block.SIZE;

            GameObject trunkBlock = new GameObject(
                    new Vector2(rootX, currentY),
                    new Vector2(Block.SIZE, Block.SIZE),
                    new RectangleRenderable(ColorSupplier.
                            approximateColor(BASE_TRUNK_COLOR))
            );

            trunkBlock.physics().preventIntersectionsFromDirection(Vector2.ZERO);
            trunkBlock.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
            trunkBlock.setTag(TRUNK_TAG);

            treeParts.add(trunkBlock);

            if (i == treeHeight - 1) {
                trunkTopY = currentY;
            }
        }

        for (int i = -CANOPY_RADIUS; i <= CANOPY_RADIUS; i++) {
            for (int j = -CANOPY_RADIUS; j <= CANOPY_RADIUS; j++) {
                int leafX = rootX + (i * Block.SIZE);
                int leafY = trunkTopY + (j * Block.SIZE);

                if (random.nextFloat() > LEAF_DENSITY_THRESHOLD) {
                    continue;
                }

                Leaf leaf = new Leaf(new Vector2(leafX, leafY));
                treeParts.add(leaf);

                if (random.nextFloat() < FRUIT_CREATION_PROBABILITY) {
                    Fruit fruit = new Fruit(
                            new Vector2(leafX + FRUIT_OFFSET, leafY + FRUIT_OFFSET),
                            addEnergyCallback
                    );
                    treeParts.add(fruit);
                }
            }
        }

        return treeParts;
    }
}