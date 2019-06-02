package com.endgame.vectordesigntool;

import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class UnitTests {
    MouseEvent me = new MouseEvent(Gui.canvas, 0,0,0,100,100,1,false);
                for(MouseListener ml: canvas.getMouseListeners()) {
        ml.mouseClicked(me);
    }

    @BeforeEach
    public void newTempFile() {

    }

    /* Drawing Tests */
    @Test
    public void drawPlotTest() throws AWTException {

    }
}
