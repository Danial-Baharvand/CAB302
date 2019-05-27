package com.endgame.vectordesigntool;

import javax.swing.*;
import java.awt.*;

public class Grid extends JFrame {

    public Grid() {
        super("Grid Input");
    }

    public void inputWin() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea(15, 50);
        JPanel inputPan = new JPanel();
        inputPan.setLayout(new FlowLayout());
        JTextField input = new JTextField(20);
        JButton button = new JButton("Enter");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        inputPan.add(input);
        inputPan.add(button);

        panel.add(inputPan);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
