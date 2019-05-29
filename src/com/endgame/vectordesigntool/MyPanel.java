package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;
//THIS CLASS READS FROM TEMP AND DRAWS THE SHAPES
//ADDITIONAL COMMENTS WILL BE ADDED LATER
class MyPanel extends JPanel {
    private Scanner scanner;
    //all the painting has been implemented inside this method
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //reset pen and fill colors to defaults
        Shapes.fillColor=null;
        Shapes.penColor=Color.black;
        Gui.model.clear();//clear menu items
        Gui.model.addAll(Arrays.asList(Gui.tempVEC.split("\n")));//update menu items from the latest temp
        String drawingTool = null;//stores the shape to be drawn with
        if(Gui.selectedHistory<0){//if a history item is not selected
            scanner = new Scanner(Gui.tempVEC);//read from the main temp
        }else {
            scanner = new Scanner(Gui.historyTempVEC);//read from the history temp
        }
        //read VEC instruction and draw to screen
        while (scanner.hasNext()) {
            if (scanner.hasNext())drawingTool = scanner.next();
            if(!Shapes.readyToDraw){
                Shapes.pressedX = -3;
            }
            switch (drawingTool) {
                case "PLOT":
                    if (scanner.hasNextFloat()) {
                        Shapes.plot(intCanvas(scanner.next()), intCanvas(scanner.next()),g);
                        //System.out.print(Gui.tempVEC);
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
    //calculates correct coordinates for the VEC format based on the canvas size
    private static int intCanvas(String s){
        return (int)(Float.valueOf(s)*Gui.canvSize);
    }
}