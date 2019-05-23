package com.endgame.vectordesigntool;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import static java.awt.Color.black;

/**
 *
 * @authors Group_010 - Daniel Baharvand, James Dick, Jai Hunt, Jovi Lee
 * @version 1.4
 */
public class Gui extends JFrame implements ActionListener, Runnable {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public double WIDTH = screenSize.getWidth();
    public double HEIGHT = screenSize.getHeight();
    public final double widthProp = 0.8;
    public final double heightProp = 0.7;
    JInternalFrame shapesWindow;
    JInternalFrame colorWindow;
    JInternalFrame historyWindow;
    public static JPanel canvas;
    enum Type {PLOT, LINE, RECTANGLE,ELLIPSE,POLYGON}
    Type selectedShape;
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
        addComponentListener(new ResizeListener());
        //Plot.plot();
        setVisible(true);
    }

    private JDesktopPane display(){
        JDesktopPane bg = new JDesktopPane();
        bg.setBackground(Color.BLACK);
        bg.add(createColorWindow());
        bg.add(makeCanvas());
        bg.add(createShapes());
        bg.add(createHistoryWindow());
        return bg;
    }


    public JPanel makeCanvas(){
        //Create a white canvas
        canvas = new JPanel();
        canvas.setSize(1000, 1000);
        canvas.setLocation(150, 50);
        canvas.setOpaque(true);
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(new canvasAction());
        return canvas;
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
        save.addActionListener((new saveAction()));
        load.addActionListener(new loadAction());
        exit.addActionListener(new exitAction());
        shapes.addActionListener(new shapesToggleAction());
        toolColorChooser.addActionListener(new colorToggleAction());
        history.addActionListener(new historyToggleAction());
        return bar;
    }

    private JInternalFrame createShapes(){
        //Frame
         shapesWindow = new JInternalFrame("Shapes");
        //Panel
        JPanel shapesPanel = new JPanel(new GridLayout(5, 1));
        //Buttons
        JButton plotButton = new JButton("PLOT" );
        JButton lineButton = new JButton("LINE" );
        JButton rectButton = new JButton("RECTANGLE" );
        JButton ellipseButton = new JButton("ELLIPSE" );
        JButton polygonButton = new JButton("POLYGON" );
        //Setting shape parameters in window
        shapesWindow.setSize(100, 500);
        shapesWindow.setLocation(0, 30);
        //Adding shape panels to window
        shapesPanel.add(plotButton);
        plotButton.addActionListener((new plotAction()));
        shapesPanel.add(lineButton);
        lineButton.addActionListener((new lineAction()));
        shapesPanel.add(rectButton);
        rectButton.addActionListener((new rectAction()));
        shapesPanel.add(ellipseButton);
        ellipseButton.addActionListener((new ellipseAction()));
        shapesPanel.add(polygonButton);
        polygonButton.addActionListener((new polygonAction()));
        shapesWindow.add(shapesPanel);
        shapesWindow.setVisible(true);
        return shapesWindow;
    }
    private JInternalFrame createColorWindow(){
        colorWindow = new JInternalFrame("Color");
        JColorChooser colors= new JColorChooser(black);
        colors.setPreviewPanel(new JPanel());
        //Setting window parameters
        colorWindow.setSize(600, 250);
        colorWindow.setVisible(true);
        //Adding to window
        colorWindow.add(colors);
        return colorWindow;
    }
    private JInternalFrame createHistoryWindow() {
        //History
        String subject[] = { "shapesPanel.add(elButton)", " shapesPanel.add(recButto",
                "hapesWindow.setSize(100, ", "   apesWindow.add(shapesPanel);", "   menuOpen = new JMenu(\"Open\" ",
                " orChooser colors= new JColorChooser(bla ",
                " Bar.setPreferredSize(new Dimen ", "Frame shapesWindow = new JInternalFram" };

        historyWindow = new JInternalFrame("History");
        JList<String> list = new JList<String>(subject);
        JScrollPane scrollWindow = new JScrollPane(list);
        //Setting parameters
        scrollWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        historyWindow.setSize(300, 300);
        System.out.println(getContentPane().getBounds().getSize().width);
        historyWindow.setVisible(true);
        //Adding scrolling window
        historyWindow.add(scrollWindow);
        return historyWindow;
    }



    class exitAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }
    class saveAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            int returnValue = jfc.showSaveDialog(null);
            // int returnValue = jfc.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath());
            }
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
    class shapesToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(shapesWindow.isVisible()){shapesWindow.setVisible(false);}
            else{shapesWindow.setVisible(true); shapesWindow.setLocation(0,30);}
        }
    }
    class colorToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(colorWindow.isVisible()){colorWindow.setVisible(false);}
            else {
                WIDTH = screenSize.getWidth();
                HEIGHT = screenSize.getHeight();
                colorWindow.setVisible(true);
                colorWindow.setLocation((int)(WIDTH * widthProp)-620, (int)(HEIGHT * heightProp)-315);}
        }
    }
    class historyToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(historyWindow.isVisible()){historyWindow.setVisible(false);}
            else{historyWindow.setVisible(true); historyWindow.setLocation((int)(WIDTH * widthProp)-620, (int)(HEIGHT * heightProp)-950);}
        }
    }
    class plotAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectedShape=Type.PLOT;
            Shapes.pressedX=-1;
            Shapes.pressedY=-1;
        }
    }
    class lineAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectedShape=Type.LINE;
            Shapes.pressedX=-1;
            Shapes.pressedY=-1;
        }
    }
    class rectAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectedShape=Type.RECTANGLE;
            Shapes.pressedX=-1;
            Shapes.pressedY=-1;
        }
    }
    class ellipseAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectedShape=Type.ELLIPSE;
            Shapes.pressedX=-1;
            Shapes.pressedY=-1;
        }
    }
    class polygonAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectedShape=Type.POLYGON;
            Shapes.pressedX=-1;
            Shapes.pressedY=-1;
        }
    }
    class canvasAction implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (selectedShape==Type.PLOT) {
                Shapes.plot(e.getX(), e.getY());
            }else if (selectedShape==Type.LINE) {
                Shapes.line(e.getX(), e.getY());
            }else if (selectedShape==Type.RECTANGLE) {
                Shapes.rect(e.getX(), e.getY());
            }else if (selectedShape==Type.ELLIPSE) {
                Shapes.ellipse(e.getX(), e.getY());
            }else if (selectedShape==Type.POLYGON) {
                Shapes.polygon(e.getX(), e.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

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
    class ResizeListener implements ComponentListener {

        public void componentHidden(ComponentEvent e) {}
        public void componentMoved(ComponentEvent e) {}
        public void componentShown(ComponentEvent e) {}

        public void componentResized(ComponentEvent e) {
            historyWindow.setLocation(getContentPane().getBounds().getSize().width-300,getContentPane().getBounds().getSize().height-900);
            colorWindow.setLocation(getContentPane().getBounds().getSize().width-600,getContentPane().getBounds().getSize().height-250);
            System.out.println(getContentPane().getBounds().getSize().width);
            System.out.println(getContentPane().getBounds().getSize().height);

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

