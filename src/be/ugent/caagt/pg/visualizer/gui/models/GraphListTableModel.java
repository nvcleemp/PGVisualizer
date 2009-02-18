/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ugent.caagt.pg.visualizer.gui.models;

import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author nvcleemp
 */
public class GraphListTableModel implements TableModel {

    private GraphListModel graphListModel;

    public GraphListTableModel(GraphListModel graphListModel) {
        this.graphListModel = graphListModel;
    }

    public int getRowCount() {
        return graphListModel.getSize();
    }

    public int getColumnCount() {
        return TableColumnEnum.values().length;
    }

    public String getColumnName(int columnIndex) {
        return TableColumnEnum.values()[columnIndex].getName();
    }

    public Class<?> getColumnClass(int columnIndex) {
        return TableColumnEnum.values()[columnIndex].getColumnClass();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return TableColumnEnum.values()[columnIndex].getValue(rowIndex, graphListModel);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("This is an immutable model");
    }

    public void addTableModelListener(TableModelListener l) {
        // immutable model
    }

    public void removeTableModelListener(TableModelListener l) {
        // immutable model
    }
}
