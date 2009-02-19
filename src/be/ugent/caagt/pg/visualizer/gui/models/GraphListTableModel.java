/* GraphListTableModel.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
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
