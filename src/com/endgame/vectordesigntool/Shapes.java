package com.endgame.vectordesigntool;

import java.awt.*;
import java.util.ArrayList;
// implement shapes
class Shapes {
    //pressedX is the first location, x is second
    private static int polCount = 1;
    private static int polLastX = -1;
    private static int polLastY = -1;
    private static ArrayList<Integer> polX = new ArrayList<Integer>();
    private static ArrayList<Integer> polY = new ArrayList<Integer>();
    private static Graphics canvasG = Gui.canvas.getGraphics();

    static int pressedX = -1;
    static int pressedY = -1;
    static ArrayList<String> History = new ArrayList<String>();

    static void plot(int x,int y,Graphics g) {
        g.drawLine(x, y, x, y);

    }
    static void plot(int x,int y) {
     //plot(x, y, canvasG);
        History.add("PLOT");
        History.add(String.valueOf((float)x/Gui.canvSize));
        History.add(String.valueOf((float)y/Gui.canvSize));
        History.add("\n");
        addHisTOTemp();

       //System.out.print(Gui.tempVEC);
        History.clear();

    }

    static void line(int x,int y,Graphics g) {
        if (pressedX < 0 && pressedY<0){
            pressedX = x;
            pressedY = y;
        } else {
            g.drawLine(pressedX, pressedY, x, y);
            System.out.print(Gui.tempVEC);
            //System.out.println(History);
            History.clear();
            pressedX = -1;
            pressedY=-1;
        }
    }
    static void line(int x,int y) {
        if (pressedX < 0 && pressedY<0){
            pressedX = x;
            pressedY = y;
        } else {
            History.add("LINE");
            History.add(String.valueOf((float)pressedX/Gui.canvSize));
            History.add(String.valueOf((float)pressedY/Gui.canvSize));
            History.add(String.valueOf((float)x/Gui.canvSize));
            History.add(String.valueOf((float)y/Gui.canvSize));
            History.add("\n");
            addHisTOTemp();
            System.out.print(Gui.tempVEC);
            //System.out.println(History);
            History.clear();
            pressedX = -1;
            pressedY=-1;
        }

    }

    static void rect(int x,int y, Graphics g) {
        if (pressedX < 0){
            pressedX = x;
            pressedY = y;
        } else {
            if(x < pressedX) x = x ^ pressedX ^ (pressedX = x);
            if(y < pressedY) y = y ^ pressedY ^ (pressedY = y);
            g.drawRect(pressedX, pressedY, x-pressedX, y-pressedY);
            History.add("RECTANGLE");
            History.add(String.valueOf((float)pressedX/Gui.canvSize));
            History.add(String.valueOf((float)pressedY/Gui.canvSize));
            History.add(String.valueOf((float)x/Gui.canvSize));
            History.add(String.valueOf((float)y/Gui.canvSize));
            History.add("\n");
            addHisTOTemp();
            History.clear();
            pressedX = -1;
        }
    }
    static void rect(int x,int y) {
        rect(x, y, canvasG);
    }

    static void ellipse(int x, int y, Graphics g) {
        if (pressedX < 0){
            pressedX = x;
            pressedY = y;
        } else {
            if(x < pressedX) x = x ^ pressedX ^ (pressedX = x);
            if(y < pressedY) y = y ^ pressedY ^ (pressedY = y);
            g.drawOval(pressedX, pressedY, x-pressedX, y-pressedY);
            History.add("ELLIPSE");
            History.add(String.valueOf((float)pressedX/Gui.canvSize));
            History.add(String.valueOf((float)pressedY/Gui.canvSize));
            History.add(String.valueOf((float)x/Gui.canvSize));
            History.add(String.valueOf((float)y/Gui.canvSize));
            History.add("\n");
            addHisTOTemp();
            History.clear();
            pressedX = -1;
        }
    }
    static void ellipse(int x, int y) {
        ellipse(x, y, canvasG);
    }
    static void polygon(int x,int y,Graphics g) {
        if (pressedX < 0){
            pressedX = x;
            pressedY = y;
            polLastX = x;
            polLastY = y;
            polX.add(x);
            polY.add(y);
        } else if (x == pressedX && y == pressedY){
            g.drawPolygon(polX.stream().mapToInt(Integer::intValue).toArray(),polY.stream().mapToInt(Integer::intValue).toArray(),polCount);
            History.add("POLYGON");
            for(int i = 0; i < polCount; i++){
                History.add(String.valueOf((float)polX.get(i)/Gui.canvSize));
                History.add(String.valueOf((float)polY.get(i)/Gui.canvSize));
            }
            History.add("\n");
            addHisTOTemp();
            System.out.println(History);
            History.clear();
            polX.clear();
            polY.clear();
            pressedX = -1;
            polCount = 1;
        } else {
            g.drawLine(polLastX, polLastY, x, y);
            polX.add(x);
            polY.add(y);
            polLastX = x;
            polLastY = y;
            polCount++;
        }
    }
    static void polygon(int x,int y) {
        polygon(x, y, canvasG);
    }
    static void addHisTOTemp(){
        Gui.tempVEC+=History.toString().replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replaceFirst(".$","");
    }
}