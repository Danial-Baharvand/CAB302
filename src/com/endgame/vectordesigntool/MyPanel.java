package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Reads from temp file and then uses that information to enable the user to draw shapes onto the canvas.
 *
 * @author Group_010
 * @version 5.0
 */
class MyPanel extends JPanel {

    /**
     * all the painting has been implemented inside this method
     *
     * @param g paintComponent.Graphics
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //reset pen and fill colors to defaults
        Shapes.fillColor=null;
        Shapes.penColor=Color.black;
        Gui.model.clear(); //clear menu items
        Gui.model.addAll(Arrays.asList(Gui.tempVEC.split("\n"))); //update menu items from the latest temp
        Scanner scanner;
        if(Gui.selectedHistory<0){ //if a history item is not selected
            scanner = new Scanner(Gui.tempVEC); //read from the main temp
        } else {
            scanner = new Scanner(Gui.historyTempVEC); //read from the history temp
        }
        //draw grid if grid is enabled
        if(Gui.gridX>0 &&Gui.gridY>0){
            g.setColor(Color.lightGray);//set grid color
            for (int i=0;i<Gui.canvSize/Gui.gridX;i++){//draw the vertical grid lines
                g.drawLine(Gui.gridX*i, 0, Gui.gridX*i, Gui.canvSize);
            }
            for (int i=0;i<Gui.canvSize/Gui.gridY;i++){//draw the horizontal grid lines
                g.drawLine(0, Gui.gridY*i,Gui.canvSize , Gui.gridY*i);
            }
            g.setColor(Shapes.penColor);//revert the color
        }
        //read VEC instruction and draw to screen
        while (scanner.hasNext()) {
            //stores the shape to be drawn with
            String drawingTool = scanner.next();
            if(!Shapes.readyToDraw){
                Shapes.pressedX = -3;
            }
            try {
                switch (drawingTool) {
                    case "PLOT":
                        if (scanner.hasNextFloat()) {
                            Shapes.plot(intCanvas(scanner.next()), intCanvas(scanner.next()),g);
                        }
                        break;
                    case "LINE":
                        for (int i = 0; i < 2; i++) {
                            if (scanner.hasNext()) {
                                Shapes.line(intCanvas(scanner.next()), intCanvas(scanner.next()),g);
                            }
                        }
                        break;
                    case "RECTANGLE":
                        for (int i = 0; i < 2; i++) {
                            if (scanner.hasNext()) {
                                Shapes.rect(intCanvas(scanner.next()), intCanvas(scanner.next()),g);
                            }
                        }
                        break;
                    case "ELLIPSE":
                        for (int i = 0; i < 2; i++) {
                            if (scanner.hasNext()) {
                                Shapes.ellipse(intCanvas(scanner.next()), intCanvas(scanner.next()),g);
                            }
                        }
                        break;
                    case "POLYGON":
                        while (scanner.hasNextFloat()) {
                            Shapes.polAddLoad(intCanvas(scanner.next()), intCanvas(scanner.next()));
                        }
                        if(Shapes.readyToDraw) Shapes.polygon(g);
                        break;
                    case "PEN":
                        if (scanner.hasNext()) {
                            Shapes.penColor=Color.decode(scanner.next());
                            g.setColor(Shapes.penColor);
                        }
                        break;
                    case "FILL":
                        if (scanner.hasNext()) {
                            if (scanner.hasNext("OFF")){
                                scanner.next();
                                Shapes.fillColor=null;
                            }else {
                                Shapes.fillColor = Color.decode(scanner.next());
                            }
                        }
                        break;
                    default:
                        Gui.tempVEC = "";
                        g.dispose();
                        repaint();
                        //throw an exception if the draw instruction is non of the above
                        throw new Exception("The loaded file is or has become corrupted");

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                g.dispose();
                repaint();


            }
        }
    }

    /**
     * calculates correct coordinates for the VEC format based on the canvas size
     *
     * @param s coordinates of the VEC file format
     * @return the coordinates or the VEC format based on the canvas size
     */
    static int intCanvas(String s){
        return (int)(Float.valueOf(s)*Gui.canvSize);
    }
}