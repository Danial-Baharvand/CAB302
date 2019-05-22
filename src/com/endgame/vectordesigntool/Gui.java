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
 * @version 1.3
 */
public class Gui extends JFrame implements ActionListener, Runnable {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public final double WIDTH = screenSize.getWidth();
    public final double HEIGHT = screenSize.getHeight();
    public final double widthProp = 0.8;
    public final double heightProp = 0.7;

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
        setSize((int)(WIDTH * widthProp), (int)(HEIGHT * heightProp));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenu());
        getContentPane().add(display());
        setVisible(true);
    }

    private JDesktopPane display(){
        JDesktopPane bg = new JDesktopPane();
        bg.setBackground(Color.BLACK);
        bg.add(createColorWindow());
        bg.add(canvas());
        bg.add(createShapes());
        bg.add(createHistoryWindow());
        return bg;
    }

    private JPanel canvas(){
        //Create a white canvas
        JPanel can = new JPanel();
        can.setSize(1000, 1000);
        can.setLocation(150, 50);
        can.setOpaque(true);
        can.setBackground(Color.WHITE);
        return can;
    }

    private JMenuBar createMenu(){
        //Menu Bar
        JMenuBar bar = new JMenuBar();
        //Menu
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu tools = new JMenu("Tools");
        //Menu item
        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem shapes = new JMenuItem("Shapes");
        JMenuItem toolColorChooser = new JMenuItem("Color Chooser");
        JMenuItem history = new JMenuItem("history");
        //Setting menu parameters
        bar.setBackground(Color.cyan);
        bar.setPreferredSize(new Dimension(200, 20));
        //Adding menu functions
        bar.add(file);
        file.add(save);
        file.add(load);
        file.add(exit);
        bar.add(edit);
        edit.add(undo);
        bar.add(tools);
        tools.add(shapes);
        tools.add(toolColorChooser);
        tools.add(history);
        load.addActionListener(new loadAction());
        exit.addActionListener(new exitAction());
        return bar;
    }

    private JInternalFrame createShapes(){
        //Frame
        JInternalFrame shapesWindow = new JInternalFrame("Shapes");
        //Panel
        JPanel shapesPanel = new JPanel(new GridLayout(4, 1));
        //Buttons
        JButton lineButton = new JButton("LINE" );
        JButton recButton = new JButton("RECTANGLE" );
        JButton elButton = new JButton("ELLIPSE" );
        JButton polButton = new JButton("POLYGON" );
        //Setting shape parameters in window
        shapesWindow.setSize(100, 500);
        shapesWindow.setLocation(0, 30);
        //Adding shape panels to window
        shapesPanel.add(lineButton);
        shapesPanel.add(recButton);
        shapesPanel.add(elButton);
        shapesPanel.add(polButton);
        shapesWindow.add(shapesPanel);
        shapesWindow.setVisible(true);
        return shapesWindow;
    }

    private JInternalFrame createHistoryWindow() {
        //History
        String subject[] = { "shapesPanel.add(elButton)", " shapesPanel.add(recButto",
                "hapesWindow.setSize(100, ", "   apesWindow.add(shapesPanel);", "   menuOpen = new JMenu(\"Open\" ",
                " orChooser colors= new JColorChooser(bla ",
                " Bar.setPreferredSize(new Dimen ", "Frame shapesWindow = new JInternalFram" };

        JInternalFrame historyWindow = new JInternalFrame("History");
        JList<String> list = new JList<String>(subject);
        JScrollPane scrollWindow = new JScrollPane(list);
        //Setting parameters
        scrollWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        historyWindow.setSize(300, 300);
        historyWindow.setLocation(1500, 0);
        historyWindow.setVisible(true);
        //Adding scrolling window
        historyWindow.add(scrollWindow);
        return historyWindow;
    }

    private JInternalFrame createColorWindow(){
        JInternalFrame colorWindow = new JInternalFrame("Color");
        JColorChooser colors= new JColorChooser(black);
        colors.setPreviewPanel(new JPanel());
        //Setting window parameters
        colorWindow.setSize(600, 250);
        colorWindow.setLocation((int)(WIDTH * widthProp)-620, (int)(HEIGHT * heightProp)-315);
        colorWindow.setVisible(true);
        //Adding to window
        colorWindow.add(colors);
        return colorWindow;
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

