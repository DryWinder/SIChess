package com.example.SIChess.Chess.Engine;

import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Color;
import com.example.SIChess.Chess.Pieces.PieceAbstract;
import com.example.SIChess.Chess.Pieces.PieceType;

import java.util.ArrayList;

public class MaterialEvaluator {
    private Color color;
    private ArrayList<PieceAbstract> pieces;
    private ArrayList<Square> squares;
    public MaterialEvaluator(ArrayList<PieceAbstract> pieces, ArrayList<Square> squares, Color color){
        this.pieces = pieces;
        this.squares = squares;
        this.color = color;
    }

    public int evaluateMaterialScore(){
        int overallScore = 0;
        for(PieceAbstract pieceAbstract : this.pieces){
            if(pieceAbstract.getColor() == color && pieceAbstract.getPieceType() != PieceType.KING){
                overallScore += pieceAbstract.getScore();
            }
        }
        return overallScore;
    }

}
