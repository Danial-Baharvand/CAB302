package com.endgame.vectordesigntool;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTests extends JPanel {
    private MouseEvent me;

    /*
     * Method simulating a mouse click
     */
    public void mouseClick(int x, int y) {
        me = new MouseEvent(Gui.canvas, 0,0,0,x,y,1,false);
        for(MouseListener ml: Gui.canvas.getMouseListeners()) {
            ml.mouseClicked(me);
        }
    }

    @BeforeEach
    public void setUp(){
        Gui.makeCanvas();
        Gui.tempVEC = "";
    }

    @Test
    public void testDrawPlot()  {
        Gui.selectBtn = Gui.Type.PLOT; //Select to draw plot
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        assertEquals("PLOT 0.001 0.001\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testDrawLine()  {
        Gui.selectBtn = Gui.Type.LINE; //Select to draw plot
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(2, 2);
        assertEquals("LINE 0.001 0.001 0.002 0.002\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testDrawRect()  {
        Gui.selectBtn = Gui.Type.RECTANGLE; //Select to draw plot
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(2, 2);
        assertEquals("RECTANGLE 0.001 0.001 0.002 0.002\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testDrawEllipse()  {
        Gui.selectBtn = Gui.Type.ELLIPSE; //Select to draw plot
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(2, 2);
        assertEquals("ELLIPSE 0.001 0.001 0.002 0.002\n", Gui.tempVEC); //Check if the coordinates are in the tempVec    }

    @Test
    public void testGrid() {

    }
}
