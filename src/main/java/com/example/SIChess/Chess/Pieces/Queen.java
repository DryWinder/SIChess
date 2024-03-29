package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;


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


    private boolean isQueenMovedLikeBishop(){
        return abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == abs(this.newSquare.getRow() - this.previousSquare.getRow());
    }


    private boolean isQueenMovedLikeRook(){
        return ((abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == 0) && abs(this.newSquare.getRow() - this.previousSquare.getRow()) > 0)|| (abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) > 0 && abs(this.newSquare.getRow() - this.previousSquare.getRow()) == 0);
    }


    public boolean isValidMove() {
        return isQueenMovedLikeBishop() || isQueenMovedLikeRook();
    }


    @Override
    public boolean isValidKill() {
        return this.newSquare.isTherePiece() && this.newSquare.getPieceColor() != this.getColor();
    }


    @Override
    public void isThereHiddenCheck() {

    }

}

