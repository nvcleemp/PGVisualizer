/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
