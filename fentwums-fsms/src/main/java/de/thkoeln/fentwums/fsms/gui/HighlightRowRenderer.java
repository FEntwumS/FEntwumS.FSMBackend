package de.thkoeln.fentwums.fsms.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Manage the highlighting of the current row.
 *
 * @author Linus Schoendorf
 */
public class HighlightRowRenderer implements TableCellRenderer {

    public static final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

    private int highlightedRow = -1;
    private Color highlightColor = new Color(130, 200, 230);
    private Color defaultColor = Color.WHITE;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (highlightedRow == row) {
            c.setBackground(highlightColor);
        } else {
            c.setBackground(defaultColor);
        }
        return c;
    }

    public void setHighlightedRow(int index) {
        this.highlightedRow = index;
    }

    public int getHighlightedRow() {
        return highlightedRow;
    }
}
