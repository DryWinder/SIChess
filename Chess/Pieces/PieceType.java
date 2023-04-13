package com.example.decoy.Chess.Pieces;

import com.example.decoy.Chess.Color;

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
