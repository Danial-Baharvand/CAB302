package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @authors Group_010 - Daniel Baharvand, James Dick, Jai Hunt, Jovi Lee
 * @version 1.0
 */
public class Gui extends JFrame implements ActionListener, Runnable {
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 700;

    /**
     *
     * @param title
     * @throws HeadlessException when code that is dependent on keyboard, display or mouse is called in environment
     * that does not support any of these things.
     */
    public Gui(String title) throws HeadlessException{
        super(title);
    }

    private void createGUI(){
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {
        createGUI();
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Gui("Vector Design Tool")); //should display "file name" - Vector Design Tool
    }
}

