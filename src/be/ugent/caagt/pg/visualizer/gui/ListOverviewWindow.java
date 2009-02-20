/* ListOverviewWindow.java
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

import be.ugent.caagt.pg.visualizer.gui.models.GraphListTableModel;
import be.ugent.caagt.pg.visualizer.gui.models.TableColumnEnum;
import java.awt.Color;
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
        table.setGridColor(Color.LIGHT_GRAY);
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
