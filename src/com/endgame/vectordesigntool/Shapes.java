package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Shapes {
    static int pressedX=-1;
    static int pressedY=-1;
    static ArrayList<Integer> polX = new ArrayList<Integer>();
    static ArrayList<Integer> polY = new ArrayList<Integer>();
    static int polCount=1;
    static int polLastX=-1;
    static int polLastY=-1;
    static Graphics g = Gui.canvas.getGraphics();
    //static Graphics g= Gui.canvas.paintComponents();
    static void plot(int x,int y) {
        g.drawLine(x,y,x,y);
    }
    static void line(int x,int y) {
        if (pressedX < 0){pressedX=x;pressedY=y;}
        else {
            g.drawLine(pressedX, pressedY, x, y);
            pressedX=-1;
        }
    }
    static void rect(int x,int y) {
        if (pressedX < 0){pressedX=x;pressedY=y;}
        else {
                if(x<pressedX){x = x ^ pressedX ^ (pressedX = x);}
                if(y<pressedY){y = y ^ pressedY ^ (pressedY = y);}
                g.drawRect(pressedX, pressedY, x-pressedX, y-pressedY);
                pressedX=-1;
        }
    }
    static void ellipse(int x,int y) {
        if (pressedX < 0){pressedX=x;pressedY=y;}
        else {
                if(x<pressedX){x = x ^ pressedX ^ (pressedX = x);}
                if(y<pressedY){y = y ^ pressedY ^ (pressedY = y);}
                g.drawOval(pressedX, pressedY, x-pressedX, y-pressedY);
                pressedX=-1;
        }
    }
    static void polygon(int x,int y) {
        if (pressedX < 0){
            pressedX=x;
            pressedY=y;
            polLastX=x;
            polLastY=y;
            polX.add(x);polY.add(y);
        }
        else if (x==pressedX &&y==pressedY){
            g.drawPolygon(polX.stream().mapToInt(Integer::intValue).toArray(),polY.stream().mapToInt(Integer::intValue).toArray(),polCount);
            polX.clear();
            polY.clear();
            pressedX=-1;
            polCount=1;
        }else{
            g.drawLine(polLastX, polLastY, x, y);
            polX.add(x);
            polY.add(y);
            polLastX=x;
            polLastY=y;
            polCount++;
        }
    }
}