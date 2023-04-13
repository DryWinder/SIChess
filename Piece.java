package com.example.decoy;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Piece extends JPanel {
    ImageIcon image = new ImageIcon("C:\\Users\\Windows\\Desktop\\Tree\\Studying\\Java Folders\\SIChess\\src\\main\\resources\\Icons\\BlackKnight.png");
    final int WIDTH = 64;//image.getIconWidth();
    final int HEIGHT = 64;//image.getIconHeight();
    Point imageCorner;
    Point prevPt;


    Piece(){
        imageCorner = new Point(0,0);
        ClickListener clickListener = new ClickListener();
        DragListener dragListener = new DragListener();
        this.addMouseListener(clickListener);
        this.addMouseMotionListener(dragListener);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
        this.setBounds((int)imageCorner.getX(), (int)imageCorner.getY(), 64, 64);
        image.paintIcon(this, g,(int)imageCorner.getX(), (int)imageCorner.getY());
    }

    private class ClickListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            prevPt = e.getPoint();
        }
    }

    private class DragListener extends MouseMotionAdapter{
        public void mouseDragged(MouseEvent e){
            Point currentPt = e.getPoint();
            imageCorner.translate(
                    (int)(currentPt.getX() - prevPt.getX()),
                    (int)(currentPt.getY() - prevPt.getY())
            );
            prevPt = currentPt;
            repaint();
        }
    }
}
