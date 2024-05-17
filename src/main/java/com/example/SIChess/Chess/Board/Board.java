package com.example.SIChess.Chess.Board;


import com.example.SIChess.Chess.Color;
import com.example.SIChess.Chess.Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.abs;

public class Board extends JFrame {
    private static ArrayList<Square> squares;
    private ArrayList<PieceAbstract> pieces;

    private JLayeredPane layeredPane;

    private boolean whiteMoved = false;
    private boolean blackMoved = false;
    private boolean approveToMove = false;
    private PieceAbstract selectedPiece;
    private PieceAbstract selectedPieceCopy;

    private final static int xLocation = 50;//50
    private final static int yLocation = 50;

    private PieceAbstract pieceToBeKilled;

    private int numberOfWhiteMoves = 0;
    private int numberOfBlackMoves = 0;

    private Square enPassantSquare;

    ArrayList<Square> validPath;
    ArrayList<Square> checkPath;

    private boolean isCheck = false;

    private ArrayList<PieceAbstract> piecesThatCheck;

    private JLabel player1Label;
    private JLabel player2Label;
    private int xCoordinateToDrawTakenPiecePlayer2 = 140;
    private int xCoordinateToDrawTakenPiecePlayer1 = 140;
    private int yCoordinateToDrawTakenPiecePlayer2 = 10;
    private int yCoordinateToDrawTakenPiecePlayer1 = 565;

    private Square squareThatIsChecked;
    private PieceAbstract checkerPiece;
    private ArrayList<Square> squaresThatGuardKing;
    private boolean doesCheckRemains = false;
    private Square whiteKingsSquare;
    private Square blackKingsSquare;

    private boolean isCheckedWhite = false;
    private boolean isCheckedBlack = false;

    private int numberOfAttackers = 0;
    private int willNumberOfAttackersBeIncreased = 0;
    private PieceAbstract helperPiece = null;
    private PieceAbstract possibleAttacker = null;
    private ArrayList<Square> resultArray;
    private ArrayList<Square> castleSquaresArray = new ArrayList<>();
    private PieceAbstract newPiece = selectedPiece;
    private int validMovesUnderCheck = 0;

    public Board(){

        layeredPane = new JLayeredPane();
        //TODO: Pane size must be equal to chess board size
        layeredPane.setBounds(xLocation, yLocation, 576, 600);//576
        //layeredPane.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
        this.squares = new ArrayList<Square>();
        this.pieces = new ArrayList<PieceAbstract>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("Chess");
        //this.setUndecorated(true);5555
        this.setBounds(250, 50, 750, 750);
        this.add(layeredPane);

        player2Label = new JLabel("Player 2");
        player2Label.setBounds(50, 10, 100, 40); // Adjust the location and size as needed
        player1Label = new JLabel("Player 1");
        player1Label.setBounds(50, 560, 100, 40); // Adjust the location and size as needed
        layeredPane.add(player2Label);
        layeredPane.add(player1Label);

        // Load icons for taken pieces
        ImageIcon takenPieceIcon2 = new ImageIcon("C:\\Users\\Windows\\Desktop\\Tree\\Studying\\Java Folders\\SIChess\\src\\main\\resources\\Icons\\WhiteBishop.png"); // Adjust the path as needed
        ImageIcon takenPieceIcon1 = new ImageIcon("C:\\Users\\Windows\\Desktop\\Tree\\Studying\\Java Folders\\SIChess\\src\\main\\resources\\Icons\\BlackBishop.png"); // Adjust the path as needed

        // Scale the icons to desired size
        Image scaledIcon2 = takenPieceIcon1.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Image scaledIcon1 = takenPieceIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        takenPieceIcon2 = new ImageIcon(scaledIcon1);
        takenPieceIcon1 = new ImageIcon(scaledIcon2);

        // Set icons for the labels
        player1Label.setIcon(takenPieceIcon2);
        player2Label.setIcon(takenPieceIcon1);

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

    public static ArrayList<Square> getSquares() {
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
                //System.out.println("Possible moves for " + selectedPiece.getPieceType());
                /*if(isCheck == true){
                    for(Square squareThatGuardsKing : squareToGuardTheKing(checkPath)) {
                        if (squareThatGuardsKing != null) {
                            //squareThatGuardsKing.setNewColor(java.awt.Color.PINK);
                        }
                    }
                }*/

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
                determineKingSquares();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getX() + "  " + e.getY());
                Component c = layeredPane.findComponentAt(e.getX(), e.getY());
                if (c instanceof JPanel || c instanceof JLayeredPane){return;}
                selectedPiece = (PieceAbstract) c;

                selectedPieceCopy = selectedPiece;

                validPath = validPath(selectedPiece);
                setGreenColorForValidPath();

                setColorForPathUnderCheck();


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
                //System.out.println("willTheKingBeUnderAttack: " + willTheKingBeUnderAttack(selectedPiece, selectedPiece.getNewSquare()));
                int count = 0;
                for (PieceAbstract piece : pieces){
                    if (isThereCheck2(validPath(piece), piece.getColor(), 1, piece)){
                        piecesThatCheck.add(piece);
                        count += 1;
                    }
                }
                if(numberOfAttackers == 1){
                    for(PieceAbstract piece : piecesThatCheck){
                        checkerPiece = piece;
                    }
                }
                System.out.println("ISTHERECHECK = " + count);
                if(count > 0){
                    if(xd(count).size() == 0) {
                        System.out.println("CHECKMATE!!!");
                        dispose();
                    }
                }
                numberOfAttackers = count;
                System.out.println("FUCK " + isThereCheck(validPath(selectedPiece)));

                int finalMove = finalMove(selectedPiece.getNewSquare());
                System.out.println("final move");
                System.out.println("Possible moves for " + selectedPiece.getPieceType());
                if (validPath != null){
                    for (Square square : validPath){
                        System.out.println(square.getFile() + " " + square.getRow());
                        //square.setOriginalColor();
                    }
                    //selectedPiece.getActualSquare().setOriginalColor();
                }

                int newCount = 0;
                for (PieceAbstract piece : pieces){
                    if (isThereCheck2(validPath(piece), piece.getColor(), 1, piece)){
                        System.out.println("Attacker: " + piece.getPieceType());
                        piecesThatCheck.add(piece);
                        newCount += 1;
                    }
                }

                /*if(newCount > 0){
                    returnToPreviousSquare1(selectedPiece, selectedPieceCopy.getPreviousSquare());
                }*/
                System.out.println("ISTHERECHECK = " + newCount);
                numberOfAttackers = newCount;
                if(newCount > 0){
                    isCheck = true;
                }
                System.out.println(numberOfAttackers);
                if(numberOfAttackers == 1){
                    for(PieceAbstract piece : piecesThatCheck){
                        System.out.println(piece);
                        checkerPiece = piece;
                    }
                }




                for(Square square : squares){
                    if(newCount > 0){
                        if(square != whiteKingsSquare && square != blackKingsSquare){
                            square.setOriginalColor();
                        }
                    }
                    else{
                        square.setOriginalColor();
                    }
                }

                boolean checkMate = isThereCheckMate();
                System.out.println("CheckMate: " + checkMate);
                if(newCount > 0 && checkMate){
                    //dispose();
                }
                //System.out.println("XD: " + xd(newCount).size());


                validMovesUnderCheck = 0;
                piecesThatCheck = new ArrayList<PieceAbstract>();

                selectedPiece = null;
            }
        });
    }

    private void determineKingSquares(){
        for(PieceAbstract piece : pieces){
            if(piece.getPieceType() == PieceType.KING){
                if(piece.getColor() == Color.WHITE){
                    whiteKingsSquare = piece.getActualSquare();
                }
                if(piece.getColor() == Color.BLACK){
                    blackKingsSquare = piece.getActualSquare();
                }
            }
        }
    }

    private void setGreenColorForValidPath(){
        for (Square square : validPath){
            System.out.println("Press " + square.getFile() + " " + square.getRow());
            square.setNewColor(java.awt.Color.green);
        }
    }

    private void setColorForPathUnderCheck(){
        ArrayList<Square> resultArray = new ArrayList<>();
        if(checkerPiece!=null){
            resultArray = validPathUnderCheck(selectedPiece);
        }
        java.awt.Color colorForSquare = java.awt.Color.green;
        for (Square square : validPath) {
            //System.out.println("Press " + square.getFile() + " " + square.getRow());
            if(numberOfAttackers != 0 && resultArray != null) {
                if (resultArray.contains(square)) {
                    //square.setNewColor(java.awt.Color.green);
                    colorForSquare = java.awt.Color.green;
                } else {
                    colorForSquare = java.awt.Color.gray;
                }
            }
            else{
                System.out.println("Press " + square.getFile() + " " + square.getRow());
                colorForSquare = java.awt.Color.green;
            }
            if(colorForSquare == java.awt.Color.gray){
                square.setOriginalColor();
            }
            else{
                //square.setNewColor(colorForSquare);
            }
        }
    }

    public ArrayList<Square> validPath(PieceAbstract selectedPiece){
        ArrayList<Square> possibleSquareToMoveIn = new ArrayList<>();
        Square defaultNewSquare = selectedPiece.getNewSquare();

        for (Square square : this.squares){
            selectedPiece.setNewSquare(square);
            if(isEnPassant(selectedPiece)){
                if(square.getFile().getValue() == this.enPassantSquare.getFile().getValue()){
                    if(this.enPassantSquare.getPiece().getColor() == Color.BLACK && square.getRow() == this.enPassantSquare.getRow() - 1){
                        possibleSquareToMoveIn.add(square);
                    }
                    if(this.enPassantSquare.getPiece().getColor() == Color.WHITE && square.getRow() == this.enPassantSquare.getRow() + 1){
                        possibleSquareToMoveIn.add(square);
                    }
                }
            }
            if(selectedPiece.isValidMove() && selectedPiece.getActualSquare() != square){
                if(isValidPath(selectedPiece)){
                    if(square.isTherePiece() && selectedPiece.isValidKill()){
                        possibleSquareToMoveIn.add(square);
                    }
                    if(!square.isTherePiece()){
                        possibleSquareToMoveIn.add(square);
                    }
                }
            }
            if(selectedPiece.getPieceType() == PieceType.PAWN && selectedPiece.getActualSquare() != square && selectedPiece.isValidKill()){
                possibleSquareToMoveIn.add(square);
            }
        }

        selectedPiece.setNewSquare(defaultNewSquare);
        return possibleSquareToMoveIn;
    }

    public boolean isThereCheck(ArrayList<Square> validPath){
        boolean check = false;
        for(Square square : validPath){
            if(square.getPiece() != null){
                if(square.getPiece().getPieceType() == PieceType.KING && square.getPiece().getColor() != selectedPiece.getColor()){
                    square.setNewColor(java.awt.Color.RED);
                    this.squareThatIsChecked = square;
                    check = true;
                }
            }
        }

        return check;
    }

    public boolean isThereCheck2(ArrayList<Square> validPath1, Color color, int mode, PieceAbstract piece) {
        boolean check = false;
        boolean checkWhite = false;
        boolean checkBlack = false;
        if(piece.getPieceType() == PieceType.PAWN){
            ArrayList<Square> validPathForPawn = new ArrayList<>();
            Square pawnNewSquare = piece.getNewSquare();
            for(Square square : this.squares){
                piece.setNewSquare(square);
                if(piece.getColor() == Color.WHITE){
                    if(piece.getPreviousSquare().getRow() > piece.getNewSquare().getRow() && abs(piece.getNewSquare().getFile().getValue() - piece.getPreviousSquare().getFile().getValue()) == 1 && piece.getPreviousSquare().getRow() - piece.getNewSquare().getRow() == 1 ){
                        validPathForPawn.add(square);
                    }
                }
                if(piece.getColor() == Color.BLACK){
                    if(piece.getPreviousSquare().getRow() < piece.getNewSquare().getRow() && abs(piece.getNewSquare().getFile().getValue() - piece.getPreviousSquare().getFile().getValue()) == 1 && piece.getNewSquare().getRow() - piece.getPreviousSquare().getRow() == 1 ){
                        validPathForPawn.add(square);
                    }
                }
            }
            validPath1 = validPathForPawn;
            piece.setNewSquare(pawnNewSquare);
        }
        for (Square square : validPath1) {
            if(color == Color.BLACK){
                if (square.getFile() == whiteKingsSquare.getFile() && square.getRow() == whiteKingsSquare.getRow()) {
                    if(mode == 1){
                        square.setNewColor(java.awt.Color.yellow);
                        this.squareThatIsChecked = square;
                    }
                    checkWhite = true;
                    check = true;

                }

            }

            if(color == Color.WHITE){
                if (square.getFile() == blackKingsSquare.getFile() && square.getRow() == blackKingsSquare.getRow()) {
                    if(mode == 1) {
                        square.setNewColor(java.awt.Color.yellow);
                        this.squareThatIsChecked = square;
                    }
                    checkBlack = true;
                    check = true;
                }

            }
        }
        if(mode == 1) {
            this.isCheckedWhite = checkWhite;
            this.isCheckedBlack = checkBlack;
        }
        return check;
    }


    public ArrayList<PieceAbstract> getPiecesThatCheck(Color color){
        ArrayList<PieceAbstract> piecesThatCheck = new ArrayList<>();
        for(PieceAbstract piece : this.pieces){
            for(Square square : validPath(piece)){
                if(square.getPiece() != null){
                    if(square.getPiece().getPieceType() == PieceType.KING && square.getPiece().getColor() != color){
                        System.out.println("PISSSS " + piece.getPieceType() + piece.getColor());
                        piecesThatCheck.add(piece);
                    }
                }
            }
        }
        return piecesThatCheck;
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

    // TODO:.......................PAWN PROMOTION SECTION.......................

    private void promotePawn(Color color){
        JFrame promotionFrame = new JFrame();
        promotionFrame.setLayout(null);
        promotionFrame.setTitle("Chess");
        promotionFrame.setBounds(500, 400, 420, 150);
        promotionFrame.setVisible(true);

        String colorName;
        ArrayList<String> piecesName = new ArrayList<>(Arrays.asList("Bishop.png", "Knight.png", "Rook.png", "Queen.png"));
        if(color == Color.BLACK){
            colorName = "Black";
        }
        else {
            colorName = "White";
        }

        String pathToIcon = "C:\\Users\\Windows\\Desktop\\Tree\\Studying\\Java Folders\\SIChess\\src\\main\\resources\\Icons\\" + colorName;
        ArrayList<JButton> buttons = new ArrayList<>();

        Square square = selectedPiece.getActualSquare();
        for(String pieceName : piecesName){
            ImageIcon icon = new ImageIcon(pathToIcon + pieceName);

            JButton button = new JButton(icon);
            button.setSize(100, 100);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (pieceName.equalsIgnoreCase("Rook.png")) {
                        kill(square, selectedPiece, 1);
                        Rook rook = new Rook(color, PieceType.ROOK, square);
                        drawPiece(rook, square);
                        selectedPiece = rook;
                    }
                    if (pieceName.equalsIgnoreCase("Bishop.png")) {
                        kill(square, selectedPiece, 1);
                        Bishop bishop = new Bishop(color, PieceType.BISHOP, square);
                        drawPiece(bishop, square);
                        selectedPiece = bishop;
                    }
                    if (pieceName.equalsIgnoreCase("Queen.png")) {
                        kill(square, selectedPiece, 1);
                        Queen queen = new Queen(color, PieceType.QUEEN, square);
                        drawPiece(queen, square);
                        selectedPiece = queen;
                    }
                    if (pieceName.equalsIgnoreCase("Knight.png")) {
                        kill(square, selectedPiece, 1);
                        Knight knight = new Knight(color, PieceType.KNIGHT, square);
                        drawPiece(knight, square);
                        selectedPiece = knight;
                    }
                }
            });

            buttons.add(button);
        }

        int x = 0;
        int y = 12;
        for(JButton button : buttons){
            promotionFrame.getContentPane().add(button);
            button.setBounds(x, y ,100, 100);
            x += 100;
        }

    }

    private void drawPiece(PieceAbstract pieceAbstract, Square square){
        pieces.add(pieceAbstract);
        square.setPiece(pieceAbstract);
        square.nowHasPiece();
        layeredPane.add(pieceAbstract, JLayeredPane.DRAG_LAYER);
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


    // TODO:.......................VALID MOVE CHECK SECTION.......................
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
        boolean isValid = false;
        if (selectedPiece.pieceType != PieceType.KNIGHT && selectedPiece.getPieceType() != PieceType.KING) {
            int fileLimit = abs(selectedPiece.getNewSquare().getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue());
            int rowLimit = abs(selectedPiece.getNewSquare().getRow() - selectedPiece.getPreviousSquare().getRow());
            if (fileLimit == 0 && (selectedPiece.getPieceType() == PieceType.ROOK || selectedPiece.getPieceType() == PieceType.QUEEN || selectedPiece.getPieceType() == PieceType.PAWN)) {
                isValid = isValidPathUpAndDown(selectedPiece);
            }
            if (rowLimit == 0 && (selectedPiece.getPieceType() == PieceType.ROOK || selectedPiece.getPieceType() == PieceType.QUEEN)){
                isValid = isValidPathLeftAndRight(selectedPiece);
            }

            if (fileLimit == rowLimit && rowLimit > 0 && (selectedPiece.getPieceType() == PieceType.BISHOP || selectedPiece.getPieceType() == PieceType.QUEEN || selectedPiece.getPieceType() == PieceType.PAWN)){
                isValid = isValidPathDiagonal(selectedPiece);
            }

        }
        else{
            isValid = true;
        }
        return isValid;
    }


    // TODO:.......................ENPASSANT SECTION.......................
    public boolean isEnPassant(PieceAbstract selectedPiece){
        if(selectedPiece.pieceType == PieceType.PAWN){
            for (Square square : this.squares){
                if(selectedPiece.getPreviousSquare().getRow() == square.getRow() && (selectedPiece.getPreviousSquare().getFile().getValue() == square.getFile().getValue() - 1 || selectedPiece.getPreviousSquare().getFile().getValue() == square.getFile().getValue() + 1)){
                    if(square.getPiece() != null){
                        if(square.getPiece().getPieceType() == PieceType.PAWN && square.getPiece().getColor() != selectedPiece.getColor() && square.getPiece().getNumberPieceHasMoved() == 2){
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

    //TODO:.......................CASTLE SECTION.......................
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
        PieceAbstract rook = rookSquare.getPiece();
        System.out.println(fileLimit);
        Square square1 = null;
        Square kingSquare;
        for(Square square : this.squares){
            if(fileLimit == 3) {
                if (square.getRow() == rowFlag) {
                    System.out.println("Here" + rowFlag);
                    if (square.getFile().getValue() == 7 && square1 == null) {
                        System.out.println(square.getFile().getValue());
                        square1 = square;
                    }
                    if (square.getFile().getValue() == fileFlag + 1) {
                        move(square, rookSquare.getPiece(), 1);
                        square.setPiece(rook);
                        square.nowHasPiece();
                        //rookSquare.setPiece(null);
                    }
                }
            }
            if(fileLimit == 4){
                if (square.getRow() == rowFlag) {
                    if (square.getFile().getValue() == fileFlag - 2) {
                        move(square, selectedPiece, 1);
                        square.setPiece(selectedPiece);
                        square.nowHasPiece();
                        if(selectedPiece.getColor() == Color.WHITE){
                            this.whiteKingsSquare = square;
                        }
                        else{
                            this.blackKingsSquare = square;
                        }
                    }
                    if (square.getFile().getValue() == fileFlag - 1) {
                        move(square, rookSquare.getPiece(), 1);
                        square.setPiece(rook);
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
        if(square1 != null) {
            move(square1, selectedPiece, 1);
            square1.nowHasPiece();
            if(selectedPiece.getColor() == Color.WHITE){
                this.whiteKingsSquare = square1;
            }
            else{
                this.blackKingsSquare = square1;
            }
        }

    }

    //TODO:.......................KILL/MOVE SECTION.......................
    public void kill(Square newSquare, PieceAbstract selectedPiece, int state){
        pieceToBeKilled = newSquare.getPiece();
        //newSquare.setPiece(selectedPiece);
        layeredPane.remove(pieceToBeKilled);
        pieces.remove(pieceToBeKilled);
        int X, Y;
        if(pieceToBeKilled.getColor() == Color.BLACK){
            X = xCoordinateToDrawTakenPiecePlayer1;
            Y = yCoordinateToDrawTakenPiecePlayer1;
        }
        else {
            X = xCoordinateToDrawTakenPiecePlayer2;
            Y = yCoordinateToDrawTakenPiecePlayer2;
        }
        if(state == 0) {
            drawTakenPieces(pieceToBeKilled, X, Y);
        }

    }

    public void move(Square newSquare, PieceAbstract selectedPiece, int mode){
        if(mode == 1) {
            if (selectedPiece.pieceType == PieceType.PAWN) {
                if (abs(selectedPiece.getNewSquare().getRow() - selectedPiece.getPreviousSquare().getRow()) == 2) {
                    selectedPiece.setNumberPieceHasMoved(2);
                } else {
                    selectedPiece.setNumberPieceHasMoved(1);
                }
                if (selectedPiece.getNumberPieceHasMoved() == 6) {
                    promotePawn(selectedPiece.getColor());
                }
            }
        }

        newSquare.setPiece(selectedPiece);
        newSquare.nowHasPiece();
        selectedPiece.setActualSquare(newSquare);
        selectedPiece.getActualSquare().nowHasPiece();
        selectedPiece.getActualSquare().thePieceIsWhite(selectedPiece.isWhite());
        selectedPiece.getActualSquare().setTypeOfPiece(selectedPiece.getPieceType());
        selectedPiece.getPreviousSquare().nowThereIsNoPiece();
        selectedPiece.setPreviousSquare(newSquare);

        if(mode == 1){
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

    private void returnToPreviousSquare1(PieceAbstract selectedPiece, Square previousSquare){
        selectedPiece.setLocation(previousSquare.getX(), previousSquare.getY());
        selectedPiece.setActualSquare(previousSquare);
    }

    private ArrayList<Square> validPathUnderCheck(PieceAbstract selectedPiece){
        ArrayList<Square> resultArray = new ArrayList<>();
        if(checkerPiece != null) {
            if (checkerPiece.getPieceType() == PieceType.KNIGHT) {
                if (selectedPiece.getPieceType() == PieceType.KING) {
                    resultArray = validPath;
                } else {
                    resultArray.add(checkerPiece.getActualSquare());
                }
            } else {
                for (Square possibleSquare : validPath(selectedPiece)) {
                    int counter = 0;
                    //System.out.println("checkSquare " + checkSquare.getFile() + " " + checkSquare.getRow());
                    //System.out.println("dick23 " + possibleSquare.getFile() + " " + possibleSquare.getRow());
                    for (Square checkSquare : squareToGuardTheKing(validPath(checkerPiece))) {
                        System.out.println("checkSquare " + checkSquare.getFile() + " " + checkSquare.getRow());

                        if (possibleSquare == checkSquare && selectedPiece.getPieceType() != PieceType.KING) {
                            resultArray.add(possibleSquare);
                            //System.out.println("dick1 " + possibleSquare.getFile() + " " + possibleSquare.getRow());
                        }
                        if (possibleSquare != checkSquare && checkerPiece.getActualSquare() == checkSquare && selectedPiece.getPieceType() == PieceType.KING) {
                            if(!willTheKingBeUnderAttack(selectedPiece, possibleSquare)) {
                                System.out.println("DICK " + possibleSquare.getFile() + " " + possibleSquare.getRow());
                                counter += 1;
                            }
                            //resultArray.add(possibleSquare);
                            //kingsResultArray.add(possibleSquare);
                            //System.out.println("dick2 " + possibleSquare.getFile() + " " + possibleSquare.getRow());
                        }
                        if (possibleSquare == checkSquare && checkerPiece.getActualSquare() == checkSquare && selectedPiece.getPieceType() == PieceType.KING) {
                            if(!willTheKingBeUnderAttack(selectedPiece, checkerPiece.getActualSquare())) {
                                counter += 1;
                            }
                            //resultArray.add(possibleSquare);
                            //kingsResultArray.add(possibleSquare);
                            //System.out.println("dick6 " + possibleSquare.getFile() + " " + possibleSquare.getRow());
                        }
                        if (possibleSquare == checkSquare && selectedPiece.getPieceType() == PieceType.KING) {
                            //resultArray.add(possibleSquare);
                            //System.out.println("dick3 " + possibleSquare.getFile() + " " + possibleSquare.getRow());
                        }
                    }
                    if (counter == squareToGuardTheKing(validPath(checkerPiece)).size()) {
                        System.out.println("dick23 " + possibleSquare.getFile() + " " + possibleSquare.getRow());
                        resultArray.add(possibleSquare);
                    }
                    counter = 0;
                    //System.out.println("Counter: " + counter);
                }
            }
        }
        /*this.resultArray = resultArray;
        ArrayList<Square> resultArray1 = new ArrayList<>();
        for(Square square : resultArray){
            if(!willTheKingBeUnderAttack(selectedPiece, square)){
                resultArray1.add(square);
            }
        }*/
        return resultArray;
    }

    public ArrayList<Square> xd(int checks) {
        ArrayList<Square> result = new ArrayList<>();
        CopyOnWriteArrayList<PieceAbstract> piecesCopy = new CopyOnWriteArrayList<>(this.pieces);
        CopyOnWriteArrayList<Square> squaresCopy = new CopyOnWriteArrayList<>(this.squares);

        if(checks != 0) {
            for (PieceAbstract piece : piecesCopy) {
                if (checkerPiece.getColor() != piece.getColor()) {
                    /*for (Square square : squaresCopy) {
                        piece.setNewSquare(square);
                        if((piece.isValidMove() && isValidPath(piece)) || (piece.isValidKill() && isValidPath(piece))) {
                            piece.setNewSquare(piece.getPreviousSquare());
                            if (!willTheKingBeUnderAttack(piece, square) && this.numberOfAttackers == 1) {
                                result.add(square);
                            }
                        }
                        piece.setNewSquare(piece.getPreviousSquare());*/
                    for(Square square1 : validPathUnderCheck(piece)){
                        result.add(square1);
                    }
                }
            }
        }
        return result;
    }

    public boolean isThereCheck_castle(ArrayList<Square> validPath1, Color color) {
        boolean check = false;
        for (Square square : validPath1) {
            if (this.castleSquaresArray.contains(square)) {
                check = true;
            }
        }

        return check;
    }

    public boolean willTheKingBeUnderAttack(PieceAbstract piece, Square newSquare){
        boolean result = false;
        PieceAbstract pieceClone = piece;
        PieceAbstract pieceOnNewSquare = newSquare.getPiece();
        boolean hasMovedOnNewSqaure = false;
        boolean hasMovedPiece = piece.hasMoved();
        if(newSquare.getPiece() != null) {
            hasMovedOnNewSqaure = pieceOnNewSquare.hasMoved();
            System.out.println("PIECETYPE NEW: " + newSquare.getPiece().getPieceType());
        }
        System.out.println("PIECETYPE: " + piece.getPieceType());

        Square whiteKingSquare = this.whiteKingsSquare;
        Square blackKingSquare = this.blackKingsSquare;

        Square prevSquare = piece.getPreviousSquare();
        Square actualSquare = piece.getActualSquare();
        Square newSquare1 = piece.getNewSquare();
        System.out.println("prevSquare: " + prevSquare.getFile() + " " + prevSquare.getRow());
        System.out.println("actualSquare: " + actualSquare.getFile() + " " + actualSquare.getRow());
        System.out.println("actualSquare getPiece: " + actualSquare.getPiece() + " Type: " + actualSquare.getTypeOfPiece());
        System.out.println("actualSquare getPiece: " + actualSquare.getPiece());
        System.out.println("newSquare: " + newSquare.getFile() + " " + newSquare.getRow());
        System.out.println("newSquare getPiece: " + newSquare.getPiece());
        //move(newSquare, selectedPiece, 0);

        boolean isCastle = isCastle(piece, newSquare);

        if(isCastle){
            System.out.println("CASTLE HERE 1");
            int kingFile = selectedPiece.getPreviousSquare().getFile().getValue();
            int fileLimit = abs(newSquare.getFile().getValue() - selectedPiece.getPreviousSquare().getFile().getValue());
            int fileFlag = selectedPiece.getPreviousSquare().getFile().getValue();
            int rowFlag = selectedPiece.getPreviousSquare().getRow();
            int sign = 1;
            if(newSquare.getFile().getValue() == 1){
                sign = -1;
            }
            for(Square square : this.squares) {
                for (int i = 0; i < fileLimit; i++) {
                    if(square.getFile().getValue() == sign*i+kingFile && square.getRow() == rowFlag){
                        this.castleSquaresArray.add(square);
                        System.out.println("NAHHHHHHHHHH");
                        System.out.println(square.getFile() + " " + square.getRow());
                    }
                }
            }
        }

        for(Square square : this.squares){
            if(square == prevSquare){
                square.setPiece(null);
                square.nowThereIsNoPiece();
            }
            if (square == newSquare){
                square.setPiece(piece);
                square.nowHasPiece();
                if(selectedPiece.getPieceType() == PieceType.KING){
                    if(selectedPiece.getColor() == Color.WHITE){
                        this.whiteKingsSquare = square;
                    }
                    else {
                        System.out.println(square.getFile() + " " + square.getRow());
                        this.blackKingsSquare = square;}
                }
            }
        }
        if(pieceOnNewSquare != null) {
            if (this.pieces.contains(pieceOnNewSquare)) {
                this.pieces.remove(pieceOnNewSquare);
            }
        }
        for(PieceAbstract pieceOnBoard: this.pieces){
            this.helperPiece = pieceOnBoard;

            if(isThereCheck2(validPath(pieceOnBoard), pieceOnBoard.getColor(), 0, pieceOnBoard)){
                if(selectedPiece.getColor() != pieceOnBoard.getColor()){
                //if(checkerPiece.getColor() != pieceOnBoard.getColor()){
                    result = true;
                    System.out.println("Possible ATTACKER: " + pieceOnBoard.getPieceType());
                    this.willNumberOfAttackersBeIncreased += 1;
                    this.possibleAttacker = pieceOnBoard;
                }
            }
            if(isCastle) {
                System.out.println("CASTLE HERE 2 ");
                if (isThereCheck_castle(validPath(pieceOnBoard), pieceOnBoard.getColor()) && pieceOnBoard.getColor() != piece.getColor()) {
                    result = true;

                    System.out.println("Possible ATTACKER castle: " + pieceOnBoard.getPieceType() + pieceOnBoard.getColor());
                    this.willNumberOfAttackersBeIncreased += 1;
                    this.possibleAttacker = pieceOnBoard;
                }
            }
        }


        piece.setPreviousSquare(prevSquare);
        if(pieceOnNewSquare != null) {
            this.pieces.add(pieceOnNewSquare);
        }
        for(Square square : this.squares){
            if(square == piece.getPreviousSquare()){
                square.setPiece(piece);
                piece.getPreviousSquare().nowHasPiece();
            }
            if(square == newSquare){
                square.setPiece(pieceOnNewSquare);
            }

        }
        this.whiteKingsSquare = whiteKingSquare;
        this.blackKingsSquare = blackKingSquare;
        System.out.println("prevSquare: " + piece.getPreviousSquare().getFile() + " " + piece.getPreviousSquare().getRow());
        System.out.println("prevSquare getPiece: " +  piece.getPreviousSquare().getPiece() + " Type: " + piece.getPreviousSquare().getTypeOfPiece());

        /*newSquare.nowThereIsNoPiece();
        piece.getActualSquare().nowThereIsNoPiece();
        piece.getNewSquare().nowThereIsNoPiece();
        piece.setActualSquare(actualSquare);
        piece.getActualSquare().setPiece(piece);
        piece.getActualSquare().nowHasPiece();
        piece.getActualSquare().thePieceIsWhite(selectedPiece.isWhite());
        piece.getActualSquare().setTypeOfPiece(piece.getPieceType());
        piece.setPreviousSquare(prevSquare);
        piece.getPreviousSquare().setPiece(piece);
        piece.setNewSquare(newSquare1);
        piece.getNewSquare().setPiece(pieceOnNewSquare);

        //piece.setPreviousSquare(prevSquare);
        //piece.setActualSquare(actualSquare);
        //piece.setNewSquare(newSquare1);

        System.out.println("squareEmpty123: " + newSquare.getFile() + "  " + newSquare.getRow());
        System.out.println("squareEmpty123: " + newSquare.isTherePiece());
        System.out.println("prevSquare: " + piece.getPreviousSquare().getFile() + " " + piece.getPreviousSquare().getRow());
        System.out.println("actualSquare: " + piece.getActualSquare().getFile() + " " + piece.getActualSquare().getRow());
        System.out.println("actualSquare getPiece: " + piece.getActualSquare().getPiece() + " Type: " + piece.getActualSquare().getTypeOfPiece());
        System.out.println("newSquare of Piece: " + piece.getNewSquare().getFile() + " " + piece.getNewSquare().getRow());
        System.out.println("newSquare of Piece getPiece: " + piece.getNewSquare().getPiece());
        System.out.println("newSquare: " + newSquare.getFile() + " " + newSquare.getRow());
        System.out.println("newSquare getPiece: " + newSquare.getPiece());*/

        System.out.println("Will: " + this.willNumberOfAttackersBeIncreased);
        System.out.println("Now: " + this.numberOfAttackers);
        /*if(this.willNumberOfAttackersBeIncreased == this.numberOfAttackers){
            result = false;
        }
        else{
            result = true;
        }
        if(this.numberOfAttackers > 0){
            if(this.willNumberOfAttackersBeIncreased > 0){
                result = false;
            }
            else{
                result = true;
            }
        }*/

        piece.setHasMoved(hasMovedPiece);
        if(newSquare.getPiece() != null) {
            newSquare.getPiece().setHasMoved(hasMovedOnNewSqaure);
        }


        if(this.willNumberOfAttackersBeIncreased != 0){
            result = true;
        }
        else{
            result = false;
        }
        this.castleSquaresArray = new ArrayList<>();
        this.willNumberOfAttackersBeIncreased = 0;
        return result;
    }

    public boolean isThereCheckMate(){
        boolean checkmate = false;
        int countOfPossibleMoves = 0;
        if(checkerPiece != null) {
            if (selectedPiece.getColor() == checkerPiece.getColor()) {
                for (PieceAbstract piece : this.pieces) {

                    if (piece.getColor() != checkerPiece.getColor()) {
                        System.out.println("piece: " + piece.getPieceType() + " " + piece.getColor() + " " + piece.getPreviousSquare().getFile());

                        if (piece.getPieceType() != PieceType.KING) {
                            countOfPossibleMoves += validPathUnderCheck(piece).size();
                            System.out.println("piece: " + piece.getPieceType() + " " + piece.getColor() + " " + piece.getPreviousSquare().getFile());
                            System.out.println("CHECKMATE moves: " + countOfPossibleMoves);
                        } else {
                            for (Square square : validPath(piece)) {
                                validMoveUnderCheck(piece, square, 0);
                                countOfPossibleMoves += this.validMovesUnderCheck;
                                System.out.println("Count: " + countOfPossibleMoves);
                                System.out.println(square.getFile() + " " + square.getRow());
                            }
                        }
                    }

                }
                if (countOfPossibleMoves == 0) {
                    checkmate = true;
                }
            }
        }
        return checkmate;
    }

    public boolean doesKingAttackKing(PieceAbstract king, Square newSquare){
        boolean result = false;
        ArrayList<Square> oppositeKingSquares = new ArrayList<>();
        for(PieceAbstract piece : this.pieces){
            if(piece.getPieceType() == PieceType.KING && piece.getColor() != king.getColor()) {
                for (Square square : this.squares) {
                    if ((abs(square.getFile().getValue() - piece.getPreviousSquare().getFile().getValue()) + abs(square.getRow() - piece.getPreviousSquare().getRow()) == 1)
                            || (abs(square.getFile().getValue() - piece.getPreviousSquare().getFile().getValue()) == 1 && abs(square.getRow() - piece.getPreviousSquare().getRow()) == 1)){
                        oppositeKingSquares.add(square);
                    }
                }

            }
        }
        if(oppositeKingSquares.contains(newSquare)){
            result = true;
        }

        return result;
    }

    public int validMoveUnderCheck(PieceAbstract selectedPiece, Square newSquare, int mode){
        System.out.println("11111111111111");
        if(possibleAttacker != null){
            System.out.println("22222222222");
            if(newSquare.getPiece() != null) {
                System.out.println(newSquare.getPiece().getPieceType() + " " + newSquare.getPiece().getColor());
                if (newSquare.getPiece() == possibleAttacker) {
                    this.validMovesUnderCheck += 1;
                    if (mode == 1) {
                        kill(newSquare, selectedPiece, 0);
                        move(newSquare, selectedPiece, 1);
                        setCountOfMoves(selectedPiece);
                    }
                    return 1;
                } else {
                    System.out.println("33333333333333333");
                    returnToPreviousSquare(selectedPiece);
                    return 0;
                }
            }
        }
        System.out.println("44444444444444");
        returnToPreviousSquare(selectedPiece);
        return 0;
    }

    public int finalMove(Square newSquare) {
        //System.out.println("CheckMate: " + isThereCheckMate());
        /*if(selectedPiece.getNewSquare().getPiece() != null) {
            System.out.println(selectedPiece.getNewSquare().getPiece().getPieceType() + " " + selectedPiece.getNewSquare().getPieceColor());
        }*/
        if (selectedPiece.getPieceType() == PieceType.KING) {
            if (doesKingAttackKing(selectedPiece, newSquare)) {
                returnToPreviousSquare(selectedPiece);
                return 0;
            }
        }
        int moved = 0;
        System.out.println("En Passant " + isEnPassant(selectedPiece));
        //TODO: IF CHECK == TRUE

        boolean willTheKingBeUnderAttack = willTheKingBeUnderAttack(selectedPiece, newSquare);
        System.out.println("willTheKingBeUnderAttack: " + willTheKingBeUnderAttack);
        if(willTheKingBeUnderAttack){
            returnToPreviousSquare(selectedPiece);
            return 0;
        }
        /*if (!willTheKingBeUnderAttack) {
            //return validMoveUnderCheck(selectedPiece, newSquare, 1);
            if (numberOfAttackers == 1) {
                ArrayList<Square> resultArray = validPathUnderCheck(selectedPiece);

                Square resultSquare = null;

                System.out.println("size of my dick : " + resultArray.size());
                if (resultArray.size() == 0) {
                    System.out.println("here41");
                    returnToPreviousSquare(selectedPiece);
                    return 0;
                }
                for (Square square : resultArray) {
                    System.out.println("+-+-+-+-+-++--+-+-+-+--+-+-+-+-+");
                    System.out.println(square.getFile() + " " + square.getRow());
                    System.out.println(newSquare.getFile() + " " + newSquare.getRow() + " xd");
                    if (square.getFile() == newSquare.getFile() && square.getRow() == newSquare.getRow()) {
                        System.out.println("here1");
                        resultSquare = square;
                    }
                }
                if (resultSquare != null) {
                    if (newSquare.isTherePiece() && selectedPiece.isValidKill() && isValidPath(selectedPiece)) {
                        System.out.println("here21");
                        kill(newSquare, selectedPiece, 0);
                        move(newSquare, selectedPiece, 1);
                        if (selectedPiece.getPieceType() == PieceType.KING) {
                            if (selectedPiece.getColor() == Color.WHITE) {
                                whiteKingsSquare = selectedPiece.getActualSquare();
                            }
                            if (selectedPiece.getColor() == Color.BLACK) {
                                blackKingsSquare = selectedPiece.getActualSquare();
                            }
                        }
                        setCountOfMoves(selectedPiece);
                        checkerPiece = null;
                        return 1;
                    }
                    if (!newSquare.isTherePiece()) {
                        System.out.println("here22");
                        move(newSquare, selectedPiece, 1);
                        if (selectedPiece.getPieceType() == PieceType.KING) {
                            if (selectedPiece.getColor() == Color.WHITE) {
                                whiteKingsSquare = selectedPiece.getActualSquare();
                            }
                            if (selectedPiece.getColor() == Color.BLACK) {
                                blackKingsSquare = selectedPiece.getActualSquare();
                            }
                        }
                        setCountOfMoves(selectedPiece);
                        checkerPiece = null;
                        return 1;
                    }

                } else {
                    System.out.println("here33");
                    returnToPreviousSquare(selectedPiece);
                    return 0;
                }
            }
        }*/

        if(numberOfAttackers > 1){
            if(selectedPiece.getPieceType() != PieceType.KING){
                returnToPreviousSquare(selectedPiece);
                return 0;
            }
        }

        if(selectedPiece.getPieceType() == PieceType.KING && isCastle(selectedPiece, newSquare)){
            System.out.println("Castle " + isCastle(selectedPiece, newSquare));
            setCountOfMoves(selectedPiece);
            setCountOfMoves(newSquare.getPiece());
            castle(selectedPiece, newSquare);
            return 1;
        }

        if(newSquare.isTherePiece()){
            System.out.println("squareEmpty: " + newSquare.getFile() + "  " + newSquare.getRow());
            if(newSquare.getPiece().getColor() == selectedPiece.getColor()){
                returnToPreviousSquare(selectedPiece);
                return 0;
            }

            else{
                System.out.println("abrakadabra");
                System.out.println(selectedPiece.isValidKill());
                System.out.println(isValidPath(selectedPiece));

                if(selectedPiece.isValidKill() && isValidPath(selectedPiece)){
                    System.out.println("abrakadabra23");
                    kill(newSquare, selectedPiece, 0);
                    move(newSquare, selectedPiece, 1);
                    setCountOfMoves(selectedPiece);
                    moved = 1;
                }
                else{
                    System.out.println("Not valid kill");
                    returnToPreviousSquare(selectedPiece);
                    moved = 0;
                }
            }
        }


        if(!newSquare.isTherePiece()){
            if(selectedPiece.pieceType == PieceType.PAWN && isEnPassant(selectedPiece)){
                if (newSquare == enPassant(selectedPiece)){
                    for(Square square : this.squares) {
                        if (square.getFile().getValue() == this.enPassantSquare.getFile().getValue() && square.getRow() == this.enPassantSquare.getRow()) {
                            kill(square, selectedPiece, 0);
                            square.nowThereIsNoPiece();
                            moved = 1;
                        }
                    }

                    move(newSquare, selectedPiece, 1);
                    setCountOfMoves(selectedPiece);
                    moved = 1;
                }
            }
            System.out.println("No piece here");
            if(!isValidPath(selectedPiece)){
                System.out.println("No piece there, not valid move");
                returnToPreviousSquare(selectedPiece);
                moved = 0;
            }
            if(selectedPiece.isValidMove() && isValidPath(selectedPiece)){
                move(newSquare, selectedPiece, 1);
                setCountOfMoves(selectedPiece);
                if (selectedPiece.getPieceType() == PieceType.KING) {
                    if (selectedPiece.getColor() == Color.WHITE) {
                        whiteKingsSquare = newSquare;
                    }
                    if (selectedPiece.getColor() == Color.BLACK) {
                        blackKingsSquare = newSquare;
                    }
                }
                moved = 1;
            }
            else{
                System.out.println("No piece there, not valid move");
                returnToPreviousSquare(selectedPiece);
                moved = 0;
            }
        }
        // TODO: Kill()

        System.out.println("Count of White moves: " + this.numberOfWhiteMoves);
        System.out.println("Count of Black moves: " + this.numberOfBlackMoves);
        System.out.println("Count of This Pawn moves: " + selectedPiece.getNumberPieceHasMoved());

        return moved;
    }

    //TODO:.......................CHECK SECTION.......................
    public boolean checkValidation(Square square, PieceAbstract selectedPiece){
        boolean check = false;
        if(square.getPiece() != null){
            if(square.getPiece().getPieceType() == PieceType.KING && square.getPiece().getColor() != selectedPiece.getColor()){
                check = true;
            }
        }
        return check;
    }

    /*public boolean isThereCheck(ArrayList<Square> validPath){
        boolean check = false;
        for(Square square : validPath){
            System.out.println("------------------------------");
            System.out.println(square.getFile() + " " + square.getRow());
            if(square.getPiece() != null){
                if(square.getPiece().getPieceType() == PieceType.KING && square.getPiece().getColor() != selectedPiece.getColor()){
                    square.setNewColor(java.awt.Color.RED);
                    this.squareThatIsChecked = square;
                    check = true;
                }
            }
        }

        return check;
    }*/

    /*
    public ArrayList<PieceAbstract> getPiecesThatCheck(Color color){
        ArrayList<PieceAbstract> piecesThatCheck = new ArrayList<>();
        for(PieceAbstract piece : this.pieces){
            for(Square square : validPath(piece)){
                if(square.getPiece() != null){
                    if(square.getPiece().getPieceType() == PieceType.KING && square.getPiece().getColor() != color){
                        System.out.println("PISSSS " + piece.getPieceType() + piece.getColor());
                        piecesThatCheck.add(piece);
                    }
                }
            }
        }
        return piecesThatCheck;
    }*/

    public ArrayList<Square> squareToGuardTheKing(ArrayList<Square> checkPath){

        //Knight
        Square squareBeforeKing = null;
        ArrayList<Square> squaresBeforeKing = new ArrayList<>();
        if(checkPath != null){
            for (Square square : checkPath){
                if(square.getPiece() != null){
                    if(square.getPiece().getPieceType() == PieceType.KING){
                        squareBeforeKing = square;
                    }
                }
            }
        }
        else{
            return new ArrayList<>();
        }

        if (checkerPiece.pieceType != PieceType.KNIGHT && checkerPiece.pieceType != PieceType.PAWN && checkerPiece.getPieceType() != PieceType.KING && squareBeforeKing != null) {
            int fileLimit = abs(checkerPiece.getNewSquare().getFile().getValue() - squareBeforeKing.getPiece().getPreviousSquare().getFile().getValue());
            int rowLimit = abs(checkerPiece.getNewSquare().getRow() - squareBeforeKing.getPiece().getPreviousSquare().getRow());
            if (fileLimit == 0) {
                return checkPathUpAndDown(checkerPiece, squareBeforeKing.getPiece());
            }
            if (rowLimit == 0){
                return checkPathLeftAndRight(checkerPiece, squareBeforeKing.getPiece());
            }

            if (fileLimit == rowLimit && rowLimit > 0){
                return checkPathDiagonal(checkerPiece, squareBeforeKing.getPiece());
            }
        }
        /*if(checkerPiece.getPieceType() == PieceType.BISHOP || (checkerPiece.getPieceType() == PieceType.QUEEN && (checkerPiece.getNewSquare().getRow() - squareBeforeKing.getPiece().getNewSquare().getRow()) != 0 && (checkerPiece.getNewSquare().getFile().getValue() - squareBeforeKing.getPiece().getNewSquare().getFile().getValue()) != 0)){
            System.out.println("------------------------------------------------------------");
            for(Square square : checkPathDiagonal(checkerPiece, squareBeforeKing.getPiece())){
                System.out.println(square.getFile() + " xd " + square.getRow());
            }
            return checkPathDiagonal(checkerPiece, squareBeforeKing.getPiece());
        }
        if(checkerPiece.getPieceType() == PieceType.ROOK ||(checkerPiece.getPieceType() == PieceType.QUEEN && (checkerPiece.getNewSquare().getRow() - squareBeforeKing.getPiece().getNewSquare().getRow()) == 0 )){
            System.out.println("------------------------------------------------------------");
            for(Square square : checkPathLeftAndRight(checkerPiece, squareBeforeKing.getPiece())){
                System.out.println(square.getFile() + " xd " + square.getRow());
            }
            return checkPathLeftAndRight(checkerPiece, squareBeforeKing.getPiece());
        }

        if(checkerPiece.getPieceType() == PieceType.ROOK ||(checkerPiece.getPieceType() == PieceType.QUEEN && (checkerPiece.getNewSquare().getRow() - squareBeforeKing.getPiece().getNewSquare().getRow()) == 0 )){
            System.out.println("------------------------------------------------------------");
            for(Square square : checkPathLeftAndRight(checkerPiece, squareBeforeKing.getPiece())){
                System.out.println(square.getFile() + " xd " + square.getRow());
            }
            return checkPathLeftAndRight(checkerPiece, squareBeforeKing.getPiece());
        }*/
        /*
        int rowLimit = abs(checkerPiece.getNewSquare().getRow() - squareBeforeKing.getRow());
        int fileLimit = abs(checkerPiece.getNewSquare().getFile().getValue() - squareBeforeKing.getFile().getValue());
        for(Square square : checkPath){
            if(abs(square.getFile().getValue() - squareBeforeKing.getFile().getValue()) <= fileLimit && abs(square.getRow() - squareBeforeKing.getRow()) <= rowLimit){
                if(checkerPiece.getPieceType() != PieceType.KNIGHT){
                    squaresBeforeKing.add(square);
                }
                else{
                    squaresBeforeKing.add(checkerPiece.getNewSquare());
                }
            }
        }*/

        return squaresBeforeKing;
    }


    public ArrayList<Square> checkPathDiagonal(PieceAbstract checkerPiece, PieceAbstract king){
        ArrayList<Square> checkPath = new ArrayList<>();
        int fileLimit = abs(checkerPiece.getNewSquare().getFile().getValue() - king.getPreviousSquare().getFile().getValue());
        int rowLimit = abs(checkerPiece.getNewSquare().getRow() - king.getPreviousSquare().getRow());
        int clone = fileLimit;
        boolean flag = true;
        int coeffRow = 1;
        int coeffFile = 1;
        if(checkerPiece.getNewSquare().getRow() >= king.getPreviousSquare().getRow()){
            coeffRow = -1;
        }
        if(checkerPiece.getNewSquare().getFile().getValue() >= king.getPreviousSquare().getFile().getValue()){
            coeffFile = -1;
        }
        while (rowLimit != 0 && fileLimit != 0) {
            int diffRow = king.getPreviousSquare().getRow() - coeffRow*rowLimit;
            int diffFile = king.getPreviousSquare().getFile().getValue() - coeffFile*fileLimit;
            for(Square square : this.squares){
                if(square.getRow() == diffRow && square.getFile().getValue() == diffFile){
                    checkPath.add(square);
                }
            }
            rowLimit = rowLimit - 1;
            fileLimit = fileLimit - 1;
        }
        return checkPath;
    }

    public ArrayList<Square> checkPathLeftAndRight(PieceAbstract checkerPiece, PieceAbstract king){
        ArrayList<Square> checkPath = new ArrayList<>();
        int fileLimit = abs(checkerPiece.getNewSquare().getFile().getValue() - king.getPreviousSquare().getFile().getValue());
        int clone = fileLimit;
        boolean flag = true;
        if(checkerPiece.getNewSquare().getFile().getValue() >= king.getPreviousSquare().getFile().getValue()){
            flag = false;
        }
        int row = checkerPiece.getNewSquare().getRow();
        while (fileLimit != 0) {
            for(Square square : this.squares){
                int diff = 0;
                if (flag){
                    diff = king.getPreviousSquare().getFile().getValue() - fileLimit;
                }
                else {
                    diff = king.getPreviousSquare().getFile().getValue() + fileLimit;
                }
                if(square.getRow() == row && square.getFile().getValue() == diff){
                    checkPath.add(square);
                }
            }
            fileLimit = fileLimit - 1;
        }

        return checkPath;
    }

    public ArrayList<Square> checkPathUpAndDown(PieceAbstract checkerPiece, PieceAbstract king) {
        ArrayList<Square> checkPath = new ArrayList<>();
        int rowLimit = abs(checkerPiece.getNewSquare().getRow() - king.getPreviousSquare().getRow());
        int clone = rowLimit;
        boolean flag = true;
        if (checkerPiece.getNewSquare().getRow() >= king.getPreviousSquare().getRow()) {
            flag = false;
        }
        File file = checkerPiece.getNewSquare().getFile();
        while (rowLimit != 0) {
            for (Square square : this.squares) {
                int diff = 0;
                if (flag) {
                    diff = king.getPreviousSquare().getRow() - rowLimit;
                } else {
                    diff = king.getPreviousSquare().getRow() + rowLimit;
                }
                if (square.getFile() == file && square.getRow() == diff) {
                    checkPath.add(square);
                }
            }
            rowLimit = rowLimit - 1;
        }

        return checkPath;
    }


    private void checkMechanism(){
        int countWhite = 0;
        int countBlack = 0;
        Color color = selectedPiece.getColor();// не можна не ініціалізувати зміну в цьому випадку, бо в if буде помилка
        for(PieceAbstract piece : pieces){
            if(piece.getColor() == color && piece.getPieceType() == PieceType.KING){
                piece.getActualSquare().setNewColor(java.awt.Color.RED);
                this.squareThatIsChecked = piece.getActualSquare();
            }
        }
    }

    // TODO:.......................DRAWING/UTILS SECTION.......................


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

    private void drawTakenPieces(PieceAbstract pieceAbstract, int X, int Y){

        JLabel player1TakenPiece = new JLabel();
        ImageIcon takenPieceIcon1 = new ImageIcon(pieceAbstract.getPath());

        Image scaledIcon2 = takenPieceIcon1.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        takenPieceIcon1 = new ImageIcon(scaledIcon2);

        player1TakenPiece.setIcon(takenPieceIcon1);
        player1TakenPiece.setBounds(X, Y, 30, 30);

        layeredPane.add(player1TakenPiece);
        if(pieceAbstract.getColor() == Color.BLACK){
            xCoordinateToDrawTakenPiecePlayer1 += 30;
        }
        else {
            this.xCoordinateToDrawTakenPiecePlayer2 += 30;
        }
    }

    public void drawPieces(Color color, Square square){
        if (square.getRow() == 2 || square.getRow() == 7) {
            Pawn pawn = new Pawn(color, PieceType.PAWN, square);
            System.out.println(pawn.pieceType + " " + pawn.getColor());
            pieces.add(pawn);
            square.setPiece(pawn);
            square.nowHasPiece();
            pawn.setNewSquare(square);

            pawn.getNewSquare().setPiece(pawn);
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
                if(color == Color.WHITE){
                    this.whiteKingsSquare = square;
                }
                else {
                    this.blackKingsSquare = square;
                }
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



