package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.Color.black;

/**
 *
 * @authors Group_010 - Daniel Baharvand, James Dick, Jai Hunt, Jovi Lee
 * @version 1.1
 */
public class Gui extends JFrame implements ActionListener, Runnable {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public final double WIDTH = screenSize.getWidth();
    public final double HEIGHT = screenSize.getHeight();

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
        setSize((int)(WIDTH * 0.85), (int)(HEIGHT * 0.85));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        //Make the color chooser
            //Make the floating windows
        JInternalFrame colorWindow = new JInternalFrame("Color");
        colorWindow.setSize(600, 250);
        colorWindow.setLocation((int)WIDTH-1000, (int)HEIGHT-500);
        colorWindow.setVisible(true);
            //Make the color chooser and add it to the window
        JColorChooser colors= new JColorChooser(black);
        colors.setPreviewPanel(new JPanel());
        colorWindow.add(colors);
        add(colorWindow);
        //Make shapes controls
        JInternalFrame shapesWindow = new JInternalFrame("Shapes");
        shapesWindow.setSize(100, 500);
        shapesWindow.setLocation(0, 30);
        shapesWindow.setVisible(true);
        JPanel shapesPanel = new JPanel(new GridLayout(4, 1));
        JButton lineButton = new JButton("LINE" );
        shapesPanel.add(lineButton);
        JButton recButton = new JButton("RECTANGLE" );
        shapesPanel.add(recButton);
        JButton elButton = new JButton("ELLIPSE" );
        shapesPanel.add(elButton);
        JButton polButton = new JButton("POLYGON" );
        shapesPanel.add(polButton);
        shapesWindow.add(shapesPanel);
        add(shapesWindow);
        //Create a white canvas
        JPanel canvas = new JPanel();
        canvas.setSize(1000, 1000);
        canvas.setLocation(150, 50);
        canvas.setOpaque(true);
        canvas.setBackground(Color.WHITE);
        add(canvas);
        //Create menu
        JMenuBar cyanMenuBar = new JMenuBar();
        cyanMenuBar.setOpaque(true);
        cyanMenuBar.setBackground(Color.cyan);
        cyanMenuBar.setPreferredSize(new Dimension(200, 20));
        JMenu menuOpen = new JMenu("Open");
        JMenu menuSave = new JMenu("save");
        cyanMenuBar.add(menuOpen);
        cyanMenuBar.add(menuSave);
        setJMenuBar(cyanMenuBar);
        //History
        String subject[] = { "shapesPanel.add(elButton)", " shapesPanel.add(recButto",
                "hapesWindow.setSize(100, ", "   apesWindow.add(shapesPanel);", "   menuOpen = new JMenu(\"Open\" ",
                " orChooser colors= new JColorChooser(bla ",
                " Bar.setPreferredSize(new Dimen ", "Frame shapesWindow = new JInternalFram" };
        JInternalFrame historyWindow = new JInternalFrame("History");
        JList<String> list = new JList<String>(subject);
        JScrollPane scrollWindow = new JScrollPane(list);
        scrollWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //historyWindow.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        historyWindow.add(scrollWindow);
        historyWindow.setSize(300, 300);
        historyWindow.setLocation(1500, 0);
        historyWindow.setVisible(true);
        add(historyWindow);


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

