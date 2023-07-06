package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;


public class Bishop extends PieceAbstract{

    public Bishop(Color color, PieceType BISHOP, Square square){
        super(color, BISHOP, square);
    }


    @Override
    public void drawPath(Square startSquare, Square endSquare) {

    }

    @Override
    public void validPath(Square startSquare, Square endSquare) {

    }


    private boolean isBishopOnRightSquare(){
        return abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == abs(this.newSquare.getRow() - this.previousSquare.getRow());
    }


    private boolean isBishopOnTheRightColor(){
        return this.actualSquare.getColor() == this.newSquare.getColor();
    }


    public boolean isValidMove() {
        return isBishopOnRightSquare() && isBishopOnTheRightColor();
    }


    @Override
    public boolean isValidKill() {
        return this.newSquare.isTherePiece() && this.newSquare.getPieceColor() != this.getColor();
    }

    @Override
    public void isThereHiddenCheck() {

    }

}

