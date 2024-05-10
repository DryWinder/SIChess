package com.example.SIChess.Application;

import com.example.SIChess.Chess.Board.Board;
import com.example.SIChess.Chess.Board.Square;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.awt.GraphicsEnvironment;



@Controller
public class mainPage {
    @GetMapping("/main")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        System.setProperty("java.awt.headless", "false");
        model.addAttribute("name", name);

        final Board board = new Board();
        for(Square square: board.getSquares()){
            System.out.println(square.getColor() + " " + square.getFile() + square.getRow());
        }
        board.paintBoard();
        board.paintBoard();
        System.out.println(board.getPieces().size());
        return "main";
    }
}
