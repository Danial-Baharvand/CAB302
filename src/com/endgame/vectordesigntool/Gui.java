package com.endgame.vectordesigntool;

import javax.swing.*;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

    public void createGUI(){
        setSize((int)(WIDTH * 0.85), (int)(HEIGHT * 0.85));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Make the color chooser
            //Make the floating windows
        JDesktopPane background = new JDesktopPane();
        background.setBackground(Color.BLACK);

        JInternalFrame colorWindow = new JInternalFrame("Color");
        colorWindow.setSize(600, 250);
        colorWindow.setLocation((int)(WIDTH * 0.85)-620, (int)(HEIGHT*.85)-315);
        colorWindow.setVisible(true);
            //Make the color chooser and add it to the window
        JColorChooser colors= new JColorChooser(black);
        colors.setPreviewPanel(new JPanel());
        colorWindow.add(colors);
        background.add(colorWindow);
        getContentPane().add(background);

        setVisible(true);
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
        background.add(shapesWindow);
        //Create a white canvas
        JPanel canvas = new JPanel();
        canvas.setSize(1000, 1000);
        canvas.setLocation(150, 50);
        canvas.setOpaque(true);
        canvas.setBackground(Color.WHITE);
        background.add(canvas);
        //Create menu
        JMenuBar cyanMenuBar = new JMenuBar();
        cyanMenuBar.setOpaque(true);
        cyanMenuBar.setBackground(Color.cyan);
        cyanMenuBar.setPreferredSize(new Dimension(200, 20));
        JMenu file = new JMenu("File");
        cyanMenuBar.add(file);
        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        JMenuItem load =new JMenuItem("Load");
        file.add(load);
        load.addActionListener(new loadAction());
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new exitAction());
        JMenu edit = new JMenu("Edit");
        cyanMenuBar.add(edit);
        JMenuItem undo = new JMenuItem("Undo");
        edit.add(undo);
        JMenu tools = new JMenu("Tools");
        cyanMenuBar.add(tools);
        JMenuItem shapes = new JMenuItem("Shapes");
        tools.add(shapes);
        JMenuItem toolColorChooser =new JMenuItem("Color Chooser");
        tools.add(toolColorChooser);
        JMenuItem history =new JMenuItem("history");
        tools.add(history);


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
        historyWindow.add(scrollWindow);
        historyWindow.setSize(300, 300);
        historyWindow.setLocation(1500, 0);
        historyWindow.setVisible(true);
        background.add(historyWindow);
        //exprienments

    }
    class exitAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }
    class loadAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            int returnValue = jfc.showOpenDialog(null);
            // int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath());
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class MenuLis implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            System.out.println(source);
        }
    }


    @Override
    public void run() {
        createGUI();
    }


    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Gui("Vector Design Tool")); //should display "file name" - Vector Design Tool
    }
}

