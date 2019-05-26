package com.endgame.vectordesigntool;

import java.awt.*;
import java.util.ArrayList;
// implement shapes
class Shapes {
    //pressedX is the first location, x is second
    static int polCount = 0;
    static int loadPolCount = 0;
    static int polLastX = -1;
    static int polLastY = -1;
    static ArrayList<Integer> polX = new ArrayList<Integer>();
    static ArrayList<Integer> polY = new ArrayList<Integer>();
    static ArrayList<Integer> loadPolX = new ArrayList<Integer>();
    static ArrayList<Integer> loadPolY = new ArrayList<Integer>();
    private static Graphics canvasG = Gui.canvas.getGraphics();

    static boolean readyToDraw=false;
    static boolean polComp=false;
    static int pressedX = -1;
    static int pressedY = -1;
    static ArrayList<String> History = new ArrayList<String>();

    static void plot(int x,int y,Graphics g) {
        g.drawLine(x, y, x, y);

    }

    static void line(int x,int y,Graphics g) {
        if (pressedX < 0){
            pressedX = x;
            pressedY = y;
        } else {
            g.drawLine(pressedX, pressedY, x, y);
            pressedX = -1;
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
            pressedX = -1;
        }
    }

    static void ellipse(int x, int y, Graphics g) {
        if (pressedX < 0){
            pressedX = x;
            pressedY = y;
        } else {
            if(x < pressedX) x = x ^ pressedX ^ (pressedX = x);
            if(y < pressedY) y = y ^ pressedY ^ (pressedY = y);
            g.drawOval(pressedX, pressedY, x-pressedX, y-pressedY);
            pressedX = -1;
        }
    }

    static void polygon(Graphics g) {
        if (loadPolX.size()>1){
            readyToDraw=true;
            g.drawPolygon(loadPolX.stream().mapToInt(Integer::intValue).toArray(),loadPolY.stream().mapToInt(Integer::intValue).toArray(),loadPolCount);
            loadPolX.clear();
            loadPolY.clear();
            loadPolCount = 0;
        }
    }
    static void polygon() {
        if (polCount>1){
            History.add("POLYGON");
            for(int i = 0; i < polCount; i++){
                History.add(String.valueOf((float)polX.get(i)/Gui.canvSize));
                History.add(String.valueOf((float)polY.get(i)/Gui.canvSize));
            }
            History.add("\n");
            addHisTOTemp();
            readyToDraw=true;
            System.out.println(History);
            History.clear();
            polX.clear();
            polY.clear();
            polCount = 0;
            polComp=false;
        }
    }
    static void polAdd(int x, int y){
        System.out.print(x+" "+ y+"\n");
        readyToDraw=false;
        polX.add(x);
        polY.add(y);
        polCount++;
    }
    static void polAddLoad(int x, int y){
        System.out.print(x+" "+ y+"\n");
        loadPolX.add(x);
        loadPolY.add(y);
        loadPolCount++;
        readyToDraw=true;
    }

    static void addHisTOTemp(){
        Gui.tempVEC= Gui.tempVEC +History.toString().replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replaceFirst(".$","");
        System.out.print(Gui.tempVEC);
    }
    static void addToHisAndTemp ( int x, int y, String shapeName){
        History.add(shapeName);
        if (shapeName !="PLOT") {
            History.add(String.valueOf((float) pressedX / Gui.canvSize));
            History.add(String.valueOf((float) pressedY / Gui.canvSize));
        }
        History.add(String.valueOf((float)x/Gui.canvSize));
        History.add(String.valueOf((float)y/Gui.canvSize));
        History.add("\n");
        addHisTOTemp();
        History.clear();
    }
    static void saveShape(int x,int y, String shapeName) {
        if(shapeName=="PLOT") {
            addToHisAndTemp (x, y, shapeName);readyToDraw=true;
        } else if (pressedX < 0){
            pressedX = x;
            pressedY = y;
            readyToDraw=false;
        } else {
            addToHisAndTemp (x, y, shapeName);
            readyToDraw=true;
            pressedX = -1;
        }
    }


}