package com.endgame.vectordesigntool;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

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
        assertEquals("ELLIPSE 0.001 0.001 0.002 0.002\n", Gui.tempVEC);
        Gui.doUndo();

        //Check if the coordinates are in the tempVec file
    }
    @Test
    public void testUndo()  {
        Gui.tempVEC="PLOT 0.001 0.001\nPLOT 0.002 0.002\n";
        Gui.doUndo();
        assertEquals("PLOT 0.001 0.001\n", Gui.tempVEC);
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
    public void testDrawPolygonPoints()  {
        Shapes.polAdd( 1,  1);
        Shapes.polAdd( 1,  10);
        Shapes.polAdd( 10,  1);
        assertEquals(Shapes.polX.toString(), "[1, 1, 10]");
        assertEquals(Shapes.polY.toString(), "[1, 10, 1]");
        //Check if the coordinates are in the tempVec file
    }
    @Test
    public void testDrawPolygonFinish()  {

        Shapes.polAdd( 1,  1);
        Shapes.polAdd( 1,  10);
        Shapes.polAdd( 10,  1);
        Shapes.polygon();
        assertEquals("POLYGON 0.001 0.001 0.001 0.01 0.01 0.001\n", Gui.tempVEC);
        //Check if the coordinates are in the tempVec file
    }
    @Test
    public void confirmHistoryActionTest()  {
        Gui.tempVEC="PLOT 0.001 0.001\nPLOT 0.002 0.002\n";
        Gui.historyTempVEC="PLOT 0.001 0.001";
        Gui.confirmHistory();
        assertEquals("PLOT 0.001 0.001\n", Gui.tempVEC);
        assertEquals(-1, Gui.selectedHistory);

    }
    @Test
    public void updateHistoryTest()  {
        Gui.tempVEC="PLOT 0.001 0.001\nPLOT 0.002 0.002\nPLOT 0.003 0.003\n";
        Gui.model = new DefaultListModel<>();
        Gui.model.addAll(Arrays.asList(Gui.tempVEC.split("\n")));//adds each line of tempVEC as an item in the list
        Gui.list = new JList<>( Gui.model );//make a new JList with the model as its contents
        Gui.list.setSelectedIndex(1);
        Gui.updateHistory();
        assertEquals("PLOT 0.001 0.001\nPLOT 0.002 0.002",Gui.historyTempVEC);

    }
    @Test
    public void intCanvasTest()  {
        Gui.canvSize=1000;
        assertEquals(300,MyPanel.intCanvas("0.3"));;

    }
    @Test
    public void addHisTOTempTest()  {
        Shapes.History.add("[POLYGON, 0.36, 0.093, 0.735, 0.187, 0.585, 0.526, \n" +
                "]");
        Gui.tempVEC="";
        Shapes.addHisTOTemp();
        assertEquals("POLYGON 0.36 0.093 0.735 0.187 0.585 0.526\n",Gui.tempVEC);
        Shapes.History.clear();

    }
    @Test
    public void addToHisAndTempTest()  {
        Gui.tempVEC="";
        Shapes.addToHisAndTemp(10,10, "PLOT");
        assertEquals("PLOT 0.01 0.01\n",Gui.tempVEC);

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
