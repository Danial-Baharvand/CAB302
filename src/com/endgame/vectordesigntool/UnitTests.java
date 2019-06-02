package com.endgame.vectordesigntool;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.*;
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
        Gui.selectBtn = Gui.Type.LINE; //Select to draw line
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(2, 2);
        assertEquals("LINE 0.001 0.001 0.002 0.002\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testDrawRect()  {
        Gui.selectBtn = Gui.Type.RECTANGLE; //Select to draw rectangle
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(2, 2);
        assertEquals("RECTANGLE 0.001 0.001 0.002 0.002\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testDrawEllipse()  {
        Gui.selectBtn = Gui.Type.ELLIPSE; //Select to draw ellipse
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(2, 2);
        assertEquals("ELLIPSE 0.001 0.001 0.002 0.002\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testSetPenColourButton() {
        //Create a dummy colour chooser to test with
        Gui.colors = new JColorChooser(Color.GREEN);

        //Draw a plot with the new colour
        Gui.selectBtn = Gui.Type.PLOT; //Select to draw plot
        mouseClick(1, 1); //Clicks on the canvas and draws plot at 1,1 coordinates

        //Press the SetColourButton
        Gui.doChangePenColour();

        //Check the colour is in the temp vec
        assertEquals("PLOT 0.001 0.001\nPEN #00ff00\n", Gui.tempVEC);
    }

    @Test
    public void testSetFillColourButton() {
        //Create a dummy colour chooser to test with
        Gui.colors = new JColorChooser(Color.ORANGE);

        //Draw a rectangle with the new colour
        Gui.selectBtn = Gui.Type.RECTANGLE; //Select to draw plot
        mouseClick(1, 1); //Clicks on the canvas and draws rectangle at 1,1 to 2,2 coordinates
        mouseClick(2, 2);

        //Press the SetColourButton
        Gui.doChangeFillColour();

        //Check the colour is in the temp vec
        assertEquals("RECTANGLE 0.001 0.001 0.002 0.002\nFILL #ffc800\n", Gui.tempVEC);
    }

    @Test
    public void testNoFillColourButton() {
        //Create a dummy colour chooser to test with
        Gui.colors = new JColorChooser(Color.ORANGE);

        //Draw a rectangle a fill colour
        Gui.selectBtn = Gui.Type.RECTANGLE; //Select to draw plot
        mouseClick(1, 1); //Clicks on the canvas and draws rectangle at 1,1 to 2,2 coordinates
        mouseClick(2, 2);

        //Press the SetColourButton
        Gui.doRemoveFillColour();

        //Check the colour is in the temp vec
        assertEquals("RECTANGLE 0.001 0.001 0.002 0.002\nFILL OFF\n", Gui.tempVEC);
    }
}
