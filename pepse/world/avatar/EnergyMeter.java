package pepse.world.avatar;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.Color;

/**
 * The energy meter class.
 */
public class EnergyMeter extends GameObject {

    // Class constants.
    private static final Vector2 METER_DIMENSIONS = new Vector2(100, 30);
    private static final String INITIAL_TEXT = "Energy: 100%";
    private static final String ENERGY_FORMAT = "Energy: %.0f%%";
    private static final float LOW_ENERGY_THRESHOLD = 20f;

    // Member variables.
    private final TextRenderable textRenderable;

    /**
     * Constructs a new EnergyMeter object to display the avatar's energy level.
     * @param pos The top-left position of the meter on the screen.
     */
    public EnergyMeter(Vector2 pos) {
        super(pos, METER_DIMENSIONS, null);
        this.textRenderable = new TextRenderable(INITIAL_TEXT);
        this.textRenderable.setColor(Color.WHITE);
        this.renderer().setRenderable(textRenderable);
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Updates the displayed energy value and changes the text color based on the level.
     * @param energyValue The current energy percentage to display.
     */
    public void updateEnergy(float energyValue) {
        this.textRenderable.setString(String.format(ENERGY_FORMAT, energyValue));

        if (energyValue < LOW_ENERGY_THRESHOLD) {
            textRenderable.setColor(Color.RED);
        } else {
            textRenderable.setColor(Color.WHITE);
        }
    }
}