package com.example.decoy.Chess.Pieces;

import com.example.decoy.Chess.Board.Square;
import com.example.decoy.Chess.Color;

import javax.swing.*;
import java.awt.*;



public abstract class PieceAbstract extends JLabel {

    private String path;
    private Color color;
    private ImageIcon image;
    public PieceType pieceType;//private
    Square actualSquare;//private
    Square previousSquare;
    private int oldX;
    private int oldY;
    private Integer xCoordinate;
    private Integer yCoordinate;

    Square newSquare;
    private boolean hasMoved;


    public PieceAbstract(Color color, PieceType pieceType, Square square){
        this.color = color;
        this.pieceType = pieceType;
        this.actualSquare = square;
        this.previousSquare = square;
        this.newSquare = square;
        this.path = pathBuilder(this.color);
        this.image = new ImageIcon(path);
        this.setIcon(image);
        this.hasMoved = false;

        this.setBounds(getX(), getY(), 64,64);

    }

    public void paintComponent(Graphics g){
        this.setBounds(getX(), getY(), 64,64);
        super.paintComponent(g);
        image.paintIcon(this, g, actualSquare.getX(), actualSquare.getY());
    }

    public boolean isValidMove() {
        return false;
    }

    public void setPreviousSquare(Square previousSquare) {
        this.previousSquare = previousSquare;
    }

    public Square getNewSquare() {
        return newSquare;
    }

    public Square getActualSquare() {
        return actualSquare;
    }

    public void pieceMoved(){
        this.hasMoved = true;
    }

    public boolean hasMoved(){
        return this.hasMoved;
    }

    public int getX(){
        return actualSquare.getX();
    }

    public int getY(){
        return actualSquare.getY();
    }

    public void setActualSquare(Square square){
        this.actualSquare = square;
    }

    public Color getColor(){
        return color;
    }

    public PieceType getPieceType(){
        return pieceType;
    }

    public Square getPreviousSquare(){
        return previousSquare;
    }

    public boolean isWhite(){
        return this.getColor() == Color.WHITE;
    }

    public boolean isBlack(){
        return this.getColor() == Color.BLACK;
    }


    public abstract void drawPath(Square startSquare, Square endSquare);

    public abstract void validPath(Square startSquare, Square endSquare);
    public abstract boolean isValidKill();


    private String pathBuilder(Color color){
        String path = "C:\\Users\\Windows\\Desktop\\Tree\\Studying\\Java Folders\\SIChess\\src\\main\\resources\\Icons\\";
        if(color == Color.WHITE){
            path += "White";
        }
        else{
            path += "Black";
        }
        if(pieceType == PieceType.BISHOP){
            path += "Bishop";
        }
        if(pieceType == PieceType.KING){
            path += "King";
        }
        if(pieceType == PieceType.KNIGHT){
            path += "Knight";
        }
        if(pieceType == PieceType.PAWN){
            path += "Pawn";
        }
        if(pieceType == PieceType.QUEEN){
            path += "Queen";
        }
        if(pieceType == PieceType.ROOK){
            path += "Rook";
        }

        path += ".png";
        return path;
    }

    // TODO: WHEN PIECE MOVED AROUND - REFILL OLD SQUARE, UPDATE(SQUARE newSquare)

    public abstract void move(Square square);
}

