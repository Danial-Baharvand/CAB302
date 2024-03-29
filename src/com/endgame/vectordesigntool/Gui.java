package com.endgame.vectordesigntool;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Application - GUI creation and declaration, in addition to action listener classes
 *
 * @author Group_010 - Daniel Baharvand, James Dick, Jai Hunt, Jovi Lee
 * @version 5.0
 */
public class Gui extends JFrame implements ActionListener, Runnable {
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    //initialising up our variables
    enum Type {PLOT, LINE, RECTANGLE, ELLIPSE, POLYGON}//stores type of the shape
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//getting user's resolution
    private double WIDTH = screenSize.getWidth(); // screen width
    private double HEIGHT = screenSize.getHeight();//screen height
    private static JButton polEndButton;//button completes a polygon
    //initialising internal windows
    private JInternalFrame shapesWindow;
    private JInternalFrame colorWindow;
    static JInternalFrame historyWindow;
    //initialising history window buttons
    private JButton cancelHistory;
    private JButton confirmHistory;
    static JColorChooser colors;//initialising the colorChooser
    static Type selectBtn;//stores which shape is currently selected
    static JTextField gridXField;//holds the horizontal size of the grid
    static JTextField gridYField;//holds the vertical size of the grid
    private static JTextField bmpResField;//holds the user specified resolution for the bitmap export
    static JPanel canvas;// initialising the canvas
    static String tempVEC="";//this string is usd as cache, the VEC instructions are saved here
    static String historyTempVEC="";
    static int canvSize = 1000;// canvas size can be changed form here
    static DefaultListModel<String> model;//keeps the list items
    static int gridX=-1;//value of gridXField converted to integer, set to -1 to disable grid
    static int gridY=-1;//value of gridYField converted to integer, set to -1 to disable grid

    static JList<String> list;
    static int selectedHistory=-2;

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
        double widthProp = 0.8;//windows width compared to screen size
        double heightProp = 0.8;//windows height compared to screen size
        setSize((int)(WIDTH * widthProp), (int)(HEIGHT * heightProp));//set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//exit gracefully
        setJMenuBar(createMenu());//create the menuBar
        getContentPane().add(display());//add all contents (inside display) to frame
        addComponentListener(new ResizeListener());//add the resize listener to keep inner windows at correct location
        setVisible(true);//make things visible
    }

    /**
     * Creates the background display of the window, separating the rest of the window from the drawing canvas.
     *
     * @return the background display
     */
    private JDesktopPane display(){
        JDesktopPane bg = new JDesktopPane();// get a new JDesktopPane
        bg.setBackground(Color.lightGray);//set background color
        //add contents
        bg.add(createColorWindow());
        bg.add(createHistoryWindow());
        bg.add(createShapes());
        bg.add(makeCanvas());
        return bg;
    }

    /**
     * Creates the canvas on the window, the space in which shapes can be drawn on
     *
     * @return the canvas to be added to the display window.
     */
    static JPanel makeCanvas(){
        canvas = new MyPanel(); // get a new instance of MyPanel
        canvas.setSize(canvSize, canvSize); //set the canvas size (always a square)
        canvas.setLocation(80, 30);//set loction
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
        JMenuBar bar = new JMenuBar();//get a Menu Bar
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
        JMenuItem history = new JMenuItem("History");
        JMenuItem grid = new JMenuItem("Grid");
        JMenuItem bmp = new JMenuItem("Save as BMP");
        //Setting menu parameters
        bar.setBackground(Color.gray);
        bar.setPreferredSize(new Dimension(200, 20));
        //Adding menu functions
        bar.add(file);
        file.add(save);
        file.add(bmp);
        file.add(load);
        file.add(exit);
        bar.add(edit);
        edit.add(undo);
        bar.add(tools);
        tools.add(shapes);
        tools.add(toolColorChooser);
        tools.add(history);
        tools.add(grid);
        //adding action listeners
        save.addActionListener(new saveAction());
        bmp.addActionListener(new bmpAction());
        load.addActionListener(new loadAction());
        undo.addActionListener(new undoAction());
        exit.addActionListener(new exitAction());
        shapes.addActionListener(new shapesToggleAction());
        toolColorChooser.addActionListener(new colorToggleAction());
        history.addActionListener(new historyToggleAction());
        grid.addActionListener(new gridAction());
        //bind undo to CTRL Z
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_DOWN_MASK));
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
        JButton noFillColorButton = new JButton("Fill Off");
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

    /**
     * Creates a history frame that stores the users actions in a graphical display.
     *
     * @return history frame to be added at the top of the display window.
     */
    private JInternalFrame createHistoryWindow() {
        model = new DefaultListModel<>();//holds history window items
        model.addAll(Arrays.asList(tempVEC.split("\n")));//adds each line of tempVEC as an item in the list
        list = new JList<>( model );//make a new JList with the model as its contents
        list.addListSelectionListener(new myListSelectionListener());//add the listener for clicks on list items
        // set up the history window
        historyWindow = new JInternalFrame("History");
        historyWindow.setLayout(new BorderLayout());//set to flow layout
        //buttons
        confirmHistory = new JButton("Confirm");
        cancelHistory = new JButton("Cancel");
        confirmHistory.addActionListener((new confirmHistoryAction()));
        cancelHistory.addActionListener((new cancelHistoryAction()));
        JScrollPane scrollWindow = new JScrollPane(list);//make a scrolling pane with list
        //Setting parameters
        scrollWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        historyWindow.setSize(300, 300);
        historyWindow.setVisible(true);
        historyWindow.add(scrollWindow,BorderLayout.CENTER); //Adding scrolling window
        //add the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmHistory);
        buttonPanel.add(cancelHistory);
        historyWindow.add(buttonPanel,BorderLayout.SOUTH);
        //disable the buttons
        cancelHistory.setEnabled(false);
        confirmHistory.setEnabled(false);
        return historyWindow;
    }

    /**
     * makes the popup grid window
     */
    void gridWin(){
        //Popup window
        JFrame parent = new JFrame("Grid Input");
        //Panels
        JPanel xPanel = new JPanel(new BorderLayout()); //panel for x option
        JPanel yPanel = new JPanel(new BorderLayout()); //panel for y option
        //Button
        JButton enterBtn = new JButton("Confirm");
        //Text boxes
        gridXField = new JTextField(10);
        gridYField = new JTextField(10);
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
        xPanel.add(gridXField, BorderLayout.LINE_END);
        yPanel.add(yInput, BorderLayout.LINE_START);
        yPanel.add(gridYField, BorderLayout.LINE_END);
        parent.add(xPanel, BorderLayout.PAGE_START);
        parent.add(yPanel, BorderLayout.CENTER);
        parent.add(enterBtn, BorderLayout.PAGE_END);
        enterBtn.addActionListener(new gridEnterAction());
        //Display parameters
        parent.pack();
        parent.setVisible(true);
        parent.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * makes the popup bmp window
     */
    private void bmpWin(){
        //Popup window
        JFrame parent = new JFrame("Grid Input");
        //Panel
        JPanel xPanel = new JPanel(new BorderLayout()); //panel for x option
        //Button
        JButton enterBtn = new JButton("Confirm");
        //Text boxe
        bmpResField = new JTextField(10);
        //Text label
        JLabel xInput = new JLabel();
        //Setting parameters
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.Y_AXIS));
        xInput.setText("Please input the desired resolution: ");
        //Adds
        xPanel.add(xInput, BorderLayout.LINE_START);
        xPanel.add(bmpResField, BorderLayout.LINE_END);
        parent.add(xPanel, BorderLayout.PAGE_START);
        parent.add(enterBtn, BorderLayout.PAGE_END);
        enterBtn.addActionListener(new bmpEnterAction());
        //Display parameters
        parent.pack();
        parent.setVisible(true);
        parent.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    static void placeGrid() {
        try {
            gridX = Integer.parseInt(gridXField.getText()); //get text input and convert string to int
            gridY = Integer.parseInt(gridYField.getText()); //get text input and convert string to int
        } catch (NumberFormatException exception) {//catch if not an integer
            JOptionPane.showMessageDialog(null, "Please input a positive integer",
                    "Input: Error", JOptionPane.ERROR_MESSAGE);
        }
        if (gridX <= 0 || gridY <= 0 || gridX>canvSize/2 ||gridY>canvSize/2) {//check valid range
            JOptionPane.showMessageDialog(null, "Please input an integer between 1 and "
                    + canvSize, "Input: Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* Action Listener Implementation Classes */

    /**
     * menu exit
     */
    class exitAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }//exit successfully
    }

    /**
     * menu save
     */
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
                    JOptionPane.showMessageDialog(getContentPane(), "Please select a valid save location",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * display the bmp window
     */
    class bmpAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            bmpWin();
        }
    }

    /**
     * menu load
     */
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
                    Shapes.fillColor=null;//discord previos fill color
                    tempVEC= Files.readString(Paths.get(selectedFile.getAbsolutePath()), StandardCharsets.US_ASCII);
                    if(!tempVEC.endsWith("\n")) tempVEC=tempVEC+"\n";
                } catch (IOException ex) {// catch IO exceptions
                    JOptionPane.showMessageDialog(getContentPane(), "Please select a valid VEC file",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                canvas.repaint();
            }
        }
    }

    /**
     * Undo functionality for user actions
     */
    static void doUndo(){
        //implement undo
        int endIndex = tempVEC.length()-2;//starts from temp lengths -2 to avoid last \n
        endIndex = tempVEC.lastIndexOf('\n', endIndex-1);//get the index of last character before last line
        if(endIndex==-1){endIndex=0;}//for the last line
        tempVEC=tempVEC.substring(0,endIndex)+"\n";//update temp

    }
    class undoAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            doUndo();
            repaint();//show the updated canvas
        }
    }

    /**
     * toggle visibility and location of shapes window
     */
    class shapesToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(shapesWindow.isVisible())shapesWindow.setVisible(false);
            else {
                shapesWindow.setVisible(true);
                shapesWindow.setLocation(0,30);
            }
        }
    }

    /**
     * toggle visibility, location and size of color window
     */
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

    /**
     * toggle visibility, location and size of history window
     */
    class historyToggleAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            if(historyWindow.isVisible()){
                historyWindow.setLocation(getContentPane().getBounds().getSize().width-300,50);
                historyWindow.setVisible(false);
            } else historyWindow.setVisible(true);
        }
    }

    /**
     * set drawing plot shape, also disable polEndButton as that is only relevant to polygon. Finally reset click
     * history in case another shape was in the middle of being drawn
     */
    class plotAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.PLOT;
            Shapes.resetShapesCoordinates();

        }
    }

    /**
     * set drawing line, also disable polEndButton as that is only relevant to polygon. Finally reset click history
     * in case another shape was in the middle of being drawn
     */
    class lineAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.LINE;
            Shapes.resetShapesCoordinates();
        }
    }

    /**
     * set drawing rectangle, also disable polEndButton as that is only relevant to polygon. Finally reset click
     * history in case another shape was in the middle of being drawn
     */
    class rectAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.RECTANGLE;
            Shapes.resetShapesCoordinates();
        }
    }

    /**
     * set drawing ellipse, also disable polEndButton as that is only relevant to polygon. Finally reset click
     * history in case another shape was in the middle of being drawn
     */
    class ellipseAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            polEndButton.setEnabled(false);
            selectBtn = Type.ELLIPSE;
            Shapes.resetShapesCoordinates();
        }
    }

    /**
     * set drawing pen shape to polygon, also enable polEndButton to provide the ability to end the polygon finally
     * reset click history in case another shape was in the middle of being drawn
     */
    class polygonAction implements ActionListener{
        public void actionPerformed (ActionEvent e){
            selectBtn =Type.POLYGON;
            Shapes.resetShapesCoordinates();
        }
    }

    /**
     * finish the polygon and save it to temp, update the canvas
     */
    class polEndAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Shapes.polygon();
            polEndButton.setEnabled(false);
            if(Shapes.readyToDraw)canvas.repaint();
        }
    }

    /**
     * Actions for when set pen colour button is pressed
     */
    class penColorAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            doChangePenColour();
            repaint();
        }
    }

    /**
     * Store the selected color in the JColorChooser in the temp file
     */
    static void doChangePenColour() {
        tempVEC = tempVEC + "PEN " + "#" + Integer.toHexString(colors.getColor().getRGB()).substring(2)+"\n";
    }

    /**
     * Actions for when set fill colour button is pressed
     */
    class fillColorAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            doChangeFillColour();
            repaint();
        }
    }

    /**
     * Store the selected fill color in the JColorChooser in the temp file
     */
    static void doChangeFillColour() {
        tempVEC = tempVEC + "FILL " + "#" + Integer.toHexString(colors.getColor().getRGB()).substring(2)+"\n";
    }

    /**
     * Actions for when no fill colour button is pressed
     */
    class noFillColorAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            doRemoveFillColour();
            repaint();
        }

    }

    /**
     * Insert FILL OFF command in temp to avoid filling the next shape
     */
    static void doRemoveFillColour() {
        tempVEC = tempVEC + "FILL OFF\n";
    }

    /**
     * confirm the selected stage of drawing
     */
    static void confirmHistory(){
        tempVEC = historyTempVEC+"\n";//set the main temp to the history temp
        selectedHistory=-1;//reset the menu selected item
    }
    class confirmHistoryAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            confirmHistory();
            //disable buttons
            cancelHistory.setEnabled(false);
            confirmHistory.setEnabled(false);
            repaint();//repaint to show updated list
        }
    }

    /**
     * cancel the selected drawing stage and go back to the latest drawing
     */
    class cancelHistoryAction implements ActionListener{
        public void actionPerformed(ActionEvent e) {
           selectedHistory=-1;//reset the menu selected item
            //disable buttons
           cancelHistory.setEnabled(false);
           confirmHistory.setEnabled(false);
           repaint();//repaint to show updated list
        }
    }

    /**
     * Opens the grid window
     */
    class gridAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(gridX<0&&gridY<0) {//show the grid window if the grid is not enabled
                gridWin();
            }else {//remove the grid if grid is active
                gridX=-1;
                gridY=-1;
                repaint();//show canvas without grid
            }
        }
    }

    /**
     * Produces the grid when the desired grid size is entered and the Confirm button is pressed
     */
    class gridEnterAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            placeGrid();
            repaint();// show the grid
        }
    }

    /**
     * saves a bitmap of the drawn image to the chosen location
     */
    class bmpEnterAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int bmpRes = -1;//stores bitmap size
            try {
                if (Integer.parseInt(bmpResField.getText()) < 1) throw new UserInputException();//check valid range
                bmpRes = Integer.parseInt(bmpResField.getText()); //get text input and convert string to int
                String saveFilePath;//stores save path
                //get a new file chooser at home directory
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = jfc.showSaveDialog(null);//shows graphical interface, store user save loaction
                if (returnValue == JFileChooser.APPROVE_OPTION) {//if selected
                    File selectedFile = jfc.getSelectedFile();// get the selected file
                    //if user specified file name doesn't end with .bmp (case insensitive), add it and get save path.
                    if (!selectedFile.getAbsolutePath().toLowerCase().endsWith(".bmp")) {
                        saveFilePath = selectedFile.getAbsolutePath() + ".bmp";
                    } else {
                        saveFilePath = selectedFile.getAbsolutePath();//get save path
                    }
                    MyPanel bmpPanel = new MyPanel();//get a ne MyPanel to draw on
                    bmpPanel.setSize(bmpRes, bmpRes);//set the size based on the user input
                    bmpPanel.setBackground(Color.WHITE);//set the canvas color
                    int tempCanvSize = canvSize;//store orignal canvas size
                    canvSize = bmpRes;//change canvas size to the user requested resolution
                    //make a new, empty, buffered image for the image to be drawn to
                    try {
                        BufferedImage image = new BufferedImage(bmpRes, bmpRes, BufferedImage.TYPE_INT_RGB);
                        Graphics g = image.getGraphics();//get the graphics of the buffered image
                        bmpPanel.paint(g);//paint the drawing to the buffered image's graphic
                        canvSize = tempCanvSize;//restore original canvas size

                        ImageIO.write(image, "bmp", new File(saveFilePath));//save the image

                    } catch (IllegalArgumentException|OutOfMemoryError|NegativeArraySizeException ex1) {
                        JOptionPane.showMessageDialog(getContentPane(), "Please enter a reasonable resolution",
                                "IO: Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), "Please ensure IO is available to be written to",
                                "IO: Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            } catch (NumberFormatException | UserInputException exception) {//catch if not an integer
                JOptionPane.showMessageDialog(getContentPane(), "Please input a positive integer greater than 0",
                        "Input: Error", JOptionPane.ERROR_MESSAGE);
            }


        }
    }

    /**
     * based on the selected button send the click coordinates to be proccessed. the shape instruction will be saved
     * to the temp file once the shape is complete
     */
    static class canvasAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x=e.getX();//get mouse click x
            int y=e.getY();//get mouse click y
            //set nearest x and y to integer infinity to make sure they are overwritten
            int nearestX=Integer.MAX_VALUE;
            int nearestY=Integer.MAX_VALUE;
            if(gridX>0&&gridY>0){//if the grid is active
                for (int i=0;i<canvSize/gridX;i++){// for each vertical grid line
                    if(Math.abs((gridX*i)-x)<Math.abs(nearestX-x)){//compare click loaction with grid line
                        nearestX=gridX*i;//store the nearest grid to the click loaction
                    }
                }
                for (int i=0;i<canvSize/gridY;i++){// for each horizantal grid line
                    if(Math.abs((gridY*i)-y)<Math.abs(nearestY-y)){//compare click loaction with grid line
                        nearestY=gridY*i;//store the nearest grid to the click loaction
                    }
                }
                //swap original mouse click loaction with the closest grid point
                x=nearestX;
                y=nearestY;
            }

            if (selectBtn == Type.PLOT) {
                Shapes.saveShape(x,y,"PLOT");
            } else if (selectBtn == Type.LINE) {
                Shapes.saveShape(x,y,"LINE");
            } else if (selectBtn == Type.RECTANGLE) {
                //Shapes.rect(e.getX(), e.getY());
                Shapes.saveShape(x,y,"RECTANGLE");
            } else if (selectBtn == Type.ELLIPSE) {
                Shapes.saveShape(x,y,"ELLIPSE");
            } else if (selectBtn == Type.POLYGON) {
                if(Shapes.polCount>1)polEndButton.setEnabled(true);
                Shapes.polAdd(x, y);//adds a single point to the polygon
            }
            if(Shapes.readyToDraw)canvas.repaint();

        }
    }

    /**
     * keep window elements at correct position (also resets position) when window is resided
     */
    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            historyWindow.setLocation(getContentPane().getBounds().getSize().width - 300,50);
            colorWindow.setLocation(getContentPane().getBounds().getSize().width - 600,getContentPane().getBounds().getSize().height - 300);
        }
    }

    /**
     * updates a secondary temp when a menu item is selected and shows the drawing at that stage
     */
    static void updateHistory(){
            selectedHistory = list.getSelectedIndex();//save selected history menu item
            //deleting the undo instructions
            int endIndex = -1;//starts from -1 and is increased for the number of desired characters
            for (int i = 0; i <= selectedHistory; i++) {
                endIndex = tempVEC.indexOf('\n', endIndex + 1);//get the index of the last character
            }
            historyTempVEC = tempVEC.substring(0, endIndex);//save the updated string to history temp
    }
    class myListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting() && !list.isSelectionEmpty()) {//check if a menu item is properly selected
                //enable buttons
                updateHistory();
                cancelHistory.setEnabled(true);
                confirmHistory.setEnabled(true);
                repaint();//repaint so the user sees the changes on the canvas immediately
            }

        }
    }
    class UserInputException extends Exception {
        public UserInputException() {
            super();
        }

        public UserInputException(String message) {
            super(message);
        }

        public UserInputException(String message, Throwable cause) {
            super(message, cause);
        }

        public UserInputException(Throwable cause) {
            super(cause);
        }
    }

    @Override
    public void run() {
        createGUI();
    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Gui("Vector Design Tool"));
    }
}


