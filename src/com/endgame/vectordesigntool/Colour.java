package com.endgame.vectordesigntool;

import java.awt.*;

public class Colour {
    private static Color colour;

    /**
     * Set a new colour to draw with.
     * @param newColour the new colour
     */
    public static void setColour(Color newColour) {
        colour = newColour;
    }

    /**
     * Get the colour selected in the color chooser.
     * @return the chosen Color
     */
    public static Color getColour() {
        return colour;
    }
}
