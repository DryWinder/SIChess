package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;


public class Queen extends PieceAbstract{
    public int pieceScore = 9;

    public Queen(Color color, PieceType QUEEN, Square square){
        super(color, QUEEN, square);
    }


    @Override
    public void drawPath(Square startSquare, Square endSquare) {

    }

    // TODO: Piece Should Never Walk Past Other Figures On Its Way
    @Override
    public void validPath(Square startSquare, Square endSquare) {

    }

    public int getScore(){
        return this.pieceScore;
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

    @Override
    public boolean getPawnsMoveHistory() {
        return false;
    }

    @Override
    public void setNumberPieceHasMoved(int number){

    }

    @Override
    public int getNumberPieceHasMoved(){return 0;}

}

