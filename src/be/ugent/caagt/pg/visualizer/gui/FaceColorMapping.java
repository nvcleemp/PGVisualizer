/* FaceColorMapping.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author nvcleemp
 */
public class FaceColorMapping {
    //TODO: make this costumizable using preferences
    private static Map<Integer, Color> faceColorMap = new HashMap<Integer, Color>();
    
    static {
        faceColorMap.put(4, Color.WHITE);
        faceColorMap.put(5, Color.GRAY);
        faceColorMap.put(6, Color.YELLOW);
        faceColorMap.put(7, new Color(0.6f, 0.7f, 1.0f));
        faceColorMap.put(8, new Color(0f, 0.4f, 0f));
        faceColorMap.put(9, new Color(0.4f, 0f, 0f));
        faceColorMap.put(10, Color.PINK);
    }
    
    public static Color getColorFor(int i){
        return faceColorMap.get(i);
    }
    
    public static JPanel getDefaultColorTable(){
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable(new DefaultColorTableModel());
        table.setDefaultRenderer(Color.class, new ColorCellRenderer());
        table.setDefaultRenderer(Number.class, new NumberCellRenderer());
        panel.setName("Default Color Table");
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
    
    private static class DefaultColorTableModel extends AbstractTableModel{
        
        private List<Integer> keys;

        public DefaultColorTableModel() {
            keys = new ArrayList<Integer>();
            for (Integer integer : faceColorMap.keySet()) {
                int i = keys.size();
                while(i>0 && integer.compareTo(keys.get(i-1)) < 0)
                    i--;
                keys.add(i, integer);
            }
        }        

        public int getRowCount() {
            return keys.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex==0)
                return keys.get(rowIndex);
            else if(columnIndex==1){
                return getColorFor(keys.get(rowIndex));
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex==0)
                return Integer.class;
            else if(columnIndex==1){
                return Color.class;
            }
            return Object.class;
        }

        @Override
        public String getColumnName(int column) {
            if(column==0)
                return "size";
            else if(column==1)
                return "color";
            else
                return null;
        }
    }
    
    private static class ColorCellRenderer extends DefaultTableCellRenderer {
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

    private static class NumberCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            return this;
        }
        
    }
}
