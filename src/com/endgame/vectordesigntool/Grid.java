package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;

public class Grid {
    private Color gridColor = Color.gray;

    static void gridLineX(int y, Graphics g) {
        g.drawLine(0, y, 1, y);
    }

    static void gridLineY(int x, Graphics g) {
        g.drawLine(x, 1, x, 0);
    }

    static void drawGrid(Graphics g) {
        gridLineX(Gui.yAxis, g);
        gridLineY(Gui.xAxis, g);
    }
}


