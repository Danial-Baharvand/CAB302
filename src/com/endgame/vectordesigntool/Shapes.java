package com.endgame.vectordesigntool;

import java.awt.*;
import java.util.ArrayList;

/**
 * The mouse actions and locations made onto the display canvas are saved here, to ensure that the corresponding
 * shapes are drawn where the user intended.
 *
 * @version 4.0
 */
class Shapes {
    private static int polCount = 0;
    private static int loadPolCount = 0;
    private static ArrayList<Integer> polX = new ArrayList<>(); //Saves the polygon x coordinate
    private static ArrayList<Integer> polY = new ArrayList<>(); //Saves the polygon y coordinate
    private static ArrayList<Integer> loadPolX = new ArrayList<>();
    private static ArrayList<Integer> loadPolY = new ArrayList<>();
    private static ArrayList<String> History = new ArrayList<>();
    private static boolean polComp = false;

    static Color penColor = Color.black; //Initial pen color is black
    static Color fillColor = null;
    static boolean readyToDraw = false;
    static int pressedX = -1; //
    static int pressedY = -1; //

    /**
     * Plots a simple dot at the x and y coordinates given by where the user clicks their mouse
     *
     * @param x axis location
     * @param y axis location
     * @param g paintComponent.GraphicsComponent
     */
    static void plot(int x,int y,Graphics g) {
        g.drawLine(x, y, x, y);
    }

    /**
     * Plots a line between the x and y coordinates given
     *
     * @param x axis location
     * @param y axis location
     * @param g paintComponent.GraphicsComponent
     */
    static void line(int x,int y,Graphics g) {
        if (pressedX < 0){

            pressedX = x;
            pressedY = y;
        } else {
            g.drawLine(pressedX, pressedY, x, y);
            pressedX = -1;
        }
    }

    /**
     * Creates a rectangle
     *
     * @param x axis location
     * @param y axis location
     * @param g paintComponent.GraphicsComponent
     */
    static void rect(int x,int y, Graphics g) {
        if (pressedX < 0){
            pressedX = x;
            pressedY = y;
        } else {
            if(x < pressedX) x = x ^ pressedX ^ (pressedX = x);
            if(y < pressedY) y = y ^ pressedY ^ (pressedY = y);
            if(fillColor !=null){
                g.setColor(fillColor);
                g.fillRect(pressedX, pressedY, x-pressedX, y-pressedY);
                g.setColor(penColor);
            }
            g.drawRect(pressedX, pressedY, x-pressedX, y-pressedY);
            pressedX = -1;
        }
    }

    /**
     * Creates an ellipse
     *
     * @param x axis location
     * @param y axis location
     * @param g paintComponent.GraphicsComponent
     */
    static void ellipse(int x, int y, Graphics g) {
        if (pressedX < 0){
            pressedX = x;
            pressedY = y;
        } else {
            if(x < pressedX) x = x ^ pressedX ^ (pressedX = x);
            if(y < pressedY) y = y ^ pressedY ^ (pressedY = y);
            if(fillColor !=null){
                g.setColor(fillColor);
                g.fillOval(pressedX, pressedY, x-pressedX, y-pressedY);
                g.setColor(penColor);
            }
            g.drawOval(pressedX, pressedY, x-pressedX, y-pressedY);
            pressedX = -1;
        }
    }

    /**
     * Stores instructions for the polygon in the tempVEC string variable.
     *
     * @param g paintComponent.GraphicsComponent
     */
    static void polygon(Graphics g) {
        if (loadPolX.size()>1){
            readyToDraw = true;
            if(fillColor != null){
                g.setColor(fillColor);
                g.fillPolygon(loadPolX.stream().mapToInt(Integer::intValue).toArray(),loadPolY.stream()
                        .mapToInt(Integer::intValue).toArray(),loadPolCount);
                g.setColor(penColor);
            }
            g.drawPolygon(loadPolX.stream().mapToInt(Integer::intValue).toArray(),loadPolY.stream()
                    .mapToInt(Integer::intValue).toArray(),loadPolCount);
            loadPolX.clear();
            loadPolY.clear();
            loadPolCount = 0;
        }
    }

    /**
     * Reads polygon instructions from the tempVec string variable and adds the drawing to the graphic
     *
     */
    static void polygon() {
        if (polCount>1){
            History.add("POLYGON");
            for(int i = 0; i < polCount; i++){
                History.add(String.valueOf((float)polX.get(i)/Gui.canvSize));
                History.add(String.valueOf((float)polY.get(i)/Gui.canvSize));
            }
            History.add("\n");
            addHisTOTemp();
            readyToDraw = true;
            System.out.println(History);
            History.clear();
            polX.clear();
            polY.clear();
            polCount = 0;
            polComp=false;
        }
    }

    /**
     * Adds the coordinates of the corresponding points in the polygon into an arrayList to be translated into a VEC
     * format
     *
     * @param x axis location
     * @param y axis location
     */
    static void polAdd(int x, int y){
        System.out.print(x+" "+ y+"\n");
        readyToDraw = false;
        polX.add(x);
        polY.add(y);
        polCount++;
    }

    /**
     * Loads the coordinates of the polygon points from the arrayList and then allocates them to be drawn
     *
     * @param x axis location
     * @param y axis location
     */
    static void polAddLoad(int x, int y){
        System.out.print(x+" "+ y+"\n");
        loadPolX.add(x);
        loadPolY.add(y);
        loadPolCount++;
        readyToDraw=true;
    }

    /**
     * Adds history to the tempVEC file
     *
     */
    static void addHisTOTemp(){
        Gui.tempVEC= Gui.tempVEC +History.toString().replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replaceFirst(".$","");
        System.out.print(Gui.tempVEC);
    }

    /**
     * Adds shapes to the history array and the tempVEC file
     *
     * @param x axis location
     * @param y axis location
     * @param shapeName
     */
    static void addToHisAndTemp ( int x, int y, String shapeName){
        History.add(shapeName);
        if (shapeName != "PLOT") {
            History.add(String.valueOf((float) pressedX / Gui.canvSize));
            History.add(String.valueOf((float) pressedY / Gui.canvSize));
        }
        History.add(String.valueOf((float)x/Gui.canvSize));
        History.add(String.valueOf((float)y/Gui.canvSize));
        History.add("\n");
        addHisTOTemp();
        History.clear();
    }

    /**
     * Saves the shape to the
     *
     * @param x axis location
     * @param y axis location
     * @param shapeName
     */
    static void saveShape(int x,int y, String shapeName) {
        if(shapeName == "PLOT") {
            addToHisAndTemp(x, y, shapeName);readyToDraw=true;
        } else if (pressedX < 0){
            pressedX = x;
            pressedY = y;
            readyToDraw = false;
        } else {
            addToHisAndTemp(x, y, shapeName);
            readyToDraw=true;
            pressedX = -1;
        }
    }


}