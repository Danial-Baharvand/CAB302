package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;

import java.util.Scanner;
//THIS CLASS READS FROM TEMP AND DRAWS THE SHAPES
//ADDITIONAL COMMENTS WILL BE ADDED LATER

/**
 * This reads the corresponding coordinate points in the temp file and then implements the shapes
 *
 * @version 4.0
 */
class MyPanel extends JPanel {
    Scanner scanner;
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(Gui.zoomScale, Gui.zoomScale);

        String drawingTool = null;
        scanner = new Scanner(Gui.tempVEC);

        while (scanner.hasNext()) {
            if (scanner.hasNext())drawingTool = scanner.next();
            if(!Shapes.readyToDraw){
                Shapes.pressedX = -3; //Used to reset the stored initial point in case the window is moved
            }
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
                        System.out.print("tempvec= "+Gui.tempVEC);
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
            }
        }
    }

    private static int intCanvas(String s){
        return (int)(Float.valueOf(s)*Gui.canvSize);
    }
}