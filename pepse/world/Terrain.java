package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.utils.ColorSupplier;
import pepse.utils.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The terrain class.
 */
public class Terrain {

    // Class constants.
    private static final Color BASE_GROUND_COLOR = new
            Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final int NOISE_FACTOR = Block.SIZE * 7;
    private static final float GROUND_HEIGHT_FRACTION = 2f / 3f;
    private static final String GROUND_TAG = "ground";

    // Member variables.
    private final float groundHeightAtX0;
    private final NoiseGenerator noiseGenerator;

    /**
     * Constructs a Terrain object responsible for generating the ground.
     * @param windowDimensions The dimensions of the game window.
     * @param seed             A seed for the noise generator to ensure
     *                        deterministic terrain generation.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.groundHeightAtX0 = windowDimensions.y() * GROUND_HEIGHT_FRACTION;
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Calculates the height of the ground at a specific x-coordinate
     * using Perlin noise.
     * @param x The x-coordinate.
     * @return The y-coordinate of the ground surface at x.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Creates a list of ground blocks within a specified x-range.
     * @param minX The starting x-coordinate.
     * @param maxX The ending x-coordinate.
     * @return A list of Block objects representing the ground in the given range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();

        int startX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;

        for (int x = startX; x < maxX; x += Block.SIZE) {
            float heightAtX = groundHeightAt(x);
            int startY = (int) Math.floor(heightAtX / Block.SIZE) * Block.SIZE;

            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                int y = startY + (i * Block.SIZE);

                RectangleRenderable blockRender = new RectangleRenderable(
                        ColorSupplier.approximateColor(BASE_GROUND_COLOR)
                );

                Vector2 pos = new Vector2(x, y);
                Block block = new Block(pos, blockRender);
                block.setTag(GROUND_TAG);

                blocks.add(block);
            }
        }
        return blocks;
    }
}