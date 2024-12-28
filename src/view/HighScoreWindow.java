/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author HP
 */
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import persistence.HighScore;

public class HighScoreWindow extends JDialog {
    private final JTable table;

    /**
     * Constructor for HighScoreWindow that initializes the table with high scores.
     * 
     * @param highScores The list of high scores to be displayed.
     * @param parent The parent JFrame for this dialog window.
     */
    public HighScoreWindow(ArrayList<HighScore> highScores, JFrame parent) {
        super(parent, true);
        table = new JTable(new HighScoreTableModel(highScores));
        table.setFillsViewportHeight(true);

        TableRowSorter<TableModel> sorter = 
            new TableRowSorter<TableModel>(table.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter.setComparator(1, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                // Ensure we are comparing Integer values
                Integer score1 = (Integer) o1;
                Integer score2 = (Integer) o2;
                return score1.compareTo(score2);
            }
});
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);

        add(new JScrollPane(table));
        setSize(400, 400);
        setTitle("Highscores");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

