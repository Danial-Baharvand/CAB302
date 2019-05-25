package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class MyPanel extends JPanel {
    //Scanner scanner=new Scanner(Gui.tempVEC);
    //Scanner scanner=new Scanner("PLOT 0.539 0.404 \n" +
      //      "PLOT 0.57 0.47 \n" +
       //     "PLOT 0.57 0.47");
    Scanner scanner;
    static String tempVEC;
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        String drawingTool = null;
        // pass the path to the file as a parameter
        //File file = new File("example1.vec");


        scanner = new Scanner(Gui.tempVEC);

        //String sc=Gui.tempVEC;


        while (scanner.hasNext()) {
            if (scanner.hasNext())drawingTool = scanner.next();

            switch (drawingTool) {
                case "PLOT":
                    if (scanner.hasNextFloat()) {
                        Shapes.plot(intCanvas(scanner.next()), intCanvas(scanner.next()),g);
                        System.out.print(Gui.tempVEC);
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
                        Shapes.polygon(intCanvas(scanner.next()), intCanvas(scanner.next()),g);
                    }
                    Shapes.polygon(Shapes.pressedX, Shapes.pressedY,g);
                    break;

            }
        }
    }
    private static int intCanvas(String s){
        return (int)(Float.valueOf(s)*Gui.canvSize);
    }
}