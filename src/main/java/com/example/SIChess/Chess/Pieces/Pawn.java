package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;

public class Pawn extends PieceAbstract{
     public Pawn(Color color, PieceType PAWN, Square square){
        super(color, PAWN, square);
    }

    private int numberOfSquaresForPawnToPass(){
        if(!hasMoved()){return 2;}
        else {return 1;}
    }

    private boolean isPawnOnRightFile(){
         return this.newSquare.getFile() == this.previousSquare.getFile();
    }

    private boolean isWhitePawnMovingForward(){
         return this.previousSquare.getRow() > this.newSquare.getRow();
    }

    private boolean isBlackPawnMovingForward(){
        return this.previousSquare.getRow() < this.newSquare.getRow();
    }

    private boolean isWhitePawnMovingRightNumberOfSquaresForward(int numberOfSquaresThatPawnCanPass){
        return this.previousSquare.getRow() - this.newSquare.getRow() <= numberOfSquaresThatPawnCanPass;
    }

    private boolean isBlackPawnMovingRightNumberOfSquaresForward(int numberOfSquaresThatPawnCanPass){
        return this.newSquare.getRow() - this.previousSquare.getRow() <= numberOfSquaresThatPawnCanPass;
    }

    public boolean isValidMove() {
         int numberOfSquaresThatPawnCanPass = numberOfSquaresForPawnToPass();
         if (isWhite()){
             return isPawnOnRightFile() && isWhitePawnMovingForward() && isWhitePawnMovingRightNumberOfSquaresForward(numberOfSquaresThatPawnCanPass);
         }
         else{
             return isPawnOnRightFile() && isBlackPawnMovingForward() && isBlackPawnMovingRightNumberOfSquaresForward(numberOfSquaresThatPawnCanPass);
         }
    }

    public boolean isEnemyPieceOnRightFile(){
        return abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == 1;
    }

    public boolean isValidKill(){
         if(isWhite()){
             return isEnemyPieceOnRightFile() && isWhitePawnMovingForward() && this.newSquare.isTherePiece() && !this.newSquare.isPieceWhite();
         }
         if(isBlack()){
             return isEnemyPieceOnRightFile() && isBlackPawnMovingForward() && this.newSquare.isTherePiece() && this.newSquare.isPieceWhite();
         }
         return false;
    }

    @Override
    public void drawPath(Square startSquare, Square endSquare) {

    }

    // TODO: Start position, first possible move, next moves captures, En Passant
    @Override
    public void validPath(Square startSquare, Square endSquare) {

    }

    @Override
    public void isThereHiddenCheck() {

    }


}

