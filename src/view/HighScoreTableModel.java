/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author HP
 */
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import persistence.HighScore;

public class HighScoreTableModel extends AbstractTableModel {
    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[] { "Name", "Labyrinths Solved"};

     /**
     * Constructor for HighScoreTableModel that initializes the list of high scores.
     * 
     * @param highScores The list of high scores to be displayed in the table.
     */
    public HighScoreTableModel(ArrayList<HighScore> highScores) {
        this.highScores = highScores;
    }

    /**
     * Returns the number of rows (high scores) in the table.
     * 
     * @return The number of rows in the table.
     */
    @Override
    public int getRowCount() {
        return highScores.size();
    }

    /**
     * Returns the number of columns in the table (fixed to two: Name and Labyrinths Solved).
     * 
     * @return The number of columns in the table.
     */
    @Override
    public int getColumnCount() {
        return colName.length;
    }

    /**
     * Returns the value for a specific cell in the table.
     * 
     * @param r The row index.
     * @param c The column index.
     * @return The value at the specified row and column.
     */
    @Override
    public Object getValueAt(int r, int c) {
        HighScore h = highScores.get(r);
        if (c == 0) return h.name;
        else if (c == 1) return h.score;
        return null;
        
    }

    /**
     * Returns the name of the column at the specified index.
     * 
     * @param i The column index.
     * @return The name of the column at the specified index.
     */
    @Override
    public String getColumnName(int i) {
        return colName[i];
    }
}