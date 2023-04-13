package com.example.decoy.Chess.Pieces;

import com.example.decoy.Chess.Board.Square;
import com.example.decoy.Chess.Color;

import java.io.IOException;


public class Rook extends PieceAbstract{

    public Rook(Color color, PieceType ROOK, Square square){
        super(color, ROOK, square);
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
    public void move(Square square){

    }

}
