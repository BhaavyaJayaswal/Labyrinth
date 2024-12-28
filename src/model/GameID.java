/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Objects;

public class GameID {
    public final int level;
    
    /**
     * Constructor initializes the game ID with a level number.
     *
     * @param level the level number of the game.
     */
    public GameID(int level) {
        this.level = level;
    }
    
    /**
     * Calculates a hash code based on the level number.
     *
     * @return the hash code of the game ID.
     */
    @Override
    public int hashCode(){
        int hash = 3;
        hash = 29 * hash + this.level;
        return hash;
    }
    
    /**
     * Compares two GameID objects for equality based on their level number.
     *
     * @param obj the object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameID other = (GameID) obj;
        if (this.level != other.level) {
            return false;
        }
        
        return true;
    }
}
