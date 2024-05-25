package com.example.SIChess.Chess.Engine;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;
import com.example.SIChess.Chess.Pieces.PieceAbstract;
import com.example.SIChess.Chess.Pieces.PieceType;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class PawnStructureEvaluator {
    private ArrayList<PieceAbstract> pieces;
    private ArrayList<Square> squares;
    int numberOfSquaresWithoutPawns = 0;
    int numberOfIsolatedPawns = 0;


    public PawnStructureEvaluator(ArrayList<PieceAbstract> pieces, ArrayList<Square> squares){
        this.pieces = pieces;
        this.squares = squares;
    }

    public int evaluatePawnSructure(){
        int stackedPawns = stakedOrIsolatedPawnsFinder();
        System.out.println("STACKEDPAWNS: " + stackedPawns);
        System.out.println("ISOLATED: " + this.numberOfIsolatedPawns);

        System.out.println("PAWNS SCORE: " + (80-stackedPawns*10 - numberOfIsolatedPawns*5));
        return 80-stackedPawns*10;
    }

    private int stakedOrIsolatedPawnsFinder(){
        int numberOfStackedPawns = 0;

        for(PieceAbstract piece : pieces){
            if(piece.getPieceType() == PieceType.PAWN){
                Square actualSquare = piece.getActualSquare();
                for(Square square : squares){
                    if(abs(square.getFile().getValue() - actualSquare.getFile().getValue()) <= 1){
                        if(square.getPiece() == null){
                            this.numberOfSquaresWithoutPawns +=1;
                        }
                        else{
                            if(square.getPiece().getPieceType() == PieceType.PAWN && square.getPiece().getColor() == piece.getColor()){

                            }
                            else{
                                this.numberOfSquaresWithoutPawns += 1;
                            }
                        }
                    }
                    if(piece.getColor() == Color.WHITE){
                        if(square.getRow() == actualSquare.getRow() - 1 && square.getFile() == actualSquare.getFile()){
                            if(square.getPiece() != null){
                                if(square.getPiece().getPieceType() == PieceType.PAWN && square.getPiece().getColor() == Color.WHITE){
                                    numberOfStackedPawns += 1;
                                }
                            }
                        }
                    }
                    if(piece.getColor() == Color.BLACK){
                        if(square.getRow() == actualSquare.getRow() + 1 && square.getFile() == actualSquare.getFile()){
                            if(square.getPiece() != null){
                                if(square.getPiece().getPieceType() == PieceType.PAWN && square.getPiece().getColor() == Color.BLACK){
                                    numberOfStackedPawns += 1;
                                }
                            }
                        }
                    }
                }
                if(this.numberOfSquaresWithoutPawns == 23){
                    this.numberOfIsolatedPawns += 1;
                }
                this.numberOfSquaresWithoutPawns = 0;
            }
        }

        return numberOfStackedPawns;
    }

}
