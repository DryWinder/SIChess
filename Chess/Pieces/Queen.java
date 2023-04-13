package com.example.decoy.Chess.Pieces;

import com.example.decoy.Chess.Board.Square;
import com.example.decoy.Chess.Color;

import java.io.IOException;


public class Queen extends PieceAbstract{

    public Queen(Color color, PieceType QUEEN, Square square){
        super(color, QUEEN, square);
    }


    @Override
    public void drawPath(Square startSquare, Square endSquare) {

    }

    @Override
    public void validPath(Square startSquare, Square endSquare) {

    }

    @Override
    public boolean isValidKill() {
        return false;
    }


    @Override
    public void move(Square square) {

    }
}

