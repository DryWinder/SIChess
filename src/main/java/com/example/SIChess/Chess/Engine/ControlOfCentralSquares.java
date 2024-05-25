package com.example.SIChess.Chess.Engine;

import com.example.SIChess.Chess.Board.File;
import com.example.SIChess.Chess.Board.Square;
import com.example.SIChess.Chess.Board.Board;
import com.example.SIChess.Chess.Color;
import com.example.SIChess.Chess.Pieces.PieceAbstract;

import java.util.ArrayList;

//import static com.example.SIChess.Chess.Board.Board.validPath;

public class ControlOfCentralSquares {

    private Color color;
    private ArrayList<PieceAbstract> pieces;
    private ArrayList<Square> squares;
    int numberOfPiecesThatControlCenter = 0;
    int getNumberOfPiecesOnCenter = 0;

    public ControlOfCentralSquares(ArrayList<PieceAbstract> pieces, ArrayList<Square> squares, Color color){
        this.pieces = pieces;
        this.squares = squares;
        this.color = color;
    }

    public int centreControl(){
        for(PieceAbstract piece : pieces){
            if(piece.getColor() == color) {
                for (Square square : validPath(piece)) {
                    if (square.getFile() == File.d || square.getFile() == File.e) {
                        if (square.getRow() == 4 || square.getRow() == 5) {
                            numberOfPiecesThatControlCenter += 1;
                        }
                    }
                }
                if (piece.getActualSquare().getFile() == File.e || piece.getActualSquare().getFile() == File.d) {
                    if (piece.getActualSquare().getRow() == 4 || piece.getActualSquare().getRow() == 5) {
                        this.getNumberOfPiecesOnCenter += 10;
                    }
                }
            }
        }
        return numberOfPiecesThatControlCenter + getNumberOfPiecesOnCenter;
    }
}
