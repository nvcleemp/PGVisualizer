/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui;

import be.ugent.caagt.pg.visualizer.gui.models.GraphListTableModel;
import be.ugent.caagt.pg.visualizer.gui.models.TableColumnEnum;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author nvcleemp
 */
public class ListOverviewWindow extends JFrame{

    public ListOverviewWindow(GraphListModel graphListModel){
        JTable table = new JTable(new GraphListTableModel(graphListModel));
        table.setSelectionModel(graphListModel.getSelectionModel());
        TableColumnEnum[] tableColumns = TableColumnEnum.values();
        for (int i = 0; i < tableColumns.length; i++) {
            TableCellRenderer cr = table.getCellRenderer(1, i);
            if(cr != null){
                Component c = cr.getTableCellRendererComponent(table, TableColumnEnum.values()[i].getPrototypeCellValue(graphListModel), false, false, 1, 1);

                Font f = c.getFont();
                c.setFont(table.getFont());

                table.getColumnModel().getColumn(i).setPreferredWidth(c.getPreferredSize().width);
                if(!TableColumnEnum.values()[i].canGrowBeyondSetSize())
                    table.getColumnModel().getColumn(i).setMaxWidth(c.getPreferredSize().width);

                c.setFont(f);
            }
            
        }
        
        add(new JScrollPane(table));
        pack();
    }
}
