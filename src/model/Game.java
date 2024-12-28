/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.*;
import java.util.*;
import res.ResourceLoader;


public class Game {
    private final HashMap<Integer, GameLevel> gameLevels;
    private GameLevel gameLevel = null;
    
    /**
     * Constructor initializes the game and loads the levels.
     */
    public Game(){
        gameLevels = new HashMap<>();
        readLevels();
    }
    
    /**
     * Loads the game level corresponding to the provided game ID.
     *
     * @param gameID the game ID that identifies the level to load.
     */
    public void loadGame(GameID gameID){
        gameLevel = gameLevels.get(gameID.level);
    }
    
    /**
     * Prints the current game level.
     */
    public void printGameLevel (){
        gameLevel.printLevel();
    }
        
    /**
     * Makes a step in the game by moving the player in the specified direction.
     *
     * @param d the direction to move the player.
     * @return true if the player was successfully moved, false otherwise.
     */
    public boolean step(Direction d) {
        return gameLevel.movePlayer(d);
    }
    
    /**
     * Moves the dragon in the specified direction.
     *
     * @param d the direction to move the dragon.
     * @return true if the dragon was successfully moved, false otherwise.
     */

    public boolean moveDragon(Direction d) {
        return gameLevel.moveDragon(d);
    }
    
    /**
     * Returns the set of all available level IDs.
     *
     * @return a collection of all level IDs.
     */
    public Collection<Integer> getLevels() {
        return gameLevels.keySet();
    }
    
    /**
     * Checks if a game level is currently loaded.
     *
     * @return true if a level is loaded, false otherwise.
     */
    public boolean isLevelLoaded() {
        return gameLevel != null;
    }
    
    /**
     * Returns the number of rows in the current game level.
     *
     * @return the number of rows in the current level.
     */
    public int getLevelRows(){
        return gameLevel.rows;
    }
    
    /**
     * Returns the number of columns in the current game level.
     *
     * @return the number of columns in the current level.
     */
    public int getLevelCols(){
        return gameLevel.cols;
    }
    
    /**
     * Returns the item at the specified position in the current game level.
     *
     * @param row the row of the position.
     * @param col the column of the position.
     * @return the item at the given position.
     */
    public LevelItem getItem(int row, int col) {
        return gameLevel.level[row][col];
    }
    
    /**
     * Returns the GameID of the current level.
     *
     * @return the GameID of the current level, or null if no level is loaded.
     */
    public GameID getGameID() {
        return (gameLevel!=null) ? gameLevel.gameID : null;
    }
    
    /**
     * Returns the current position of the player in the game level.
     *
     * @return the position of the player.
     */
    public Position getPlayerPos() {
        return new Position(gameLevel.player.x, gameLevel.player.y);
    }
    
    /**
     * Returns the current position of the dragon in the game level.
     *
     * @return the position of the dragon.
     */
    public Position getDragonPos() {
        return new Position(gameLevel.dragon.x, gameLevel.dragon.y);
    }
    
    /**
     * Finds the position of the exit in the current game level.
     *
     * @return the position of the exit, or null if no exit is found.
     */
    public Position getExit() {
     for (int i = 0; i < gameLevel.rows; i++) {
        for (int j = 0; j < gameLevel.cols; j++) {
            if (gameLevel.level[i][j] == LevelItem.EXIT) {
                return new Position(j, i);
                }
            }
        }   
     return null; 
    }
    
    /**
     * Returns the current game level object.
     *
     * @return the current game level.
     */
    public GameLevel getGameLevel(){
        return gameLevel ;
    }
    
    /**
     * Reads and loads levels from the levels.txt resource file.
     */
    private void readLevels() {
        InputStream is;
        is = ResourceLoader.loadResource("res/levels.txt");
        
        try (Scanner sc = new Scanner(is)) {
            String line = readNextLine(sc);
            ArrayList<String> gameLevelRows = new ArrayList<>();
            
            while (!line.isEmpty()){
                GameID id = readGameID(line);
                if (id == null) return;
                
                gameLevelRows.clear();
                line = readNextLine(sc);
                while (!line.isEmpty() && line.trim().charAt(0) != ';') {
                    gameLevelRows.add(line);
                    line = readNextLine(sc);
                }
                addNewGameLevel(new GameLevel (gameLevelRows, id));
            }
        } catch (Exception e) {
            System.out.println("Reading Error");
        }
    }
    
    
    /**
     * Adds a new game level to the levels map.
     *
     * @param gameLevel the game level to add.
     */
    private void addNewGameLevel(GameLevel gameLevel) 
    { 
        gameLevels.put(gameLevel.gameID.level, gameLevel); 
    }
    
    /**
     * Reads the next non-empty line from the scanner.
     *
     * @param sc the scanner to read from.
     * @return the next non-empty line.
     */
    private String readNextLine(Scanner sc) {
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()){
            line = sc.nextLine();
        }
        return line;
    }
    
    /**
     * Reads and returns a GameID from the provided line.
     *
     * @param line the line to parse the GameID from.
     * @return the GameID, or null if it cannot be parsed.
     */
    private GameID readGameID(String line) {
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != ';') return null;
        Scanner s = new Scanner(line);
        s.next();
        if (!s.hasNextInt()) return null;
        int id = s.nextInt();
        return new GameID(id);
    }
}
