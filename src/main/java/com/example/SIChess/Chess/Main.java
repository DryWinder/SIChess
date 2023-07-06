package com.example.SIChess.Chess;

import com.example.SIChess.Chess.Board.Board;
import com.example.SIChess.Chess.Board.Square;


public class Main {
    public static void main(String[] args){

        final Board board = new Board();
        for(Square square: board.getSquares()){
            System.out.println(square.getColor() + " " + square.getFile() + square.getRow());
        }
        board.paintBoard();
        System.out.println(board.getPieces().size());
    }
}
