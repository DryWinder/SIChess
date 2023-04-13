package com.example.decoy;

import com.example.decoy.Chess.Board.File;
import com.example.decoy.Chess.Board.Square;
import com.example.decoy.Chess.Color;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JFrame {

    ArrayList<Square> squares = new ArrayList<>();
    /*Piece piece = new Piece();

    public Board(){
        this.add(piece);
        this.setTitle("Chess");
        this.setSize(600, 600);
        this.setBackground(Color.blue);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }*/

    public Board() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("Chess");
        this.setSize(600, 600);
        this.setVisible(true);
        this.squares = createSquares();
        for (Square square : squares) {
            this.add(square);
        }


    }


    private ArrayList<Square> createSquares() {
        int numberOfSquare = 1;
        for (int i = 1; i < 9; i++) {
            for (File file : File.values()) {
                if (numberOfSquare % 2 == 1) {
                    squares.add(new Square(Color.WHITE, file, i));
                } else {
                    squares.add(new Square(Color.BLACK, file, i));
                }
                numberOfSquare++;
            }
            numberOfSquare--;
        }

        return squares;
    }
}
