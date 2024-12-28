/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistence;

/**
 *
 * @author HP
 */
public class HighScore {
    public final int score;
    public final String name;
    
    /**
     * Constructor to initialize a high score entry with the player's name and score.
     * 
     * @param name the name of the player.
     * @param score the score achieved by the player.
     */
    public HighScore(String name, int score){
        this.name = name;
        this.score = score;
    }
    
    
}
