package com.example.SIChess.Chess.Board;


import com.example.SIChess.Chess.Color;
import com.example.SIChess.Chess.Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Board extends JFrame {
    private ArrayList<Square> squares;
    private ArrayList<PieceAbstract> pieces;

    private JLayeredPane layeredPane;

    private boolean whiteMoved = false;
    private boolean blackMoved = false;
    private boolean approveToMove = false;
    private PieceAbstract selectedPiece;

    private final static int xLocation = 50;//50
    private final static int yLocation = 50;

    private PieceAbstract pieceToBeKilled;


    public Board(){

        layeredPane = new JLayeredPane();
        //TODO: Pane size must be equal to chess board size
        layeredPane.setBounds(xLocation, yLocation, 576, 576);//576
        //layeredPane.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
        this.squares = new ArrayList<Square>();
        this.pieces = new ArrayList<PieceAbstract>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("Chess");
        //this.setUndecorated(true);5555
        this.setBounds(250, 50, 750, 750);
        this.add(layeredPane);

        this.setVisible(true);
        this.squares = createSquares();

        for (Square square : squares) {
            layeredPane.add(square, JLayeredPane.DEFAULT_LAYER);
        }

        drawPiecesInStartingPosition();

        for (PieceAbstract piece : pieces) {
            layeredPane.add(piece, JLayeredPane.DRAG_LAYER);
            System.out.println(piece.pieceType + " " + piece.getColor());
        }
    }

    public static int getXLocation() {
        return xLocation;
    }

    public static int getYLocation() {
        return yLocation;
    }

    private ArrayList<Square> createSquares() {
        int numberOfSquare = 1;
        for (int i = 1; i < 9; i++) {
            for (File file : File.values()) {
                    squares.add(new Square( numberOfSquare % 2 == 1 ? Color.WHITE : Color.BLACK , file, i));
                numberOfSquare++;
            }
            numberOfSquare--;
        }
        return squares;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public ArrayList<PieceAbstract> getPieces() {
        return pieces;
    }

    private boolean isSelectedPieceNull(PieceAbstract pieceSelected){
        return pieceSelected == null;
    }


    private boolean isThisFirstMove(){
        return selectedPiece.isBlack() && whiteMoved && blackMoved;
    }

    private boolean isThisWhitesTurnToMove(){
        return selectedPiece.isWhite() && !whiteMoved;
    }

    private boolean isThisBlacksTurnToMove(){
       return selectedPiece.isBlack() && !blackMoved && whiteMoved;
    }

    public void paintBoard(){

        layeredPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isSelectedPieceNull(selectedPiece)) return;
                if (approveToMove) {
                    selectedPiece.move(getSquareByCoordinates(e.getX(), e.getY()));
                    setNewSquareForPiece(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getX() + "  " + e.getY());
                Component c = layeredPane.findComponentAt(e.getX(), e.getY());
                if (c instanceof JPanel || c instanceof JLayeredPane){return;}
                selectedPiece = (PieceAbstract) c;

                System.out.println(selectedPiece.pieceType);
                System.out.println(selectedPiece.getColor());

                approveToMove = false;
                if (isThisFirstMove()) {return;}
                if (isThisWhitesTurnToMove()) {approveToMove = true;}
                if (isThisBlacksTurnToMove()) {approveToMove = true;}
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //TODO: Dodge java.lang.NullPointerException: Cannot invoke "com.example.decoy.Chess.Pieces.PieceAbstract.getColor()" because "this.piece" is null
                finalMove(selectedPiece.getNewSquare());
            }
        });
    }

    public void capturePiece(PieceAbstract piece) {
        piece.remove(piece);
    }


    //TODO: Solve the jumping over other pieces bug
    private boolean hasPieceJumpedOverSomePieces(){
        int numOfSquaresPassedByPiece = abs(selectedPiece.getNewSquare().getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue()) + abs(selectedPiece.getNewSquare().getRow() - selectedPiece.getPreviousSquare().getRow());
        System.out.println("Shit: " + numOfSquaresPassedByPiece);
        if(numOfSquaresPassedByPiece > 1){
            for (int squaresPassed = 1; squaresPassed < numOfSquaresPassedByPiece + 1; squaresPassed++){
                int flagX = 1;
                int flagY = 1;
                if(selectedPiece.getPreviousSquare().getX()/64 > selectedPiece.getNewSquare().getX()/64){
                    flagX = -1;
                }
                else if(selectedPiece.getPreviousSquare().getX()/64 < selectedPiece.getNewSquare().getX()/64){flagX=1;}
                else {
                    flagX = 0;
                }

                if(selectedPiece.getPreviousSquare().getY()/64 > selectedPiece.getNewSquare().getY()/64){
                    flagY = -1;
                }
                else if(selectedPiece.getPreviousSquare().getX()/64 < selectedPiece.getNewSquare().getX()/64){flagY=1;}
                else {
                    flagY = 0;
                }
                System.out.println("x:" + flagX + " y:" + flagY);
                System.out.println(selectedPiece.getNewSquare().getX() + flagX*64*squaresPassed + " " + (selectedPiece.getNewSquare().getY() + flagY*64*squaresPassed));
                Square passedSquare = getSquareByCoordinates(selectedPiece.getNewSquare().getX() + flagX*64*squaresPassed, selectedPiece.getNewSquare().getY() + flagY*64*squaresPassed);
                if(passedSquare.isTherePiece()){
                    return true;
                }
                else{return false;}
            }
        }
        return false;
    }


    public void finalMove(Square newSquare){
        if(newSquare.isTherePiece() && newSquare.getPieceColor() == selectedPiece.getColor()){
            Square previousSquare = selectedPiece.getPreviousSquare();
            selectedPiece.setLocation(previousSquare.getX(), previousSquare.getY());
            selectedPiece.setActualSquare(previousSquare);
        }
        if(!newSquare.isTherePiece()){
            System.out.println("No piece here");
            if(selectedPiece.isValidMove()){
                /*if(hasPieceJumpedOverSomePieces() && selectedPiece.pieceType != PieceType.KNIGHT){
                    System.out.println("Jumped over!");
                    Square previousSquare = selectedPiece.getPreviousSquare();
                    selectedPiece.setLocation(previousSquare.getX(), previousSquare.getY());
                    selectedPiece.setActualSquare(previousSquare);
                }*/
                System.out.println("No piece here, valid move");
                selectedPiece.setActualSquare(newSquare);
                selectedPiece.getActualSquare().nowHasPiece();
                newSquare.setPiece(selectedPiece);
                selectedPiece.getActualSquare().thePieceIsWhite(selectedPiece.isWhite());
                selectedPiece.getActualSquare().setTypeOfPiece(selectedPiece.getPieceType());
                selectedPiece.getPreviousSquare().nowThereIsNoPiece();
                selectedPiece.setPreviousSquare(newSquare);
                selectedPiece.pieceMoved();
                if (selectedPiece.isWhite()){
                    whiteMoved = true;
                    blackMoved = false;
                }
                if (selectedPiece.isBlack()) {
                    blackMoved = true;
                    whiteMoved = false;
                }
            }
            else{
                System.out.println("No piece there, not valid move");
                Square previousSquare = selectedPiece.getPreviousSquare();
                selectedPiece.setLocation(previousSquare.getX(), previousSquare.getY());
                selectedPiece.setActualSquare(previousSquare);
            }
        }
        // TODO: Kill()
        else{
            if(selectedPiece.isValidKill()){
                System.out.println("URA URA URA");
                pieceToBeKilled = newSquare.getPiece();
                newSquare.setPiece(selectedPiece);
                System.out.println(pieceToBeKilled.pieceType + " " + pieceToBeKilled.getColor());
                layeredPane.remove(pieceToBeKilled);
                pieces.remove(pieceToBeKilled);
                System.out.println(pieces.size());
                newSquare.setPiece(selectedPiece);
                selectedPiece.setActualSquare(newSquare);
                selectedPiece.getActualSquare().nowHasPiece();
                selectedPiece.getActualSquare().thePieceIsWhite(selectedPiece.isWhite());
                selectedPiece.getActualSquare().setTypeOfPiece(selectedPiece.getPieceType());
                selectedPiece.getPreviousSquare().nowThereIsNoPiece();
                selectedPiece.setPreviousSquare(newSquare);
                selectedPiece.pieceMoved();

                if (selectedPiece.isWhite()){
                    whiteMoved = true;
                    blackMoved = false;
                }
                if (selectedPiece.isBlack()) {
                    blackMoved = true;
                    whiteMoved = false;
                }

            }
            else{
                Square previousSquare = selectedPiece.getPreviousSquare();
                selectedPiece.setLocation(previousSquare.getX(), previousSquare.getY());
                selectedPiece.setActualSquare(previousSquare);
            }
        }

    }

    private boolean isCursorOnASquare(int xCursorCoordinate, int yCursorCoordinate, Square square){
        return ((int) (xCursorCoordinate / 64) == square.getFile().getValue() && (int) (yCursorCoordinate / 64) == square.getRow());
    }
    public void setNewSquareForPiece(int x, int y) {
        boolean isTherePiece = false;
        for (Square square : squares) {
            if (isCursorOnASquare(x,y,square)) {
                selectedPiece.setActualSquare(square);
                //selectedPiece.getNewSquare().nowHasPiece();
            }
        }
    }

    private void getPieceBySquare(Square square){
        for (PieceAbstract piece : pieces){
            if(piece.getActualSquare().equals(square)){
                pieceToBeKilled = piece;
            }
        }
    }

    public Square getSquareByCoordinates(int x, int y) {
        for (Square square : squares) {
            if (isCursorOnASquare(x,y,square)) {
                return square;
            }
        }
        return null;
    }

    public void drawPieces(Color color, Square square){
        if (square.getRow() == 2 || square.getRow() == 7) {
            Pawn pawn = new Pawn(color, PieceType.PAWN, square);
            System.out.println(pawn.pieceType + " " + pawn.getColor());
            pieces.add(pawn);
            square.nowHasPiece();
        }

        if (square.getRow() == 1 || square.getRow() == 8) {
            if (square.getFile().getValue() == 1 || square.getFile().getValue() == 8) {
                Rook rook = new Rook(color, PieceType.ROOK, square);
                pieces.add(rook);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 2 || square.getFile().getValue() == 7) {
                Knight knight = new Knight(color, PieceType.KNIGHT, square);
                pieces.add(knight);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 3 || square.getFile().getValue() == 6) {
                Bishop bishop = new Bishop(color, PieceType.BISHOP, square);
                pieces.add(bishop);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 4) {
                Queen queen = new Queen(color, PieceType.QUEEN, square);
                pieces.add(queen);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 5) {
                King king = new King(color, PieceType.KING, square);
                pieces.add(king);
                square.nowHasPiece();
            }
        }
    }

    public void drawPiecesInStartingPosition(){
        ArrayList<Square> squares = getSquares();
        for (Square square : squares) {
            if (square.getRow() <= 4) {
                drawPieces(Color.BLACK, square);
            } else {
                drawPieces(Color.WHITE, square);
            }
        }
    }
}



