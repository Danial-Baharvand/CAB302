package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class Grid extends JPanel{
    static Color gridColor = Color.gray;
    static Shape xLine;
    static Shape yLine;
    static Graphics2D g2;
    static Graphics2D g3;

    static void gridLineX(double y, Graphics g) {
        g2 = (Graphics2D) g;
        g2.setColor(gridColor);
        xLine = new Line2D.Double(0, y, 1, y);
    }

    static void gridLineY(double x, Graphics g) {
        g3 = (Graphics2D) g;
        g3.setColor(gridColor);
        yLine = new Line2D.Double(0, x, 1, x);
    }

    public void paintComponent(Graphics g) {
        g2.draw(xLine);
        g3.draw(yLine);
    }
}


