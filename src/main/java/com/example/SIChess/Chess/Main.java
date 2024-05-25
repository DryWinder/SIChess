package com.example.SIChess.Chess;

import com.example.SIChess.Chess.Board.Board;
import com.example.SIChess.Chess.Board.File;
import com.example.SIChess.Chess.Board.Square;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Main {
    public static void main(String[] args) {
        OpeningFrame openingFrame = new OpeningFrame();

        openingFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                int numberOfEngines = openingFrame.getNumberOfEngines();
                if (numberOfEngines > 0) {
                    if (numberOfEngines == 1) {
                        EngineChooserFrame engineChooserFrame = new EngineChooserFrame(true);
                        engineChooserFrame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                System.out.println(engineChooserFrame.getEngines());
                                // Викликаємо відкриття дошки, коли вікно вибору двигуна завершилося
                                final Board board = new Board();

                                //london
                                System.out.println("Italian game: ");
                                board.whiteSquares(Color.WHITE, File.e, File.f, File.c, 4, 6, 5);

                                System.out.println("The Ruy-Lopez: ");
                                board.whiteSquares(Color.WHITE, File.e, File.f, File.b, 5, 6, 4);

                                System.out.println("The London System: ");
                                board.whiteSquares(Color.WHITE, File.d, File.f, File.f, 5, 6, 5);

                                System.out.println("The French Defence: ");
                                board.whiteSquares(Color.BLACK, File.e, File.d, File.f, 3, 4, 5);

                                System.out.println("The Slav Defence: ");
                                board.whiteSquares(Color.BLACK, File.d, File.c, File.f, 4, 3, 5);

                                System.out.println("The Pirc Defence: ");
                                board.whiteSquares(Color.BLACK, File.d, File.f, File.f, 3, 3, 5);

                                for (Square square : board.getSquares()) {
                                    System.out.println(square.getColor() + " " + square.getFile() + square.getRow());
                                }
                                board.paintBoard();

                                System.out.println("London game: ");
                                board.whiteSquares(Color.WHITE, File.d, File.f, File.f, 5, 6, 5);
                                board.londonOp();
                                board.AImove(board.whiteSquare1);
                            }
                        });
                    } else {
                        EngineChooserFrame engineChooserFrame = new EngineChooserFrame(false);
                        engineChooserFrame.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                // Викликаємо відкриття дошки, коли вікно вибору двигуна завершилося
                                System.out.println(engineChooserFrame.getEngines());
                                final Board board = new Board();
                                for (Square square : board.getSquares()) {
                                    System.out.println(square.getColor() + " " + square.getFile() + square.getRow());
                                }
                                board.paintBoard();
                            }
                        });
                    }
                }
                else{
                    final Board board = new Board();
                    for (Square square : board.getSquares()) {
                        //System.out.println(square.getColor() + " " + square.getFile() + square.getRow());
                    }
                    board.paintBoard();
                }
            }
        });
    }
}