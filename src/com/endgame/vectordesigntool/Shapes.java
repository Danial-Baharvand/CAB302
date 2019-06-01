package com.endgame.vectordesigntool;

import java.awt.*;
import java.util.ArrayList;

/**
 * Reads and then stores the coordinates of the users mouse clicks to a temp file.
 *
 * @authors Group_010
 * @version 3.9 (Javadocs + Exceptions + Unittesting)
 */
class Shapes {
    static int polCount = 0;//number of polygon points for saving to temp
    private static int loadPolCount = 0;//number of polygon points for painting
    //initialise two arrayLists to store location of polygon points' coordinates to be saved to temp
    static ArrayList<Integer> polX = new ArrayList<>();
    static ArrayList<Integer> polY = new ArrayList<>();
    //initialise two arrayLists to store location of polygon points' coordinates to be painted
    private static ArrayList<Integer> loadPolX = new ArrayList<>();
    private static ArrayList<Integer> loadPolY = new ArrayList<>();
    // set default color and fill
    static Color penColor = Color.black;
    static Color fillColor=null;
    static boolean readyToDraw=false;//dictates when the graphics can be updated
    static int pressedX = -1;//stores the first x coordinate for the two point shapes, set to -1 before it's recorded
    static int pressedY = -1;//stores the first y coordinate for the two point shapes, set to -1 before it's recorded
    private static ArrayList<String> History = new ArrayList<String>();// instructions are constructed in History

    /**
     * draws a dot on canvas
     *
     * @param x coordinate
     * @param y coordinate
     * @param g paintComponent.Graphics
     */
    static void plot(int x,int y,Graphics g) {
        g.drawLine(x, y, x, y);

    }

    /**
     * draws a line on canvas
     *
     * @param x coordinate
     * @param y coordinate
     * @param g paintComponent.Graphics
     */
    static void line(int x,int y,Graphics g) {
        if (pressedX < 0){//if first point
            //store the first point coordinates
            pressedX = x;
            pressedY = y;
        } else {
            g.drawLine(pressedX, pressedY, x, y);//draw the line
            pressedX = -1;//reset
        }
    }

    /**
     * draws a rectangle on canvas
     *
     * @param x coordinate
     * @param y coordinate
     * @param g paintComponent.Graphics
     */
    static void rect(int x,int y, Graphics g) {
        if (pressedX < 0){//if first point
            //store the first point
            pressedX = x;
            pressedY = y;
        } else {
            //swap points' places if second point is above or at the left of the first point
            if(x < pressedX) x = x ^ pressedX ^ (pressedX = x);
            if(y < pressedY) y = y ^ pressedY ^ (pressedY = y);
            if(fillColor !=null){// check fill
                g.setColor(fillColor);//set fill color
                g.fillRect(pressedX, pressedY, x-pressedX, y-pressedY);// draw the fill rectangle
                g.setColor(penColor);//change to draw color to pen color
            }
            g.drawRect(pressedX, pressedY, x-pressedX, y-pressedY);//draw the rectangle
            pressedX = -1;//reset
        }
    }

    /**
     * draws an ellipse on canvas
     *
     * @param x coordinate
     * @param y coordinate
     * @param g paintComponent.Graphics
     */
    static void ellipse(int x, int y, Graphics g) {
        if (pressedX < 0){//if first point
            //store the first point
            pressedX = x;
            pressedY = y;
        } else {
            //swap points' places if second point is above or at the left of the first point
            if(x < pressedX) x = x ^ pressedX ^ (pressedX = x);
            if(y < pressedY) y = y ^ pressedY ^ (pressedY = y);
            if(fillColor !=null){// check fill
                g.setColor(fillColor);//set fill color
                g.fillOval(pressedX, pressedY, x-pressedX, y-pressedY);// draw the fill ellipse
                g.setColor(penColor);//change to draw color to pen color
            }
            g.drawOval(pressedX, pressedY, x-pressedX, y-pressedY);//draw the ellipse
            pressedX = -1;//reset
        }
    }

    /**
     * draws a polygon on the canvas
     *
     * @param g paintComponent.Graphics
     */
    static void polygon(Graphics g) {
        if (loadPolCount>2){// if there are at least 2 points in the polygon
            readyToDraw=true;//enable drawing
            if(fillColor !=null){// check fill
                g.setColor(fillColor);//set fill color
                g.fillPolygon(loadPolX.stream().mapToInt(Integer::intValue).toArray(),loadPolY.stream()
                        .mapToInt(Integer::intValue).toArray(),loadPolCount);// draw the fill polygon
                g.setColor(penColor);//change to draw color to pen color
            }
            g.drawPolygon(loadPolX.stream().mapToInt(Integer::intValue).toArray(),loadPolY.stream()
                    .mapToInt(Integer::intValue).toArray(),loadPolCount);//draw the polygon
            //reset
            loadPolX.clear();
            loadPolY.clear();
            loadPolCount = 0;
        }
    }

    /**
     * store polygon instructions to temp
     *
     */
    static void polygon() {
        if (polCount>2){// if there are at least 2 points in the polygon
            History.add("POLYGON");
            //add coordinates for each point
            for(int i = 0; i < polCount; i++){
                History.add(String.valueOf((float)polX.get(i)/Gui.canvSize));
                History.add(String.valueOf((float)polY.get(i)/Gui.canvSize));
            }
            History.add("\n");
            addHisTOTemp();//add the polygon to temp
            readyToDraw=true;//enable drawing
            //reset
            History.clear();
            polX.clear();
            polY.clear();
            polCount = 0;
        }
    }

    /**
     * add a point to the polygon for drawing
     *
     * @param x takes input y coordinate of the mouse click
     * @param y takes input y coordinate of the mouse click
     */
    static void polAdd(int x, int y){
        readyToDraw=false;
        polX.add(x);
        polY.add(y);
        polCount++;
    }

    /**
     * add a point to polygon for adding to temp
     *
     * @param x takes input y coordinate of the mouse click
     * @param y takes input y coordinate of the mouse click
     */
    static void polAddLoad(int x, int y){
        loadPolX.add(x);
        loadPolY.add(y);
        loadPolCount++;
        readyToDraw=true;
    }

    /**
     * (helper class) adds contents of the secondary temp to the main temp and properly formats it
     */
    private static void addHisTOTemp(){
        Gui.tempVEC= Gui.tempVEC +History.toString().replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .replaceFirst(".$","");
    }

    /**
     * (helper class) adds the shape vec instructions to History (secondary temp)
     *
     * @param x coordinate
     * @param y coordinate
     * @param shapeName
     */
    private static void addToHisAndTemp(int x, int y, String shapeName){
        History.add(shapeName);
        if (!shapeName.equals("PLOT")) {
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
     * encodes the shapes coordinates in vec format and adds it to temp
     *
     * @param x coordinate
     * @param y coordinate
     * @param shapeName the name of the shape
     */
    static void saveShape(int x,int y, String shapeName) {
        if(shapeName.equals("PLOT")) {
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