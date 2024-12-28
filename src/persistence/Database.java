/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistence;

/**
 *
 * @author HP
 */
import java.sql.*;
import java.sql.SQLException;
import java.util.*;

public class Database {
    private final String tableName = "highscores";
    private final Connection conn;
    private final ArrayList<HighScore> highScores;

    /**
     * Constructor to initialize the database connection and load the high scores.
     * 
     * @throws SQLException if a database access error occurs.
     */
    public Database() throws SQLException{
        Properties connectionProps = new Properties();
        // Add new user -> MySQL workbench (Menu: Server / Users and priviliges)
        //                             Tab: Administrative roles -> Check "DBA" option
        connectionProps.put("user", "root");
        connectionProps.put("password", "");
        connectionProps.put("serverTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3307/LabyrinthHighscores";
        
        conn = DriverManager.getConnection(dbURL, connectionProps);
        highScores = new ArrayList<>();
        loadHighScores();
    }

    /**
     * Stores a new high score in the database.
     * 
     * @param name the name of the player.
     * @param newScore the score achieved by the player.
     */
    public void storeHighScore(String name, int newScore){
        try (PreparedStatement stmt = conn.prepareStatement(
             "INSERT INTO " + tableName + " (player_name, score) VALUES (?, ?) "   
        )){
            stmt.setString(1, name); 
            stmt.setInt(2, newScore);
            stmt.executeUpdate();
            loadHighScores();
        } catch (Exception e){
            System.out.println("storeToDatabase error"+ e);
        }
    }

    /**
     * Retrieves the top 10 high scores from the database.
     * 
     * @return a list of the top 10 high scores, or fewer if there are not enough records.
     */
    public ArrayList<HighScore> getHighScores(){
        return new ArrayList<>(highScores.subList(0, Math.min(10, highScores.size())));
    }
    
     /**
     * Loads the high scores from the database into the in-memory list.
     * The high scores are ordered by score in descending order.
     */
    private void loadHighScores() {
        highScores.clear(); // Clear the list to avoid duplicates
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " ORDER BY score DESC LIMIT 10");
            while (rs.next()) {
                String name = rs.getString("player_name");
                int score = rs.getInt("score");
                highScores.add(new HighScore(name, score));
            }
        } catch (Exception e) { 
            System.out.println("loadHighScores error: " + e.getMessage());
        }
    }
}




