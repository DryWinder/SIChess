package com.example.SIChess.Chess.Board;


import com.example.SIChess.Chess.Color;
import com.example.SIChess.Chess.Pieces.PieceAbstract;
import com.example.SIChess.Chess.Pieces.PieceType;

import javax.swing.*;

public class Square extends JPanel {
    private Color color;
    private File file;
    private int row;

    private boolean isTherePiece = false;

    private boolean isPieceWhite;
    private PieceType typeOfPiece;

    private PieceAbstract piece;

    public Square(Color color, File file, int row){
        this.color = color;
        this.file = file;
        this.row = row;
        if (getColor() == Color.WHITE) {
            this.setBackground(new java.awt.Color(235, 235, 208));
        } else {
            this.setBackground(new java.awt.Color(119, 148, 85));
        }
        this.setBounds(getX(), getY(), 64,64);
    }



    public Square(){}

    public Color getColor(){return color;}

    public File getFile(){return file;}

    public int getRow(){return row;}

    public  int getX(){
        return (getFile().getValue()-1)*64 + Board.getXLocation();
    }
    public  int getY(){ return (getRow()-1)*64 + Board.getYLocation();}

    public boolean isTherePiece(){
        return isTherePiece;
    }

    public PieceAbstract getPiece(){return piece;}

    public Color getPieceColor(){return  piece.getColor();}

    public void setPiece(PieceAbstract piece){
        this.piece = piece;
    }

    public void nowHasPiece(){
        isTherePiece = true;
    }

    public void nowThereIsNoPiece(){
        isTherePiece = false;
    }

    public boolean isPieceWhite(){return this.isPieceWhite;}

    public void thePieceIsWhite(boolean isPieceWhite){this.isPieceWhite = isPieceWhite;}

    public PieceType getTypeOfPiece(){return typeOfPiece;}

    public void setTypeOfPiece(PieceType pieceType){this.typeOfPiece = pieceType;}


    public void setX(int x){
        for(File file : File.values()){
            if((int)((x/64)) == file.getValue()){
                this.file = file;
            }
        }
    }

    public void setY(int y){
        this.row = (int)((y)/64);
    }


}

