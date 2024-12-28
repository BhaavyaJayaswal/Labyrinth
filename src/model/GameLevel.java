/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.Random;

public class GameLevel {
    public final GameID gameID;
    public final int rows, cols;
    public final LevelItem[][] level;
    public Position player = new Position(0, 0);
    public Position dragon = new Position(0, 0);
    private Direction currentDragonDirection;
    
    
    /**
     * Constructor initializes the game level based on the level data.
     *
     * @param gameLevelRows the rows of the game level.
     * @param gameID the GameID of the level.
     */
    public GameLevel (ArrayList<String> gameLevelRows, GameID gameID){
        this.gameID = gameID;
        int c = 0;
        for (String s : gameLevelRows) 
            if (s.length()>c) 
                c = s.length();
        rows = gameLevelRows.size();
        cols = c;
        level = new LevelItem[rows][cols];
        
        for (int i = 0; i < rows; i++){ 
            String s = gameLevelRows.get(i);
            for (int j = 0; j < s.length(); j++){
                switch (s.charAt(j)){
                    case '#': level[i][j] = LevelItem.WALL; break;
                    case '*': level[i][j] = LevelItem.EXIT; break;
                    default: level[i][j] = LevelItem.EMPTY; break;
                }
            }
            for (int j = s.length(); j < cols; j++){
                level[i][j] = LevelItem.EMPTY;
            }
            player.x = 0;
            player.y = rows-2; //row
            dragon = generateRandomPosition();
            chooseRandomDirection();
            //level[dragon.x][dragon.y] = LevelItem.DRAGON;
        }              
    }
    
    public GameLevel (GameLevel g1) {
        gameID = g1.gameID;
        rows = g1.rows;
        cols = g1.cols;
        level = new LevelItem[rows][cols];
        player = new Position(g1.player.x, g1.player.y);
        dragon = new Position(g1.dragon.x, g1.dragon.y);
        chooseRandomDirection();
        for(int i = 0; i < rows; i++) {
            System.arraycopy(g1.level[i], 0, level[i], 0, cols);
        }  
    }
    
    /**
     * Resets the player and dragon to their initial positions in the game.
     */
    public void reset(){
        player.x = 0;
        player.y = rows-2;
        dragon = generateRandomPosition();
    }
    
    /**
     * Checks if the given position is valid (within bounds of the level).
     *
     * @param p the position to check.
     * @return true if the position is valid, false otherwise.
     */
    public boolean isValidPosition(Position p) {
        return (p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows);
    }
    
    /**
     * Checks if the given position is free (not occupied by a wall).
     *
     * @param p the position to check.
     * @return true if the position is free, false if it's blocked by a wall.
     */
    public boolean isFree(Position p) {
        if (!isValidPosition(p))
            return false;
        LevelItem li = level[p.y][p.x];
        return (li != LevelItem.WALL);
    }
    
    /**
     * Moves the player in the specified direction if possible.
     *
     * @param d the direction to move the player.
     * @return true if the player was successfully moved, false otherwise.
     */
    public boolean movePlayer(Direction d) {
        Position curr = player;
        Position next = curr.translate(d);
        if (isFree(next)) {
            player = next;
            return true;
        }
        return false;
    }
    
    /**
     * Moves the dragon in the specified direction if possible.
     *
     * @param d the direction to move the dragon.
     * @return true if the dragon was successfully moved, false otherwise.
     */
    public boolean moveDragon(Direction d) {
    Position next = dragon.translate(d);
    if (isFree(next)) {
        dragon = next;
        return true;
    }
    return false;
    }
    
    /**
     * Picks a random direction from the Direction enumerator
     */
    public void chooseRandomDirection() { 
        Random rand = new Random(); 
        Direction[] directions = Direction.values(); 
        currentDragonDirection = directions[rand.nextInt(directions.length)]; 
    }
    
    /**
     * Retrieves the current direction of the dragon.
     *
     * @return the current direction of the dragon.
     */
    public Direction getCurrentDragonDirection() { 
        return currentDragonDirection; 
    }
    
    /**
     * Generates a random position for the dragon that is not too close to the player and not blocked by a wall.
     *
     * @return a random valid position for the dragon.
     */
    public Position generateRandomPosition() { 
        Position playerPos = player;
        
        Random rand = new Random();
        Position randomPos;
        int x,y;
        // Loop until a valid position is found
        do {
            x = rand.nextInt(cols); // random x-coordinate
            y = rand.nextInt(rows); // random y-coordinate
            randomPos = new Position(x, y); //crossing i j here
        } while (isTooCloseToPlayer(randomPos, playerPos) || (level[y][x]==LevelItem.WALL));

        return randomPos;
    }

    /**
     * Checks if the dragon's position is too close to the player's position (within 2 cells).
     *
     * @param dragonPos the position of the dragon.
     * @param playerPos the position of the player.
     * @return true if the dragon's position is too close to the player, false otherwise.
     */
    private boolean isTooCloseToPlayer(Position dragonPos, Position playerPos) {
        int dx = Math.abs(dragonPos.x - playerPos.x);
        int dy = Math.abs(dragonPos.y - playerPos.y);
        
        // Return true if the dragon's position is within 2 cells of the player
        return dx <= 2 && dy <= 2;
    }

    /**
     * Checks if the player is dead (i.e., if the player and dragon occupy the same position).
     *
     * @return true if the player and dragon are at the same position, false otherwise.
     */
    public boolean isPlayerDead() { 
        return player.equals(dragon);
    }
    
    
    /**
     * Prints the current state of the game level to the console.
     * The player is represented by '@', and other items in the level are represented by their respective symbols.
     */
    public void printLevel(){
        int x = player.x, y = player.y;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++){
                if (i == y && j == x)
                    System.out.print('@');
                else
                    System.out.print(level[i][j].representation);
            }
            System.out.println("");
        }
    }
    
}
