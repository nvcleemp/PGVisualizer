/* ExportExcelAction.java
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

package be.ugent.caagt.pg.visualizer.gui.action;

import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import be.ugent.caagt.pg.visualizer.gui.models.TableColumnEnum;
import be.ugent.caagt.pg.visualizer.gui.util.CheckBoxSelectionPanel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author nvcleemp
 */
public class ExportExcelAction extends AbstractAction{

    private GraphListModel graphListModel;
    private JFileChooser chooser = new JFileChooser();
    private CheckBoxSelectionPanel<TableColumnEnum> selectionPanel = CheckBoxSelectionPanel.buildPanelFromArray(TableColumnEnum.values());

    public ExportExcelAction(GraphListModel graphListModel) {
        super("Export Excel-importable file");
        this.graphListModel = graphListModel;

//        for (int i = 0; i < TableColumnEnum.values().length; i++) {
//            selectionPanel.addElement(TableColumnEnum.values()[i]);
//            selectionPanel.newLine();
//        }

        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel file", "xls"));
    }

    private void exportToFile(GraphListModel graphListModel, List<TableColumnEnum> columns, File file) throws IOException {
            FileWriter out = new FileWriter(file);

            for (int i = 0; i < columns.size(); i++) {
                out.write(columns.get(i).getName() + "\t");
            }
            out.write("\n");

            for (int i = 0; i < graphListModel.getSize(); i++) {
                for (int j = 0; j < columns.size(); j++) {
                    if(!Number.class.isAssignableFrom(columns.get(j).getColumnClass())){
                        out.write("\"" + columns.get(j).getValue(i, graphListModel).toString() + "\"" + "\t");
                    } else {
                        out.write(columns.get(j).getValue(i, graphListModel).toString() + "\t");
                    }
                }
                out.write("\n");
            }
            out.close();
        }

    public void actionPerformed(ActionEvent e) {
        selectionPanel.setAllSelected(false);
        JOptionPane.showMessageDialog(null, selectionPanel, "Select the columns", JOptionPane.QUESTION_MESSAGE);
        List<TableColumnEnum> columns = new ArrayList<TableColumnEnum>(selectionPanel.getSelectedElements());
        if(columns.size()>0 && chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            if(!file.getName().toLowerCase().endsWith(".xls")){
                file = new File(file.getParentFile(), file.getName() + ".xls");
            }
            if(file.isDirectory()){
                JOptionPane.showMessageDialog(null, "This file already exists and is a directory", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(!(file.exists() && JOptionPane.showConfirmDialog(null, "This file already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)){
                try {
                    exportToFile(graphListModel, columns, file);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "There was an error during saving. It is possible that the file is incomplete.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No file was saved", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
