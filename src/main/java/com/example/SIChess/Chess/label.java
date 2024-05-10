package com.example.SIChess.Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class label {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo Frame");
        JPanel panel = new JPanel();
        panel.setBounds(100, 25,5,5);
        panel.setLayout(new GridBagLayout());
        JLabel label = new JLabel("Player 1 ");
        label.setIcon(new ImageIcon("C:\\Users\\Windows\\Desktop\\Tree\\Studying\\Java Folders\\SIChess\\src\\main\\resources\\Icons\\BlackBishop.png"));
        panel.setBackground(Color.RED);
        label.setBounds(100, 25,55,55);
        panel.add(label);
        frame.add(panel);
        frame.setBounds(250, 50, 750, 750);
        frame.setVisible(true);
    }
}
