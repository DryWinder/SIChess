package com.example.SIChess.Chess.Engine;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;
import com.example.SIChess.Chess.Pieces.PieceAbstract;
import com.example.SIChess.Chess.Pieces.PieceType;

import java.util.ArrayList;
import java.util.HashMap;

public class ValueOfPieceesOnBoard {

    private ArrayList<PieceAbstract> pieces;
    private ArrayList<Square> squares;
    private Color color;
    HashMap<PieceType, Integer> chessPiecesWhite = new HashMap<>();
    HashMap<PieceType, Integer> chessPiecesBlack = new HashMap<>();

    public ValueOfPieceesOnBoard(ArrayList<PieceAbstract> pieces, ArrayList<Square> squares, Color color){
        this.pieces = pieces;
        this.squares = squares;
        this.color = color;
    }

    public double evaluateValue(){
        int value = 0;

        chessPiecesWhite.put(PieceType.QUEEN, 0);
        chessPiecesWhite.put(PieceType.ROOK, 0);
        chessPiecesWhite.put(PieceType.BISHOP, 0);
        chessPiecesWhite.put(PieceType.KNIGHT, 0);
        chessPiecesWhite.put(PieceType.PAWN, 0);

        chessPiecesBlack.put(PieceType.QUEEN, 0);
        chessPiecesBlack.put(PieceType.ROOK, 0);
        chessPiecesBlack.put(PieceType.BISHOP, 0);
        chessPiecesBlack.put(PieceType.KNIGHT, 0);
        chessPiecesBlack.put(PieceType.PAWN, 0);

        for(PieceAbstract piece : pieces){
            if(piece.getColor() == Color.WHITE){
                if (chessPiecesWhite.containsKey(piece.getPieceType())) {
                    chessPiecesWhite.put(piece.getPieceType(), chessPiecesWhite.get(piece.getPieceType()) + 1);
                }
            }
            else {
                if (chessPiecesBlack.containsKey(piece.getPieceType())) {
                    chessPiecesBlack.put(piece.getPieceType(), chessPiecesBlack.get(piece.getPieceType()) + 1);
                }
            }

        }

        for (PieceType piece : chessPiecesWhite.keySet()) {
            System.out.println(piece + ": " + chessPiecesWhite.get(piece));
        }

        //int differenceInPawns = 0;
        //int differenceInKnights = 0;
        //int differenceInBishops = 0;
        int differenceInRooks = 0;
        int differenceInQueens = 0;
        int flagWhite = 1;
        int flagBlack = 1;
        if(color == Color.WHITE){
            flagBlack = -1;
        }
        else {
            flagWhite = - 1;
        }

        differenceInRooks = flagWhite*chessPiecesWhite.get(PieceType.ROOK) - flagBlack*chessPiecesBlack.get(PieceType.ROOK);
        differenceInQueens = flagWhite*chessPiecesWhite.get(PieceType.QUEEN) - flagBlack*chessPiecesBlack.get(PieceType.QUEEN);

        if(differenceInQueens < 0){
            value += 0.5;
        }
        else{
            value += 1 + 0.5*differenceInQueens;
        }
        if(differenceInRooks < 0){
            value += 0.5;
        }
        else{
            value += 1 + 0.5*differenceInRooks;
        }

        return value;
    }
}
