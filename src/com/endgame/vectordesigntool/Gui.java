package com.endgame.vectordesigntool;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @authors Group_010 - Daniel Baharvand, James Dick, Jai Hunt, Jovi Lee
 * @version 3.0
 */
public class Gui extends JFrame implements ActionListener, Runnable {

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double WIDTH = screenSize.getWidth();
    private double HEIGHT = screenSize.getHeight();
    private JButton polEndButton;
    private JButton gridBtn;
    private JInternalFrame shapesWindow;
    private JInternalFrame colorWindow;
    private JInternalFrame historyWindow;
    private JInternalFrame utilWindow;
    private JColorChooser colors;
    private Type selectBtn;

    static String tempVEC = "";
    protected static JPanel canvas;
    static final int canvSize = 1000;
    enum Type {PLOT, LINE, RECTANGLE, ELLIPSE, POLYGON, ZOOM, GRID}
    static Graphics canvasG;

    /**
     *
     * @param title
     * @throws HeadlessException when code that is dependent on keyboard, display or mouse is called in environment
     * that does not support any of these things.
     */
    private Gui(String title) throws HeadlessException{
        super(title);
    }

    private void createGUI(){
        double widthProp = 0.8;
        double heightProp = 0.8;
        setSize((int)(WIDTH * widthProp), (int)(HEIGHT * heightProp));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenu());
        getContentPane().add(display());
        addComponentListener(new ResizeListener());
        setVisible(true);
    }

    private JDesktopPane display(){
        JDesktopPane bg = new JDesktopPane();
        bg.setBackground(Color.BLACK);
        bg.add(createColorWindow());
        bg.add(makeCanvas());
        bg.add(createShapes());
        bg.add(createHistoryWindow());
        bg.add(createUtilWin());
        return bg;
    }


    private JPanel makeCanvas(){
        //Create a white canvas
        canvas = new MyPanel();
        canvas.setSize(canvSize, canvSize);
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
        undo.addActionListener(new undoAction());
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
        JPanel shapesPanel = new JPanel(new GridLayout(6, 1));
        //Buttons
        JButton plotButton = new JButton(new ImageIcon("plot.png") );
        JButton lineButton = new JButton(new ImageIcon(("line.png")) );
        JButton rectButton = new JButton(new ImageIcon("rectangle.png"));
        JButton ellipseButton = new JButton(new ImageIcon("ellipse.png") );
        JButton polygonButton = new JButton(new ImageIcon("polygon.png") );
        polEndButton = new JButton(new ImageIcon("finishedPolygon.png") );
        //Setting shape parameters in window
        shapesWindow.setSize(60, 300);
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
        shapesPanel.add(polEndButton);
        polEndButton.addActionListener((new polEndAction()));
        polEndButton.setEnabled(false);
        shapesWindow.add(shapesPanel);
        shapesWindow.setVisible(true);
        return shapesWindow;
    }

    private JInternalFrame createColorWindow(){
        colorWindow = new JInternalFrame("Color");
        colors= new JColorChooser(Color.BLACK);
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
        String[] subject = {"shapesPanel.add(elButton)", " shapesPanel.add(recButto",
                "hapesWindow.setSize(100, ", "   apesWindow.add(shapesPanel);", "   menuOpen = new JMenu(\"Open\" ",
                " orChooser colors= new JColorChooser(bla ",
                " Bar.setPreferredSize(new Dimen ", "Frame shapesWindow = new JInternalFram"};

        historyWindow = new JInternalFrame("History");
        JList<String> list = new JList<>(subject);
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

    private JInternalFrame createUtilWin(){
        //Separate utilities window for zoom and grid features
        utilWindow = new JInternalFrame("Utilities");
        //Panel
        JPanel utilPanel = new JPanel(new GridLayout(1, 2));
        //Buttons
        JButton zoomBtn = new JButton(new ImageIcon("magnifyingGlass.png"));
        gridBtn = new JButton(new ImageIcon("grid.png"));
        //Setting utils parameters in window
        utilWindow.setSize(100, 80);
        utilWindow.setLocation(0, 600);
        //Adding util to window
        utilPanel.add(zoomBtn);
        utilPanel.add(gridBtn);
        zoomBtn.addActionListener(new zoomAction());
        gridBtn.addActionListener(new gridAction());
        utilWindow.add(utilPanel);
        utilWindow.setVisible(true);
        return utilWindow;
    }

    private void gridWin(){
        //Popup window
        JFrame parent = new JFrame("Grid");
        //Panels
        JPanel xPanel = new JPanel(new BorderLayout()); //panel for x option
        JPanel yPanel = new JPanel(new BorderLayout()); //panel for y option
        //Button
        JButton enterBtn = new JButton("Enter");
        //Text boxes
        JTextField xTextField = new JTextField(10);
        JTextField yTextField = new JTextField(10);
        //Text labels
        JLabel xInput = new JLabel();
        JLabel yInput = new JLabel();
        //Setting parameters
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.Y_AXIS));
        yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.Y_AXIS));
        xInput.setText("Please input the x coordinate: ");
        yInput.setText("Please input the y coordinate: ");
        //Adds
        xPanel.add(xInput, BorderLayout.LINE_START);
        xPanel.add(xTextField, BorderLayout.LINE_END);
        yPanel.add(yInput, BorderLayout.LINE_START);
        yPanel.add(yTextField, BorderLayout.LINE_END);
        parent.add(xPanel, BorderLayout.PAGE_START);
        parent.add(yPanel, BorderLayout.CENTER);
        parent.add(enterBtn, BorderLayout.PAGE_END);
        //Display parameters
        parent.pack();
        parent.setVisible(true);
        parent.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class exitAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }

    class saveAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            String saveFilePath;
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath());
                if(!selectedFile.getAbsolutePath().toLowerCase().endsWith(".vec")){
                    saveFilePath=selectedFile.getAbsolutePath()+".vec";
                }else{
                    saveFilePath=selectedFile.getAbsolutePath();
                }
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(saveFilePath), StandardCharsets.US_ASCII))) {
                    writer.write(tempVEC);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class loadAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                System.out.println(selectedFile.getAbsolutePath());
                Gui.canvas.getGraphics().dispose();
                //Load.load(selectedFile.getAbsolutePath());
                try {
                    tempVEC=new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())), StandardCharsets.US_ASCII);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                canvas.repaint();
            }
        }
    }
    class undoAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
          //implement undo
        }
    }

    class shapesToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(shapesWindow.isVisible())shapesWindow.setVisible(false);
            else {
                shapesWindow.setVisible(true);
                shapesWindow.setLocation(0,30);
            }
        }
    }

    class colorToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(colorWindow.isVisible())colorWindow.setVisible(false);
            else {
                WIDTH = screenSize.getWidth();
                HEIGHT = screenSize.getHeight();
                colorWindow.setVisible(true);
                colorWindow.setLocation(getContentPane().getBounds().getSize().width-600,getContentPane().getBounds().getSize().height-250);
            }
        }
    }

    class historyToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(historyWindow.isVisible()){
                historyWindow.setLocation(getContentPane().getBounds().getSize().width-300,50);
                historyWindow.setVisible(false);
            } else historyWindow.setVisible(true);
        }
    }
    class plotAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.PLOT;
            Shapes.pressedX = -1;
            Shapes.pressedY = -1;

        }
    }
    class lineAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.LINE;
            Shapes.pressedX = -1;
            Shapes.pressedY = -1;
        }
    }
    class rectAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.RECTANGLE;
            Shapes.pressedX = -1;
            Shapes.pressedY = -1;
        }
    }
    class ellipseAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.ELLIPSE;
            Shapes.pressedX = -1;
            Shapes.pressedY = -1;
        }
    }
    class polygonAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectBtn = Type.POLYGON;
            Shapes.pressedX = -1;
            Shapes.pressedY = -1;
            polEndButton.setEnabled(true);
        }
    }
    class polEndAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            System.out.print("end button pressedX"+Shapes.pressedX+"\n");
            //if(Shapes.polX.size()>0) Shapes.polygon(Shapes.polX.get(0),Shapes.polY.get(0));
            Shapes.polygon();
            if(Shapes.readyToDraw)canvas.repaint();
            colors.setEnabled(true);
        }
    }

    class zoomAction implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            polEndButton.setEnabled(false);
            selectBtn = Type.ZOOM;
        }
    }

    class gridAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == gridBtn){
                gridWin();
            }
        }
    }


    class canvasAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (selectBtn == Type.PLOT) {
                Shapes.saveShape(e.getX(),e.getY(),"PLOT");
            } else if (selectBtn == Type.LINE) {
                //Shapes.line(e.getX(), e.getY());
                Shapes.saveShape(e.getX(),e.getY(),"LINE");
            } else if (selectBtn == Type.RECTANGLE) {
                //Shapes.rect(e.getX(), e.getY());
                Shapes.saveShape(e.getX(),e.getY(),"RECTANGLE");
            } else if (selectBtn == Type.ELLIPSE) {
                Shapes.saveShape(e.getX(),e.getY(),"ELLIPSE");
            } else if (selectBtn == Type.POLYGON) {
                colors.setEnabled(false);
                Shapes.polAdd(e.getX(), e.getY());
            }
            if(Shapes.readyToDraw)canvas.repaint();

        }
    }

    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            historyWindow.setLocation(getContentPane().getBounds().getSize().width - 300,50);
            colorWindow.setLocation(getContentPane().getBounds().getSize().width - 600,getContentPane().getBounds().getSize().height - 250);
            utilWindow.setLocation(0, getContentPane().getBounds().getSize().height - 80);
        }
    }

            //Shapes.polygon();
            //if(Shapes.readyToDraw)canvas.repaint();

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {
        createGUI();
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Gui("Vector Design Tool"));
    }
}


