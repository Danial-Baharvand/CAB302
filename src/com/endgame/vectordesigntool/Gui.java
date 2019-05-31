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
 * @version 4.0
 */
public class Gui extends JFrame implements ActionListener, Runnable {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Stores users screen resolution size in a variable
    private double WIDTH = screenSize.getWidth();
    private double HEIGHT = screenSize.getHeight();
    private JInternalFrame shapesWindow;
    private JInternalFrame colorWindow;
    private JInternalFrame historyWindow; //MIGHT DELETE DEPENDING ON IMPLEMENTATION
    private JInternalFrame utilWindow;
    private JColorChooser colors;
    private Type selectBtn; //Variable to store selected shape

    private JButton polEndButton; //Button that finishes drawing the polygon
    private JButton okBtn;

    private JComboBox zoomComboBox;

    static double zoomScale;
    static String tempVEC = ""; //This string is usd as cache, the VEC instructions are saved here
    static JPanel canvas;
    static final int canvSize = 1000; //canvas size can be changed from here
    enum Type {PLOT, LINE, RECTANGLE, ELLIPSE, POLYGON}

    /**
     *
     * @param title
     * @throws HeadlessException when code that is dependent on keyboard, display or mouse is called in environment
     * that does not support any of these things.
     */
    private Gui(String title) throws HeadlessException{
        super(title);
    }

    /**
     * Creates the actual GUI and its parameters
     */
    private void createGUI(){
        double widthProp = 0.8; //windows width compared to screen size
        double heightProp = 0.8; //windows height compared to screen size
        setSize((int)(WIDTH * widthProp), (int)(HEIGHT * heightProp)); //set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit gracefully
        setJMenuBar(createMenu()); //creates the menuBar
        getContentPane().add(display()); //add all contents (inside display) to frame
        addComponentListener(new ResizeListener()); //add the resize listener to keep inner windows at correct location
        setVisible(true); //make things visible
    }

    /**
     * Creates the background display of the window, separating the rest of the window from the drawing canvas.
     *
     * @return the background display.
     */
    private JDesktopPane display(){
        JDesktopPane bg = new JDesktopPane();// get a new JDesktopPane
        bg.setBackground(Color.BLACK);//set background color
        //add contents
        bg.add(createColorWindow());
        bg.add(makeCanvas());
        bg.add(createShapes());
        bg.add(createHistoryWindow());
        bg.add(createUtilWin());
        return bg;
    }

    /**
     * Creates the canvas on the window, the space in which shapes can be drawn on
     *
     * @return the canvas to be added to the display window.
     */
    private JPanel makeCanvas(){
        canvas = new MyPanel(); // get a new instance of MyPanel
        canvas.setSize(canvSize, canvSize); //set the canvas size (always a square)
        canvas.setLocation(150, 50);//set location
        canvas.setOpaque(true); //make the canvas opaque
        canvas.setBackground(Color.WHITE); //set canvas color
        canvas.addMouseListener(new canvasAction());// add a listener for mouse clicks on canvas
        return canvas;
    }

    /**
     * Creates the toolbar menu found on all standard applications. File, Save and Load features can be found tabbed
     * at the top of the page.
     *
     * @return the toolbar to be added to the display window.
     */
    private JMenuBar createMenu(){
        JMenuBar bar = new JMenuBar(); //get a Menu Bar
        //Menu options
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu tools = new JMenu("Tools");
        //Menu items
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
        //adding action listeners
        save.addActionListener((new saveAction()));
        load.addActionListener(new loadAction());
        undo.addActionListener(new undoAction());
        exit.addActionListener(new exitAction());
        shapes.addActionListener(new shapesToggleAction());
        toolColorChooser.addActionListener(new colorToggleAction());
        history.addActionListener(new historyToggleAction());
        return bar;
    }

    /**
     * Creates the frame displaying all the possible shapes/vectors the user can choose to input into the display canvas.
     * Allowing for the selection of Plot, Line, Rectangle, Ellipses and Polygons to be drawn to the canvas.
     *
     * @return the shapes frame to be added to the side of the display window.
     */
    private JInternalFrame createShapes(){
        shapesWindow = new JInternalFrame("Shapes");//get a new internal frame
        JPanel shapesPanel = new JPanel(new GridLayout(6, 1));//new shapes panel with grid layout
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

    /**
     * Creates the frame displaying all the possible colors the user can choose for the pen/fill
     *
     * @return the color frame to be added at the bottom of the display window.
     */
    private JInternalFrame createColorWindow(){
        colorWindow = new JInternalFrame("Color");// get a new internal frame
        colorWindow.setLayout(new FlowLayout());//set to flow layout
        //buttons
        JButton penColorButton = new JButton("Set Pen Color");
        JButton fillColorButton = new JButton("Set Fill Color");
        JButton noFillColorButton = new JButton("No Fill");
        penColorButton.addActionListener((new penColorAction()));
        fillColorButton.addActionListener((new fillColorAction()));
        noFillColorButton.addActionListener((new noFillColorAction()));
        colors= new JColorChooser(Color.BLACK);// get a new color chooser
        colors.setPreviewPanel(new JPanel());//get rid of the preview panel
        //Setting window parameters
        colorWindow.setSize(600, 300);
        colorWindow.setVisible(true);
        //Adding to window
        colorWindow.add(colors);
        colorWindow.add(penColorButton);
        colorWindow.add(fillColorButton);
        colorWindow.add(noFillColorButton);
        return colorWindow;
    }

    //DELETE IF NOT IMPLEMENTED//
    /**
     * Creates a history frame that stores the users actions in a graphical display.
     *
     * @return returns history frame to be added at the top of the display window.
     */
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
    ////

    /**
     * Creates a frame displaying the utilities options: Zoom and Grid for the canvas.
     *
     * @return utilWindow to be added to the bottom of the display window.
     */
    private JInternalFrame createUtilWin(){
        //Separate utilities window for zoom and grid features
        utilWindow = new JInternalFrame("Utilities");
        //Panel
        JPanel utilPanel = new JPanel(new GridLayout(1, 2));
        //Buttons
        JButton gridBtn = new JButton(new ImageIcon("grid.png"));
        JButton zoomBtn = new JButton(new ImageIcon("magnifyingGlass.png"));
        //Setting utils parameters in window
        utilWindow.setSize(100, 80);
        utilWindow.setLocation(0, 600);
        //Adding util to window
        utilPanel.add(zoomBtn);
        utilPanel.add(gridBtn);
        zoomBtn.addActionListener(new zoomAction());
        //gridBtn.addActionListener(new gridAction());
        utilWindow.add(utilPanel);
        utilWindow.setVisible(true);
        return utilWindow;
    }

    private void zoomWin(){
        //Popup window
        Integer[] zoomAmount = { 25, 50, 100, 200, 300, 400, 500 };
        JFrame parent = new JFrame("Zoom Input");
        //Panels
        JPanel zoomPanel = new JPanel(new BorderLayout()); //panel for zoom amount
        //Button
        okBtn = new JButton("Ok"); //Ok button
        //Text boxes
        zoomComboBox = new JComboBox(zoomAmount); //Enter texts
        //Text labels
        JLabel input = new JLabel();
        //Setting parameters
        zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.Y_AXIS));
        input.setText("Magnify by: ");
        zoomComboBox.setSelectedIndex(2);
        zoomComboBox.setPreferredSize(new Dimension(100, 20));
        //Adds
        zoomPanel.add(input, BorderLayout.LINE_START);
        zoomPanel.add(zoomComboBox, BorderLayout.LINE_END);
        parent.add(zoomPanel, BorderLayout.PAGE_START);
        parent.add(okBtn, BorderLayout.PAGE_END);
        zoomComboBox.addActionListener(this);
        okBtn.addActionListener(new okAction());
        //Display parameters
        parent.pack();
        parent.setVisible(true);
        parent.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /* Action Listener Classes*/

    /**
     * Exits system window
     */
    class exitAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }

    //menu save
    class saveAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            String saveFilePath;//stores save path
            //get a new file chooser at home directory
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showSaveDialog(null);//shows graphical interface, store user save loaction
            if (returnValue == JFileChooser.APPROVE_OPTION) {//if selected
                File selectedFile = jfc.getSelectedFile();// get the selected file
                //if user specified file name doesn't end with .vec (case insensitive), add it and get save path.
                if(!selectedFile.getAbsolutePath().toLowerCase().endsWith(".vec")){
                    saveFilePath=selectedFile.getAbsolutePath()+".vec";
                }else{
                    saveFilePath=selectedFile.getAbsolutePath();//get save path
                }
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(//get a new OutputStreamWriter
                        //write everything in the buffer to save location in ASCII format
                        new FileOutputStream(saveFilePath), StandardCharsets.US_ASCII))) {
                    writer.write(tempVEC);
                } catch (IOException ex) {// catch IO exceptions
                    ex.printStackTrace();
                }
            }
        }
    }

    //menu load
    class loadAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            //get a new file chooser at home directory
            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showOpenDialog(null);//shows graphical interface, store user load loaction
            if (returnValue == JFileChooser.APPROVE_OPTION) {//if selected
                File selectedFile = jfc.getSelectedFile();// get the selected file
                Gui.canvas.getGraphics().dispose();// clear the canvas
                try {
                    //load from file at selected location to the temp file with ASCII format
                    tempVEC= Files.readString(Paths.get(selectedFile.getAbsolutePath()), StandardCharsets.US_ASCII);
                } catch (IOException ex) {// catch IO exceptions
                    ex.printStackTrace();
                }
                canvas.repaint();
            }
        }
    }

    //DELETE IF UNDO NOT IMPLEMENTED//
    class undoAction implements ActionListener{
        public void actionPerformed (ActionEvent e){

        }
    }
    ////

    //toggle visibility of inner windows
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
                colorWindow.setLocation(getContentPane().getBounds().getSize().width-600,getContentPane()
                        .getBounds().getSize().height-250);
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

    //set drawing pen shape, also disable polEndButton as that is only relevant to polygon
    //finally reset click history in case another shape was in the middle of being drawn
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

    //set drawing pen shape to polygon, also enable polEndButton to provide the ability to end the polygon
    //finally reset click history in case another shape was in the middle of being drawn
    class polygonAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectBtn =Type.POLYGON;
            Shapes.pressedX = -1;
            Shapes.pressedY = -1;
            polEndButton.setEnabled(true);
        }
    }

    //finish the polygon and save it to temp, update the canvas
    class polEndAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Shapes.polygon();
            if(Shapes.readyToDraw)canvas.repaint();
        }
    }

    //store the selected color in the JColorChooser in the temp file
    class penColorAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            tempVEC = tempVEC + "PEN " + "#" + Integer.toHexString(colors.getColor().getRGB()).substring(2)+"\n";
        }
    }

    //store the selected fill color in the JColorChooser in the temp file
    class fillColorAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            tempVEC = tempVEC + "FILL " + "#" + Integer.toHexString(colors.getColor().getRGB()).substring(2)+"\n";
        }
    }

    //insert FILL OFF command in temp to avoid filling the next shape
    class noFillColorAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            tempVEC = tempVEC + "FILL OFF\n";
        }
    }

    class zoomAction implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            zoomWin();
        }
    }

    class okAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            zoomScale = (Integer)zoomComboBox.getSelectedItem(); //Getting value from comboBox
            double newCanvasSize = (zoomScale/100) * canvSize;
            canvas.setSize((int)newCanvasSize, (int)newCanvasSize);
        }
    }

//    class enterAction extends Component implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//            try {
//                getXTextValue = xTextField.getText();
//                getYTextValue = yTextField.getText();
//                xAxis = Double.parseDouble(getXTextValue); //Convert string to double
//                yAxis = Double.parseDouble(getYTextValue); //Convert string to double
//            } catch (NumberFormatException exception) {
//                JOptionPane.showMessageDialog(this, "Please input a positive coordinate", "Input: Error", JOptionPane.ERROR_MESSAGE);
//            }
//            if (xAxis <= 0 || yAxis <= 0) {
//                JOptionPane.showMessageDialog(this, "Please input a positive coordinate", "Input: Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }

    //Based on the selected button send the click coordinates to be processed
    //The shape instruction will be saved to the temp file once the shape is complete
    class canvasAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (selectBtn == Type.PLOT) {
                Shapes.saveShape(e.getX(),e.getY(),"PLOT");
            } else if (selectBtn == Type.LINE) {
                Shapes.saveShape(e.getX(),e.getY(),"LINE");
            } else if (selectBtn == Type.RECTANGLE) {
                Shapes.saveShape(e.getX(),e.getY(),"RECTANGLE");
            } else if (selectBtn == Type.ELLIPSE) {
                Shapes.saveShape(e.getX(),e.getY(),"ELLIPSE");
            } else if (selectBtn == Type.POLYGON) {
                Shapes.polAdd(e.getX(), e.getY());
            }
            if(Shapes.readyToDraw)canvas.repaint();

        }
    }

    //Keep window elements at correct position (also resets position) when window is resided
    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            historyWindow.setLocation(getContentPane().getBounds().getSize().width - 300,50);
            colorWindow.setLocation(getContentPane().getBounds().getSize().width - 600,getContentPane().getBounds().getSize().height - 300);
            utilWindow.setLocation(0, getContentPane().getBounds().getSize().height - 80);
        }
    }
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