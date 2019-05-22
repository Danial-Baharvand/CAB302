package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *
 * @authors Group_010 - Daniel Baharvand, James Dick, Jai Hunt, Jovi Lee
 * @version 1.0
 */
public class Gui extends JFrame implements ActionListener, Runnable {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public final double WIDTH = screenSize.getWidth();
    public final double HEIGHT = screenSize.getHeight();

    private JPanel pnlDisplay;
    private JPanel pnlMenu;

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
        setSize((int)(WIDTH * 0.75), (int)(HEIGHT * 0.7));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        pnlDisplay = createPanel(Color.WHITE);
        pnlMenu = createPanel(Color.LIGHT_GRAY);
        //menuBar.add(fileMenu);
        getContentPane().add(pnlDisplay,BorderLayout.CENTER);
        getContentPane().add(pnlMenu,BorderLayout.NORTH);
        setVisible(true);
    }

    private JPanel createPanel(Color c){
        JPanel panel = new JPanel();
        panel.setBackground(c);
        return panel;
    }

    private void addToPanel(JPanel jp,Component c, GridBagConstraints constraints,int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
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

