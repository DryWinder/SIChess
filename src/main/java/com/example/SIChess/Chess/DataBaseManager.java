package com.example.SIChess.Chess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/student01_DB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root1";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void insertChessResult(int id, String player1, String player2, int player1NumOfMoves,
                                  int player2NumOfMoves, boolean player1Win, boolean player2Win,
                                  String player1DebutName, String player2DebutName, boolean draw) {
        String sql = "INSERT INTO chess_results (id, player1, player2, player1_num_of_moves, player2_num_of_moves, " +
                "player1_win, player2_win, player1_debut_name, player2_debut_name, draw) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, player1);
            pstmt.setString(3, player2);
            pstmt.setInt(4, player1NumOfMoves);
            pstmt.setInt(5, player2NumOfMoves);
            pstmt.setBoolean(6, player1Win);
            pstmt.setBoolean(7, player2Win);
            pstmt.setString(8, player1DebutName);
            pstmt.setString(9, player2DebutName);
            pstmt.setBoolean(10, draw);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        DataBaseManager dbManager = new DataBaseManager();
        dbManager.insertChessResult(1, "Player1", "Player2", 35, 36, true, false, "Debut1", "Debut2", false);
    }
}

