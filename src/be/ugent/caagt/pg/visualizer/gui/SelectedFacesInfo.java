/* SelectedFacesInfo.java
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

package be.ugent.caagt.pg.visualizer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author nvcleemp
 */
public class SelectedFacesInfo extends JPanel{
    
    private GraphFaceSelectionModel graphFaceSelectionModel;
    private TorusView view;
    private JTable table;
    
    private class SelectedFacesTableModel extends AbstractTableModel implements GraphFaceSelectionListener{

        public SelectedFacesTableModel() {
            graphFaceSelectionModel.addGraphFaceSelectionListener(this);
        }        

        public int getRowCount() {
            return graphFaceSelectionModel.getSelectedFaces().length;
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex==0)
                return rowIndex;
            else if(columnIndex==1)
                return graphFaceSelectionModel.getSelectedFaces()[rowIndex].getSize();
            else if(columnIndex==2){
                Color c = null;
                if(view.getFaceHighlight()!=null)
                    c = view.getFaceHighlight().getColorFor(graphFaceSelectionModel.getSelectedFaces()[rowIndex]);
                if(c == null)
                    c = FaceColorMapping.getColorFor(graphFaceSelectionModel.getSelectedFaces()[rowIndex].getSize());
                return c;
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex==0 || columnIndex==1)
                return Integer.class;
            else if(columnIndex==2){
                return Color.class;
            }
            return Object.class;
        }

        @Override
        public String getColumnName(int column) {
            return super.getColumnName(column);
        }
        
        

        public void graphFaceSelectionChanged() {
            fireTableDataChanged();
        }
    }

    public SelectedFacesInfo(GraphFaceSelectionModel graphFaceSelectionModel, TorusView view) {
        super(new BorderLayout());
        this.graphFaceSelectionModel = graphFaceSelectionModel;
        this.view = view;
        table = new JTable(new SelectedFacesTableModel());
        table.setPreferredSize(new Dimension(200, 100));
        table.setDefaultRenderer(Color.class, new ColorCellRenderer());
        setName("Selected faces info");
        add(table, BorderLayout.CENTER);
    }
    
    private class ColorCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(value == null){
                setOpaque(false);
                setText("none");
            } else if(value instanceof Color){
                setOpaque(true);
                setBackground((Color)value);
                setText(null);
            }
            return this;
        }
        
    }

}
