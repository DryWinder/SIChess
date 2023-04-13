package com.example.decoy.Chess.Pieces;

import com.example.decoy.Chess.Board.Square;
import com.example.decoy.Chess.Color;

import java.io.IOException;


public class King extends PieceAbstract{

    public King(Color color, PieceType KING, Square square){
        super(color, KING, square);
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