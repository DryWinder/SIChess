package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;


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


    private boolean isKingOnRightSquare(){
        return (abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) + abs(this.newSquare.getRow() - this.previousSquare.getRow()) == 1)
                || (abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == 1 && abs(this.newSquare.getRow() - this.previousSquare.getRow()) == 1);
    }


    public boolean isValidMove() {
        return isKingOnRightSquare();
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

}
