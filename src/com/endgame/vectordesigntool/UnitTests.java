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
        Gui.gridX = 0;
        Gui.gridY = 0;
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
        assertEquals("ELLIPSE 0.001 0.001 0.002 0.002\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testGridPlot() {
        Gui.gridX = 50;
        Gui.gridY = 50;
        Gui.selectBtn = Gui.Type.PLOT; //Select to draw plot
        mouseClick(40, 40); //Clicks on the canvas and draws plot at 0,0 coordinates
        assertEquals("PLOT 0.05 0.05\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testGridLine()  {
        Gui.gridX = 50;
        Gui.gridY = 50;
        Gui.selectBtn = Gui.Type.LINE; //Select to draw plot
        mouseClick(40, 30); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(60, 40);
        assertEquals("LINE 0.05 0.05 0.05 0.05\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testGridRect() {
        Gui.gridX = 50;
        Gui.gridY = 50;
        Gui.selectBtn = Gui.Type.RECTANGLE; //Select to draw plot
        mouseClick(40, 30); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(60, 40);
        assertEquals("RECTANGLE 0.05 0.05 0.05 0.05\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testGridEllipse() {
        Gui.gridX = 50;
        Gui.gridY = 50;
        Gui.selectBtn = Gui.Type.ELLIPSE; //Select to draw plot
        mouseClick(40, 30); //Clicks on the canvas and draws plot at 0,0 coordinates
        mouseClick(60, 40);
        assertEquals("ELLIPSE 0.05 0.05 0.05 0.05\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testGridPoly() {
        Gui.gridX = 50;
        Gui.gridY = 50;
        Shapes.polAdd( 40,  30);
        Shapes.polAdd( 30,  40);
        Shapes.polAdd( 20,  50);
        Shapes.polygon();
        assertEquals("POLYGON 0.04 0.03 0.03 0.04 0.02 0.05\n", Gui.tempVEC); //Check if the coordinates are in the tempVec
    }

    @Test
    public void testGridNumberFormatException() throws NumberFormatException {
        Gui.gridXField = new JTextField();
        Gui.gridYField = new JTextField();
        Gui.gridXField.setText("asdf"); //Adds text in the text field
        Gui.gridYField.setText("qwer"); //Adds text in the text field
        assertThrows(NumberFormatException.class, () -> {
            Gui.gridX = Integer.parseInt(Gui.gridXField.getText());
            Gui.gridY = Integer.parseInt(Gui.gridYField.getText());
        });
    }
}
