/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package model;

import java.util.Objects;

/**
 *
 * @author HP
 */
public class Position {
    
    public int x, y;
    
     /**
     * Constructor to initialize the position with specified coordinates.
     *
     * @param x the x-coordinate of the position.
     * @param y the y-coordinate of the position.
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Translates the position by a given direction.
     * This method creates a new Position by adding the direction's 
     * x and y components to the current position.
     *
     * @param d the direction to translate the position by.
     * @return a new Position object with the translated coordinates.
     */
    public Position translate(Direction d){
        return new Position(x + d.x, y + d.y);
    }
    
    /**
     * Checks if the current position is equal to another position.
     * Two positions are considered equal if they have the same x and y coordinates.
     *
     * @param obj the object to compare this position with.
     * @return true if the positions are equal, false otherwise.
     */
    @Override 
    public boolean equals(Object obj) 
    { 
        if (this == obj) return true; 
        if (obj == null || getClass() != obj.getClass()) 
            return false; 
        Position position = (Position) obj; 
        return x == position.x && y == position.y; 
    }
    
     /**
     * Generates a hash code for the position based on its x and y coordinates.
     *
     * @return the hash code for the position.
     */
    @Override public int hashCode() 
    { 
        return Objects.hash(x, y); 
    }
    
}
