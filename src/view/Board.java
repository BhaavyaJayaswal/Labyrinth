/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import model.*;
import res.ResourceLoader;
import java.lang.Math;

    
public class Board extends JPanel{
    private Game game;
    private final Image player, wall, empty, dragon, dark;
    private double scale;
    private int scaled_size;
    private final int tile_size = 32;
    
    /**
     * Constructor for Board that initializes the game and loads all necessary images.
     * 
     * @param g The Game object to associate with the board.
     * @throws IOException if there is an error loading the images.
     */
    public Board(Game g) throws IOException{
        game = g;
        scale = 1.0;
        scaled_size = (int)(scale * tile_size);
        player = ResourceLoader.loadImage("res/player.png");
        wall = ResourceLoader.loadImage("res/wall.jpg");
        empty = ResourceLoader.loadImage("res/empty.png");
        dragon = ResourceLoader.loadImage("res/dragon.png");
        dark = ResourceLoader.loadImage("res/dark.jpeg");
    }
    
    /**
     * Refreshes the board by updating its size and re-rendering the level.
     * 
     * @return true if the level was successfully refreshed, false otherwise.
     */
    public boolean refresh(){
        if (!game.isLevelLoaded()) return false;
        Dimension dim = new Dimension(game.getLevelCols() * scaled_size, game.getLevelRows() * scaled_size);
        setPreferredSize(dim);
        setSize(dim);
        revalidate();
        repaint();
        
        return true;
    }
    
    /**
     * Resets the game level to its initial state.
     */
    public void resetGame() {
        game.getGameLevel().reset();
    }
    
    /**
     * Paints the game components on the board, including player, dragon, walls, etc.
     * This method is responsible for rendering the visual representation of the game.
     * 
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!game.isLevelLoaded()) return;
        Graphics2D gr = (Graphics2D) g;

        int w = game.getLevelCols();
        int h = game.getLevelRows();
        Position p = game.getPlayerPos(); // Player's position
        Position d = game.getDragonPos(); // Dragon's position

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Image img = null;
                LevelItem li = game.getItem(y, x);

                // Determine which image to render 
                if (Math.abs(x-p.x)>3 || Math.abs(y-p.y)>3) { 
                    img = dark; // Use dark image for areas outside visibility
                } else {
                    switch (li) {
                        case EXIT:
                            img = empty;
                            break;
                        case WALL:
                            img = wall;
                            break;
                        case EMPTY:
                            img = empty;
                            break;
                        case PLAYER_AT_EXIT:
                            img = player;
                            break;
                    }
                    if (p.x == x && p.y == y) img = player; // Player's position
                    if (d.x == x && d.y == y) img = dragon; // Dragon's position
                }

                // Draw the image on the grid
                if (img != null) {
                    gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, this);
                }
            }
        }
    }
}
