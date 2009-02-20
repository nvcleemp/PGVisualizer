/* FiniteStructureTextOutputDialog.java
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

import be.ugent.caagt.pg.preferences.PGPreferences;
import be.ugent.caagt.pg.visualizer.structures.Tiled3DStructure;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

/**
 *
 * @author nvcleemp
 */
public class FiniteStructureTextOutputDialog extends JDialog {
    
    private EnumListModel<ColumnType> fileStructure = 
            new EnumListModel<ColumnType>();
    private EnumListModel<ColumnType> columnChoices = 
            new EnumListModel<ColumnType>(ColumnType.values());
    private JList fileStructureList = new JList(fileStructure);
    private JList columnChoicesList = new JList(columnChoices);
    private JCheckBox addColumnNumbers = new JCheckBox("Add column numbers");
    private JCheckBox addRowNumbers = new JCheckBox("Add row numbers");
    private Tiled3DStructure structure;

    public FiniteStructureTextOutputDialog(Dialog owner) {
        super(owner);
        JPanel constructionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        fileStructureList.setPreferredSize(
                columnChoicesList.getPreferredSize());
        fileStructureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        columnChoicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.fill = GridBagConstraints.BOTH;
        constructionPanel.add(fileStructureList, gbc);
        gbc.gridx = 2;
        constructionPanel.add(columnChoicesList, gbc);
        gbc.gridheight = 1;
        gbc.gridx = 1;
        constructionPanel.add(new JButton(new AbstractAction("<") {
            public void actionPerformed(ActionEvent e) {
                fileStructure.addValue(columnChoices.getValueAt(
                        columnChoicesList.getSelectedIndex()));
            }
        }), gbc);
        gbc.gridy++;
        constructionPanel.add(new JButton(new AbstractAction("Remove") {
            public void actionPerformed(ActionEvent e) {
                fileStructure.remove(fileStructureList.getSelectedIndex());
            }
        }), gbc);
        gbc.gridy++;
        constructionPanel.add(new JButton(new AbstractAction("Up") {
            public void actionPerformed(ActionEvent e) {
                int index = fileStructureList.getSelectedIndex();
                fileStructure.moveUp(index);
                if(!(index<1 || index>=fileStructure.getSize()))
                    fileStructureList.setSelectedIndex(index-1);
            }
        }), gbc);
        gbc.gridy++;
        constructionPanel.add(new JButton(new AbstractAction("Down") {
            public void actionPerformed(ActionEvent e) {
                int index = fileStructureList.getSelectedIndex();
                fileStructure.moveDown(index);
                if(!(index<0 || index>=fileStructure.getSize()-1))
                    fileStructureList.setSelectedIndex(index+1);
            }
        }), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 3;
        constructionPanel.add(addColumnNumbers, gbc);
        gbc.gridy++;
        constructionPanel.add(addRowNumbers, gbc);
        gbc.gridy++;
        constructionPanel.add(new JButton(new AbstractAction("Save file") {
            public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser;
                    String dir = 
                            PGPreferences.getInstance().getStringPreference(
                              PGPreferences.Preference.CURRENT_DIRECTORY);
                    if(dir==null)
                        chooser = new JFileChooser();
                    else
                        chooser = new JFileChooser(new File(dir));
                    if(chooser.showSaveDialog(null)==
                            JFileChooser.APPROVE_OPTION){
                        File file = chooser.getSelectedFile();
                        PGPreferences.getInstance().setStringPreference(
                                PGPreferences.Preference.CURRENT_DIRECTORY,
                                file.getParent());
                        if(!file.exists() || JOptionPane.showConfirmDialog(null,
                                "Overwrite file " + file.toString(),
                                "Confirm",
                                JOptionPane.YES_NO_OPTION)
                                == JOptionPane.YES_OPTION){
                            if(writeFile(file,
                                    structure,
                                    fileStructure.getList(),
                                    addRowNumbers.isSelected(),
                                    addColumnNumbers.isSelected()))
                                setVisible(false);
                        }
                    }
            }
        }), gbc);
        add(constructionPanel);
        pack();
    }
    
    public void showDialog(Tiled3DStructure structure){
        this.structure = structure;
        setVisible(true);
    }
    
    private boolean writeFile(File f, Tiled3DStructure structure,
            List<ColumnType> columns, boolean rowNumber, boolean columnNumber){
        FileWriter fw = null;
        try{
            fw = new FileWriter(f);
            if(columnNumber){
                if(rowNumber){
                    fw.write("\t");
                }
                int nrOfColumns = 0;
                for (ColumnType column : columns) {
                    nrOfColumns+=column.getNumberOfColumns(structure);
                }
                for (int i = 0; i < nrOfColumns; i++) {
                    fw.write(Integer.toString(i+1));
                    fw.write("\t");
                }
                fw.write("\n");
            }
            for (int i = 0; i < structure.getSize(); i++) {
                if(rowNumber){
                    fw.write(Integer.toString(i+1));
                    fw.write("\t");
                }
                for (int j = 0; j < columns.size(); j++) {
                    columns.get(j).writeCell(i, fw, structure);
                }
                fw.write("\n");
            }
            return true;
        } catch(IOException ioe) {
            try {
                if(fw!=null)
                    fw.close();
            } catch (IOException ex) {
                Logger.getLogger(FiniteStructureTextOutputDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }
    }
    
    public static enum ColumnType {
        X{
            public void writeCell(int row, FileWriter fw, Tiled3DStructure structure) throws IOException{
                fw.write(Float.toString(structure.getX(row)));
                fw.write("\t");
            }
        },
        Y{
            public void writeCell(int row, FileWriter fw, Tiled3DStructure structure) throws IOException{
                fw.write(Float.toString(structure.getY(row)));
                fw.write("\t");
            }
        },
        Z{
            public void writeCell(int row, FileWriter fw, Tiled3DStructure structure) throws IOException{
                fw.write(Float.toString(structure.getZ(row)));
                fw.write("\t");
            }
        },
        DEGREE{
            public void writeCell(int row, FileWriter fw, Tiled3DStructure structure) throws IOException{
                fw.write(Integer.toString(structure.getDegree(row)));
                fw.write("\t");
            }
        },
        CONNECTIONS{
            public void writeCell(int row, FileWriter fw, Tiled3DStructure structure) throws IOException{
                int i = 0;
                for (; i < structure.getDegree(row); i++) {
                    fw.write(Integer.toString(structure.adjacentTo(row, i)+1));
                    fw.write("\t");
                }
                for (; i < structure.getMaximumDegree(); i++) {
                    fw.write("0\t");
                }
            }
            
            @Override
            public int getNumberOfColumns(Tiled3DStructure structure){
                return structure.getMaximumDegree();
            }
        };
        
        
        public abstract void writeCell(int row, FileWriter fw, Tiled3DStructure structure) throws IOException;
        
        public int getNumberOfColumns(Tiled3DStructure structure){
            return 1;
        }
    }
    
    private static class EnumListModel<E extends Enum> extends AbstractListModel{
        
        private List<E> list;

        public EnumListModel() {
            list = new ArrayList<E>();
        }
        
        public EnumListModel(E[] values){
            this();
            for (E e : values) {
                list.add(e);
            }
        }

        public int getSize() {
            return list.size();
        }

        public Object getElementAt(int index) {
            return list.get(index);
        }
        
        public E getValueAt(int index) {
            return list.get(index);
        }
        
        public void addValueAt(int index, E e) {
            list.add(index, e);
            fireIntervalAdded(this, index, index);
        }
        
        public void addValue(E e) {
            addValueAt(list.size(), e);
        }
        
        public List<E> getList(){
            return new ArrayList<E>(list);
        }
        
        public void empty(){
            int size = list.size();
            for (int i = list.size(); i > 0; i--) {
                list.remove(i-1);
            }
            fireIntervalRemoved(this, 0, size - 1);
        }
        
        public void remove(int index){
            if(index<0 || index>=list.size())
                return;
            list.remove(index);
            fireIntervalRemoved(this, index, index);
        }
        
        public void moveUp(int index){
            if(index<1 || index>=list.size())
                return;
            E e = list.remove(index);
            list.add(index - 1, e);
            fireContentsChanged(this, index-1, index);
        }
        
        public void moveDown(int index){
            if(index<0 || index>=list.size()-1)
                return;
            E e = list.remove(index);
            list.add(index + 1, e);
            fireContentsChanged(this, index, index+1);
        }
    }
}
