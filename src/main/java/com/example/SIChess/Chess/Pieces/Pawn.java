package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;

import static java.lang.Math.abs;

public class Pawn extends PieceAbstract{

    boolean justMovedTwoSquaresForward = false;
     public Pawn(Color color, PieceType PAWN, Square square){
        super(color, PAWN, square);
    }

    private int numberOfSquaresForPawnToPass(){
        if(!hasMoved()){return 2;}
        else {return 1;}
    }

    @Override
    public boolean getPawnsMoveHistory(){
         return justMovedTwoSquaresForward;
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
    private boolean isWhitePawnMovingOneSquareForward(){
        return this.previousSquare.getRow() - this.newSquare.getRow() == 1;
    }

    private boolean isBlackPawnMovingRightNumberOfSquaresForward(int numberOfSquaresThatPawnCanPass){
        return this.newSquare.getRow() - this.previousSquare.getRow() <= numberOfSquaresThatPawnCanPass;
    }

    private boolean isBlackPawnMovingOneSquareForward(){
        return this.newSquare.getRow() - this.previousSquare.getRow() == 1;
    }

    public boolean isValidMove() {
         boolean isValid = false;
         int numberOfSquaresThatPawnCanPass = numberOfSquaresForPawnToPass();
         if (isWhite()){
             isValid = isPawnOnRightFile() && isWhitePawnMovingForward() && isWhitePawnMovingRightNumberOfSquaresForward(numberOfSquaresThatPawnCanPass);
         }
         else{
             isValid = isPawnOnRightFile() && isBlackPawnMovingForward() && isBlackPawnMovingRightNumberOfSquaresForward(numberOfSquaresThatPawnCanPass);
         }

         if(isValid && numberOfSquaresThatPawnCanPass == 2){
             this.justMovedTwoSquaresForward = true;
         }
         else {
             this.justMovedTwoSquaresForward = false;
         }
         /*if(isValidKill()){
             isValid = true;
         }*/
         return isValid;
    }

    public boolean isEnemyPieceOnRightFile(){
        return abs(this.newSquare.getFile().getValue() - this.previousSquare.getFile().getValue()) == 1;
    }

    public boolean isValidKill(){
         if(isWhite()){
             return isEnemyPieceOnRightFile() && isWhitePawnMovingForward() && this.newSquare.isTherePiece() && !this.newSquare.isPieceWhite() && isWhitePawnMovingOneSquareForward();
         }
         if(isBlack()){
             return isEnemyPieceOnRightFile() && isBlackPawnMovingForward() && this.newSquare.isTherePiece() && this.newSquare.isPieceWhite() && isBlackPawnMovingOneSquareForward();
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

