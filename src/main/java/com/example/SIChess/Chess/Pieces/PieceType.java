package com.example.SIChess.Chess.Pieces;

import com.example.SIChess.Chess.Color;

public enum PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, NONE;
    private Color color;

    private PieceType(Color color){
        this.color = color;
    }

    private PieceType(){}

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

}
