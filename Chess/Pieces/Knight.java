package com.example.decoy.Chess.Pieces;

import com.example.decoy.Chess.Board.Square;
import com.example.decoy.Chess.Color;

import java.io.IOException;


public class Knight extends PieceAbstract{


    public Knight(Color color, PieceType KNIGHT, Square square){
        super(color, KNIGHT, square);
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

