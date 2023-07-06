package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;


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

    private boolean isRookOnRightSquare(){
        System.out.println(abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()));
        System.out.println(abs(this.newSquare.getRow() - this.previousSquare.getRow()));
        return ((abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == 0) && abs(this.newSquare.getRow() - this.previousSquare.getRow()) > 0)|| (abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) > 0 && abs(this.newSquare.getRow() - this.previousSquare.getRow()) == 0);
    }



    //TODO: Castle
    public boolean isValidMove() {
        return isRookOnRightSquare();
    }

    @Override
    public boolean isValidKill() {
        return this.newSquare.isTherePiece() && this.newSquare.getPieceColor() != this.getColor();
    }


    @Override
    public void isThereHiddenCheck() {

    }


}
