package com.endgame.vectordesigntool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Implements Load
 class Load {
    static void load(String selectedFile) {
        String drawingTool = null;
        // pass the path to the file as a parameter
        File file = new File(selectedFile);
         Scanner sc = null;
         try {
             sc = new Scanner(file);
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }


        while (sc.hasNextLine()) {
                drawingTool = sc.next();
            switch (drawingTool) {
                case "PLOT":
                    if (sc.hasNext()) {
                        Shapes.saveShape(intCanvas(sc.next()), intCanvas(sc.next()),"PLOT");
                    }
                case "LINE":
                    for (int i = 0; i < 2; i++) {
                        if (sc.hasNext()) {
                            Shapes.saveShape(intCanvas(sc.next()), intCanvas(sc.next()),"LINE");
                        }
                    }
                    break;
                case "RECTANGLE":
                    for (int i = 0; i < 2; i++) {
                        if (sc.hasNext()) {
                            Shapes.saveShape(intCanvas(sc.next()), intCanvas(sc.next()),"RECTANGLE");
                        }
                    }
                    break;
                case "ELLIPSE":
                    for (int i = 0; i < 2; i++) {
                        if (sc.hasNext()) {
                            Shapes.saveShape(intCanvas(sc.next()), intCanvas(sc.next()),"ELLIPSE");
                        }
                    }
                    break;
                case "POLYGON":
                    while (sc.hasNextFloat()) {
                        //Shapes.polygon(intCanvas(sc.next()), intCanvas(sc.next()));
                    }
                    //Shapes.polygon(Shapes.pressedX, Shapes.pressedY);
                    break;

            }
        }
    }
    private static int intCanvas(String s){
        return (int)(Float.valueOf(s)*Gui.canvSize);
    }

}
