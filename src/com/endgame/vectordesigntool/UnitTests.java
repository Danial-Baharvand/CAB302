package com.endgame.vectordesigntool;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTests extends JPanel {
    private MouseEvent me;

    public void mouseClick(int x, int y) {
        me = new MouseEvent(Gui.canvas, 0,0,0,x,y,1,false);
        for(MouseListener ml: Gui.canvas.getMouseListeners()) {
            ml.mouseClicked(me);
        }
    }

    @Test
    public void testDrawPlot()  {
        Gui.selectBtn = Gui.Type.PLOT; //Select to draw plot

        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        assertEquals("0.0", Gui.tempVEC);

        //Check if the coordinates are in the tempVec file
    }


}
