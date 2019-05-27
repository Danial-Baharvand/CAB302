package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class MyPanel extends JPanel {
    Scanner scanner;
    static String tempVEC;
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        String drawingTool = null;
        // pass the path to the file as a parameter
        //File file = new File("example1.vec");
        scanner = new Scanner(Gui.tempVEC);

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
                    //if(Shapes.polX.size()>0) Shapes.polygon(Shapes.polX.get(0), Shapes.polY.get(0),g);
                    if(Shapes.readyToDraw) Shapes.polygon(g);

                    System.out.print(Shapes.polX.toString());
                    System.out.print(Shapes.polY.toString());
                    break;
            }
        }
    }
    private static int intCanvas(String s){
        return (int)(Float.valueOf(s)*Gui.canvSize);
    }
}