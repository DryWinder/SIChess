package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;


public class Knight extends PieceAbstract{


    public Knight(Color color, PieceType KNIGHT, Square square){
        super(color, KNIGHT, square);
    }

    private boolean isKnightOnRightSquare(){
        //System.out.println((abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue())));
        //System.out.println((abs(this.newSquare.getRow() - this.previousSquare.getRow())));
        //System.out.println(this.newSquare.getColor());
        return ((abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == 1) && abs(this.newSquare.getRow() - this.previousSquare.getRow()) == 2)|| (abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == 2 && abs(this.newSquare.getRow() - this.previousSquare.getRow()) == 1);
    }

    public boolean isValidMove() {return isKnightOnRightSquare();}

    @Override
    public void drawPath(Square startSquare, Square endSquare) {

    }

    @Override
    public void validPath(Square startSquare, Square endSquare) {

    }

    //TODO: Disable kill option when there will be a hidden check
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

