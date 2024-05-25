package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;


public class Rook extends PieceAbstract{
    public int pieceScore = 5;

    public Rook(Color color, PieceType ROOK, Square square){
        super(color, ROOK, square);
    }


    @Override
    public void drawPath(Square startSquare, Square endSquare) {

    }

    public int getScore(){
        return this.pieceScore;
    }

    @Override
    public void validPath(Square startSquare, Square endSquare) {

    }

    private boolean isRookOnRightSquare(){
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
