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

    private int numberOfWhiteMoves = 0;
    private int numberOfBlackMoves = 0;

    private Square enPassantSquare;

    ArrayList<Square> validPath;

    private boolean isCheck = false;

    private ArrayList<PieceAbstract> piecesThatCheck;


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
                System.out.println("Possible moves for " + selectedPiece.getPieceType());
                validPath = validPath(selectedPiece);
                for (Square square : validPath){
                    System.out.println(square.getFile() + " " + square.getRow());
                    square.setNewColor();
                }
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
                System.out.println(selectedPiece.getActualSquare().getFile() + " " + selectedPiece.getActualSquare().getRow());

                approveToMove = false;
                if (isThisFirstMove()) {return;}
                if (isThisWhitesTurnToMove()) {approveToMove = true;}
                if (isThisBlacksTurnToMove()) {approveToMove = true;}

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //TODO: Dodge java.lang.NullPointerException: Cannot invoke "com.example.decoy.Chess.Pieces.PieceAbstract.getColor()" because "this.piece" is null
                finalMove(selectedPiece.getNewSquare());
                System.out.println("final move");
                System.out.println("Possible moves for " + selectedPiece.getPieceType());
                if (validPath != null){
                    for (Square square : validPath){
                        System.out.println(square.getFile() + " " + square.getRow());
                        square.setOriginalColor();
                    }
                    selectedPiece.getActualSquare().setOriginalColor();
                }

                validPath(selectedPiece);

                System.out.println("Check = " + isCheck);

                selectedPiece = null;

            }
        });
    }

    public void capturePiece(PieceAbstract piece) {
        piece.remove(piece);
    }


    //TODO: Solve the jumping over other pieces bug
    private boolean hasPieceJumpedOverSomePieces(){
        int numOfSquaresPassedByPiece = abs(selectedPiece.getNewSquare().getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue()) + abs(selectedPiece.getNewSquare().getRow() - selectedPiece.getPreviousSquare().getRow());
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

    private boolean isValidPathUpAndDown(PieceAbstract selectedPiece){
        boolean isValid = true;
        int rowLimit = abs(selectedPiece.getNewSquare().getRow() - selectedPiece.getPreviousSquare().getRow());
        int clone = rowLimit;
        boolean flag = true;
        if(selectedPiece.getNewSquare().getRow() >= selectedPiece.getPreviousSquare().getRow()){
            flag = false;
        }
        File file = selectedPiece.getNewSquare().getFile();
        while (rowLimit != 0) {
            for(Square square : this.squares){
                int diff = 0;
                if (flag){
                    diff = selectedPiece.getPreviousSquare().getRow() - rowLimit;
                }
                else {
                    diff = selectedPiece.getPreviousSquare().getRow() + rowLimit;
                }
                if(square.getFile() == file && square.getRow() == diff){
                    if(square.getPiece() != null) {
                        isValid = false;
                    }
                    if(square.getPiece() != null && rowLimit == clone && selectedPiece.isValidKill()){
                        isValid = true;
                    }
                }
            }
            rowLimit = rowLimit - 1;
        }
        return isValid;
    }

    private boolean isValidPathLeftAndRight(PieceAbstract selectedPiece){
        boolean isValid = true;
        int fileLimit = abs(selectedPiece.getNewSquare().getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue());
        int clone = fileLimit;
        boolean flag = true;
        if(selectedPiece.getNewSquare().getFile().getValue() >= selectedPiece.getPreviousSquare().getFile().getValue()){
            flag = false;
        }
        int row = selectedPiece.getNewSquare().getRow();
        while (fileLimit != 0) {
            for(Square square : this.squares){
                int diff = 0;
                if (flag){
                    diff = selectedPiece.getPreviousSquare().getFile().getValue() - fileLimit;
                }
                else {
                    diff = selectedPiece.getPreviousSquare().getFile().getValue() + fileLimit;
                }
                if(square.getRow() == row && square.getFile().getValue() == diff){
                    if(square.getPiece() != null) {
                        isValid = false;
                    }
                    if(square.getPiece() != null && fileLimit == clone){
                        if(selectedPiece.getPieceType() !=  PieceType.KING && selectedPiece.isValidKill()){
                            isValid = true;
                        }
                        if(selectedPiece.getPieceType() == PieceType.KING && square.getPiece().getPieceType() == PieceType.ROOK && selectedPiece.getColor() == square.getPiece().getColor()){
                            isValid = true;
                        }
                    }
                }
            }
            fileLimit = fileLimit - 1;
        }
        return isValid;

    }

    private boolean isValidPathDiagonal(PieceAbstract selectedPiece){
        boolean isValid = true;
        int fileLimit = abs(selectedPiece.getNewSquare().getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue());
        int rowLimit = abs(selectedPiece.getNewSquare().getRow() - selectedPiece.getPreviousSquare().getRow());
        int clone = fileLimit;
        boolean flag = true;
        int coeffRow = 1;
        int coeffFile = 1;
        if(selectedPiece.getNewSquare().getRow() >= selectedPiece.getPreviousSquare().getRow()){
            coeffRow = -1;
        }
        if(selectedPiece.getNewSquare().getFile().getValue() >= selectedPiece.getPreviousSquare().getFile().getValue()){
            coeffFile = -1;
        }
        while (rowLimit != 0 && fileLimit != 0) {
            int diffRow = selectedPiece.getPreviousSquare().getRow() - coeffRow*rowLimit;
            int diffFile = selectedPiece.getPreviousSquare().getFile().getValue() - coeffFile*fileLimit;
            for(Square square : this.squares){
                if(square.getRow() == diffRow && square.getFile().getValue() == diffFile){
                    if(square.getPiece() != null) {
                        isValid = false;
                    }
                    if(square.getPiece() != null && fileLimit == clone && selectedPiece.isValidKill()){
                        isValid = true;
                    }
                }
            }
            rowLimit = rowLimit - 1;
            fileLimit = fileLimit - 1;
        }
        return isValid;
    }
    public boolean isValidPath(PieceAbstract selectedPiece){
        boolean isValid = true;
        if (selectedPiece.pieceType != PieceType.KNIGHT && selectedPiece.pieceType != PieceType.PAWN && selectedPiece.getPieceType() != PieceType.KING) {
            int fileLimit = abs(selectedPiece.getNewSquare().getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue());
            int rowLimit = abs(selectedPiece.getNewSquare().getRow() - selectedPiece.getPreviousSquare().getRow());
            if (fileLimit == 0) {
                isValid = isValidPathUpAndDown(selectedPiece);
            }
            if (rowLimit == 0){
                isValid = isValidPathLeftAndRight(selectedPiece);
            }

            if (fileLimit == rowLimit && rowLimit > 0){
                isValid = isValidPathDiagonal(selectedPiece);
            }
        }
        return isValid;
    }

    public boolean isEnPassant(PieceAbstract selectedPiece){
        if(selectedPiece.pieceType == PieceType.PAWN){
            for (Square square : this.squares){
                if(selectedPiece.getPreviousSquare().getRow() == square.getRow() && (selectedPiece.getPreviousSquare().getFile().getValue() == square.getFile().getValue() - 1 || selectedPiece.getPreviousSquare().getFile().getValue() == square.getFile().getValue() + 1)){
                    if(square.getPiece() != null){
                        if(square.getPiece().getPieceType() == PieceType.PAWN && square.getPiece().getColor() != selectedPiece.getColor() && square.getPiece().getPawnsMoveHistory()){
                            if(square.getPiece().getColor() == Color.WHITE && square.getPiece().lastTimeMoved == this.numberOfWhiteMoves){
                                this.enPassantSquare = square;
                                return true;
                            }
                            if(square.getPiece().getColor() == Color.BLACK && square.getPiece().lastTimeMoved == this.numberOfBlackMoves){
                                this.enPassantSquare = square;
                                return true;
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    public Square enPassant(PieceAbstract selectedPiece){
        for(Square square : this.squares){
            if(square.getFile().getValue() == this.enPassantSquare.getFile().getValue()){
                if(this.enPassantSquare.getPiece().getColor() == Color.BLACK && square.getRow() == this.enPassantSquare.getRow() - 1){
                    return square;
                }
                if(this.enPassantSquare.getPiece().getColor() == Color.WHITE && square.getRow() == this.enPassantSquare.getRow() + 1){
                    return square;
                }
            }
        }
        return null;
    }

    public boolean isCastle(PieceAbstract selectedPiece, Square newSquare){
        boolean isCastle = false;
        if(newSquare.getPiece() != null){
            if(selectedPiece.getPieceType() == PieceType.KING && newSquare.getPiece().getPieceType() == PieceType.ROOK){
                System.out.println("king Rook " + selectedPiece.hasMoved() + " " + newSquare.getPiece().hasMoved() + " xd " + isValidPath(selectedPiece));
                if(isValidPath(selectedPiece) && !selectedPiece.hasMoved() && !newSquare.getPiece().hasMoved()){
                    isCastle = true;
                }
            }
        }
        return isCastle;
    }

    private void castle(PieceAbstract selectedPiece, Square rookSquare){
        int fileLimit = abs(rookSquare.getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue());
        int fileFlag = selectedPiece.getPreviousSquare().getFile().getValue();
        int rowFlag = selectedPiece.getPreviousSquare().getRow();
        System.out.println(fileLimit);
        Square square1 = null;
        for(Square square : this.squares){
            if(fileLimit == 3) {
                if (square.getRow() == rowFlag) {
                    System.out.println("Here" + rowFlag);
                    if (square.getFile().getValue() == 7 && square1 == null) {
                        System.out.println(square.getFile().getValue());
                        square1 = square;
                    }
                    if (square.getFile().getValue() == fileFlag + 1) {
                        move(square, rookSquare.getPiece());
                        square.setPiece(rookSquare.getPiece());
                        square.nowHasPiece();
                    }
                }
            }
            if(fileLimit == 4){
                if (square.getRow() == rowFlag) {
                    if (square.getFile().getValue() == fileFlag - 2) {
                        move(square, selectedPiece);
                        square.setPiece(selectedPiece);
                        square.nowHasPiece();
                    }
                    if (square.getFile().getValue() == fileFlag - 1) {
                        move(square, rookSquare.getPiece());
                        square.setPiece(rookSquare.getPiece());
                        square.nowHasPiece();
                    }
                }
            }
        }

        if (selectedPiece.isWhite()){
            this.numberOfWhiteMoves -= 1;
        }
        if (selectedPiece.isBlack()) {
            this.numberOfBlackMoves -= 1;
        }
        move(square1, selectedPiece);
        square1.nowHasPiece();

    }


    public void kill(Square newSquare, PieceAbstract selectedPiece){
        pieceToBeKilled = newSquare.getPiece();
        //newSquare.setPiece(selectedPiece);
        layeredPane.remove(pieceToBeKilled);
        pieces.remove(pieceToBeKilled);

    }

    public void move(Square newSquare, PieceAbstract selectedPiece){
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
            this.numberOfWhiteMoves += 1;
        }
        if (selectedPiece.isBlack()) {
            blackMoved = true;
            whiteMoved = false;
            this.numberOfBlackMoves += 1;
        }
    }

    public void setCountOfMoves(PieceAbstract selectedPiece){
        if(selectedPiece.getColor() == Color.WHITE){
            selectedPiece.lastTimeMoved = this.numberOfWhiteMoves;
        }
        else{
            selectedPiece.lastTimeMoved = this.numberOfBlackMoves;
        }
    }

    private void returnToPreviousSquare(PieceAbstract selectedPiece){
        Square previousSquare = selectedPiece.getPreviousSquare();
        selectedPiece.setLocation(previousSquare.getX(), previousSquare.getY());
        selectedPiece.setActualSquare(previousSquare);
    }

    public void finalMove(Square newSquare){
        //TODO: IF CHECK == TRUE
        if(selectedPiece.getPieceType() == PieceType.KING && isCastle(selectedPiece, newSquare)){
            System.out.println("Castle " + isCastle(selectedPiece, newSquare));
            setCountOfMoves(selectedPiece);
            setCountOfMoves(newSquare.getPiece());
            castle(selectedPiece, newSquare);
            return;
        }

        if(newSquare.isTherePiece()){
            if(newSquare.getPiece().getColor() == selectedPiece.getColor()){
                returnToPreviousSquare(selectedPiece);
                return;
            }

            else{
                if(selectedPiece.isValidKill() && isValidPath(selectedPiece)){
                    kill(newSquare, selectedPiece);
                    move(newSquare, selectedPiece);
                    setCountOfMoves(selectedPiece);
                }
                else{
                    returnToPreviousSquare(selectedPiece);
                }
            }
        }


        if(!newSquare.isTherePiece()){
            if(selectedPiece.pieceType == PieceType.PAWN && isEnPassant(selectedPiece)){
                if (newSquare == enPassant(selectedPiece)){
                    for(Square square : this.squares) {
                        if (square.getFile().getValue() == this.enPassantSquare.getFile().getValue() && square.getRow() == this.enPassantSquare.getRow()) {
                            kill(square, selectedPiece);
                            square.nowThereIsNoPiece();
                        }
                    }
                    move(newSquare, selectedPiece);
                    setCountOfMoves(selectedPiece);
                }
            }
            System.out.println("No piece here");
            if(!isValidPath(selectedPiece)){
                System.out.println("No piece there, not valid move");
                returnToPreviousSquare(selectedPiece);
            }
            if(selectedPiece.isValidMove() && isValidPath(selectedPiece)){
                move(newSquare, selectedPiece);
                setCountOfMoves(selectedPiece);
            }
            else{
                System.out.println("No piece there, not valid move");
                returnToPreviousSquare(selectedPiece);
            }
        }
        // TODO: Kill()

        System.out.println("Count of White moves: " + this.numberOfWhiteMoves);
        System.out.println("Count of Black moves: " + this.numberOfBlackMoves);
    }

    public boolean checkValidation(Square square, PieceAbstract selectedPiece){
        boolean check = false;
        if(square.getPiece() != null){
            if(square.getPiece().getPieceType() == PieceType.KING && square.getPiece().getColor() != selectedPiece.getColor()){
                check = true;
            }
        }
        return check;
    }


    public ArrayList<Square> validPath(PieceAbstract selectedPiece){
        int flag = 0;
        ArrayList<Square> possibleSquareToMoveIn = new ArrayList<>();
        Square defaultNewSquare = selectedPiece.getNewSquare();

        for (Square square : this.squares){
            selectedPiece.setNewSquare(square);
            if(isEnPassant(selectedPiece)){
                if(square.getFile().getValue() == this.enPassantSquare.getFile().getValue()){
                    if(this.enPassantSquare.getPiece().getColor() == Color.BLACK && square.getRow() == this.enPassantSquare.getRow() - 1){
                        possibleSquareToMoveIn.add(square);
                        if(checkValidation(square, selectedPiece)){
                            flag += 1;
                            piecesThatCheck.add(selectedPiece);
                        }

                    }
                    if(this.enPassantSquare.getPiece().getColor() == Color.WHITE && square.getRow() == this.enPassantSquare.getRow() + 1){
                        possibleSquareToMoveIn.add(square);
                        if(checkValidation(square, selectedPiece)){
                            flag += 1;
                            piecesThatCheck.add(selectedPiece);
                        }
                    }
                }
            }
            if(selectedPiece.isValidMove() && selectedPiece.getActualSquare() != square){
                if(isValidPath(selectedPiece)){
                    if(selectedPiece.isValidKill()){
                        possibleSquareToMoveIn.add(square);
                        if(checkValidation(square, selectedPiece)){
                            flag += 1;
                            piecesThatCheck.add(selectedPiece);
                        }
                    }
                    if(!square.isTherePiece()){
                        possibleSquareToMoveIn.add(square);
                        if(checkValidation(square, selectedPiece)){
                            flag += 1;
                            piecesThatCheck.add(selectedPiece);
                        }
                    }
                }
            }
            if(selectedPiece.getPieceType() == PieceType.PAWN && selectedPiece.getActualSquare() != square && selectedPiece.isValidKill()){
                possibleSquareToMoveIn.add(square);
                if(checkValidation(square, selectedPiece)){
                    flag += 1;
                    piecesThatCheck.add(selectedPiece);
                }
            }
        }
        if(flag == 0){
            isCheck = false;
        }
        else{
            isCheck = true;
        }
        selectedPiece.setNewSquare(defaultNewSquare);
        return possibleSquareToMoveIn;
    }

    private boolean isCheck(){
        return false;
    }

    private boolean isCursorOnASquare(int xCursorCoordinate, int yCursorCoordinate, Square square){
        return ((int) (xCursorCoordinate / 64) == square.getFile().getValue() && (int) (yCursorCoordinate / 64) == square.getRow());
    }
    public void setNewSquareForPiece(int x, int y) {
        boolean isTherePiece = false;
        for (Square square : squares) {
            if (isCursorOnASquare(x,y,square)) {
                selectedPiece.setActualSquare(square);
                //square.setPiece(selectedPiece);
                //selectedPiece.getPreviousSquare().nowThereIsNoPiece();
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
            square.setPiece(pawn);
            square.nowHasPiece();
        }

        if (square.getRow() == 1 || square.getRow() == 8) {
            if (square.getFile().getValue() == 1 || square.getFile().getValue() == 8) {
                Rook rook = new Rook(color, PieceType.ROOK, square);
                pieces.add(rook);
                square.setPiece(rook);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 2 || square.getFile().getValue() == 7) {
                Knight knight = new Knight(color, PieceType.KNIGHT, square);
                pieces.add(knight);
                square.setPiece(knight);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 3 || square.getFile().getValue() == 6) {
                Bishop bishop = new Bishop(color, PieceType.BISHOP, square);
                pieces.add(bishop);
                square.setPiece(bishop);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 4) {
                Queen queen = new Queen(color, PieceType.QUEEN, square);
                pieces.add(queen);
                square.setPiece(queen);
                square.nowHasPiece();
            }

            if (square.getFile().getValue() == 5) {
                King king = new King(color, PieceType.KING, square);
                pieces.add(king);
                square.setPiece(king);
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



