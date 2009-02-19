/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
