/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;
import java.net.URL;
import java.sql.*;
import javax.swing.*;
import model.*;
import persistence.*;

public class MainWindow extends JFrame{
    private final Game game;
    private Board board;
    private final JLabel gameStatLabel;
    private JLabel playerNameLabel;
    private Timer dragonMovementTimer;
    private Timer gameTimer;  
    private JLabel timeLabel;
    private int elapsedTime;
    public String playerName;
    public int numLabs;
    private Database db;
    
    /**
     * Constructor for the MainWindow that sets up the user interface and initializes 
     * game components such as the player name, menu, and event listeners.
     * 
     * @throws IOException if there is an error loading images.
     * @throws SQLException if there is an error connecting to the database.
     */
    public MainWindow() throws IOException, SQLException{
        game = new Game();
        db = new Database();
        
        setTitle("Labyrinth");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("res/player.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        
        playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Player Name", JOptionPane.QUESTION_MESSAGE); 
        if (playerName == null || playerName.trim().isEmpty()) { 
            playerName = "Anonymous";
        } 
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        JMenu menuGameLevel = new JMenu("Levels");
        //JMenu menuGameScale = new JMenu("Zoom");
        createGameLevelMenuItems(menuGameLevel);
        //createScaleMenuItems(menuGameScale, 1.0, 2.0, 0.5);
        
        JMenuItem menuGameRestart = new JMenuItem(new AbstractAction("Restart") { 
            @Override 
            public void actionPerformed(ActionEvent e) { 
                game.loadGame(game.getGameID()); 
                refreshGameStatLabel();
                board.resetGame();
                startDragonMovement();
                startGameTimer();
                board.refresh(); 
                pack(); 
            } 
        });
        
        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            } 
         });
        
        JMenuItem menuHighScore = new JMenuItem(new AbstractAction("High Scores") { 
            @Override public void actionPerformed(ActionEvent e) { 
                displayHighScores(); 
            } 
        });
        
        menuGame.add(menuGameLevel);
        menuGame.addSeparator();
        menuGame.add(menuHighScore);
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
        menuBar.add(menuGameRestart);
        setJMenuBar(menuBar);
        
        setLayout(new BorderLayout(0, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        gameStatLabel = new JLabel("label");
        timeLabel = new JLabel("Time: 0");
        playerNameLabel = new JLabel("Player: " + playerName);
        topPanel.add(gameStatLabel);
        topPanel.add(timeLabel);
        topPanel.add(playerNameLabel);
        add(topPanel, BorderLayout.NORTH);
        
        try {
            add(board = new Board(game), BorderLayout.CENTER);
        }
        catch (IOException ex) {}

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);
                if (!game.isLevelLoaded()) return;
                int kk = ke.getKeyCode();
                Direction d = null;
                switch (kk){
                    case KeyEvent.VK_LEFT: d = Direction.LEFT; break;
                    case KeyEvent.VK_RIGHT: d = Direction.RIGHT; break;
                    case KeyEvent.VK_UP: d = Direction.UP; break;
                    case KeyEvent.VK_DOWN: d = Direction.DOWN; break;
                    case KeyEvent.VK_ESCAPE: game.loadGame(game.getGameID()); 
                }
                refreshGameStatLabel();
                //board.repaint();
                board.refresh();
                if (d!= null && game.step(d)){ //makes player move
                    if (game.getPlayerPos().equals(game.getExit())){
                        gameTimer.stop();
                        JOptionPane.showMessageDialog(MainWindow.this, "You've won!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                        dragonMovementTimer.stop();
                        numLabs++;
                        if (numLabs==10) {
                            JOptionPane.showMessageDialog(MainWindow.this, "You've finished all levels!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                            db.storeHighScore(playerName, numLabs);
                        }
                        else
                        {
                            game.loadGame(new GameID(numLabs+1));
                            refreshGameStatLabel();
                            board.resetGame();
                            startDragonMovement();
                            startGameTimer();
                            board.refresh(); 
                            pack(); 
                        }
                        
               
                    }
                    if (game.getGameLevel().isPlayerDead()) {
                        playerDied();

                    }
                }
            }
        });
        
        setResizable(false);
        setLocationRelativeTo(null);
        game.loadGame(new GameID(1));
        startDragonMovement();
        startGameTimer();
        board.refresh();
        pack();
        refreshGameStatLabel();
        setVisible(true);
    }
   
    /**
     * Starts the dragon's movement on a regular interval and checks if the player has died.
     */
    private void startDragonMovement() { 
        if (dragonMovementTimer != null) { 
            dragonMovementTimer.stop();
        }
        dragonMovementTimer = new Timer(500, new ActionListener() { 
            @Override 
            public void actionPerformed(ActionEvent e) { 
                if (!game.isLevelLoaded()) return; 
                Direction direction = game.getGameLevel().getCurrentDragonDirection(); 
                if (!game.getGameLevel().moveDragon(direction)) { 
                    game.getGameLevel().chooseRandomDirection(); 
                } 
                if (game.getGameLevel().isPlayerDead()) { 
                    dragonMovementTimer.stop();
                    playerDied();
                }
                board.refresh(); 
            } 
        }); 
        dragonMovementTimer.start(); 
    }
    
    /**
     * Starts the game timer that counts the elapsed time and updates the displayed time.
     */
    private void startGameTimer() { 
        if (gameTimer != null) { 
            gameTimer.stop();
        } 
        elapsedTime = 0; 
        timeLabel.setText("Time: " + elapsedTime); 
        gameTimer = new Timer(1000, new ActionListener() { 
            @Override 
            public void actionPerformed(ActionEvent e) { 
                elapsedTime++; 
                timeLabel.setText("Time: " + elapsedTime); 
            } 
        }); 
        gameTimer.start(); 
    }
    
    /**
     * Handles the event when the player dies (e.g., displays a message and resets the game).
     */
    private void playerDied(){
        JOptionPane.showMessageDialog(MainWindow.this, "You died!", "Game Over", JOptionPane.ERROR_MESSAGE);
        db.storeHighScore(playerName, numLabs);
        game.loadGame(new GameID(1)); numLabs = 0;
        refreshGameStatLabel();
        board.resetGame();
        startDragonMovement();
        startGameTimer();
        board.refresh(); 
        pack(); 
    }
    
     /**
     * Refreshes the game status label to reflect the current level and other game stats.
     */
    private void refreshGameStatLabel() {
    String stat = "Level: " + game.getGameID().level;
    gameStatLabel.setText(stat);
    }
    
     /**
     * Creates menu items for selecting different game levels.
     * 
     * @param menu The menu where level options will be added.
     */
    private void createGameLevelMenuItems(JMenu menu) 
    {  
        for (Integer level : game.getLevels()) {
            JMenuItem item = new JMenuItem(new AbstractAction("Level-" + level) {
                @Override 
                public void actionPerformed(ActionEvent e) { 
                    game.loadGame(new GameID(level));
                    startGameTimer();
                    refreshGameStatLabel();
                    board.refresh(); 
                    pack();
                } 
            }); 
            menu.add(item); 
        }
    } 
    
    /**
     * Displays the high scores in a new window.
     */
    private void displayHighScores() { 
            ArrayList<HighScore> highScores = db.getHighScores(); 
            new HighScoreWindow(highScores, this); 
        
    }
        
    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
