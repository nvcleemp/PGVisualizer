/* SearchDialog.java
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

import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.visualizer.groups.WallpaperGroup;
import be.ugent.caagt.pg.visualizer.gui.util.CheckBoxSelectionPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author nvcleemp
 */
public class SearchDialog extends JDialog{

    private enum AcceptanceSpecifier {
        EQUAL{
            public boolean accept(int referenceSize, int size){
                return referenceSize == size;
            }

            public String getDescription(){
                return "equal to";
            }
        },
        GREATER{
            public boolean accept(int referenceSize, int size){
                return referenceSize < size;
            }

            public String getDescription(){
                return "greater than";
            }
        },
        SMALLER{
            public boolean accept(int referenceSize, int size){
                return referenceSize > size;
            }

            public String getDescription(){
                return "smaller than";
            }
        };

        public abstract boolean accept(int referenceSize, int size);
        public abstract String getDescription();
    }

    private enum SearchSpecifier {
        /*        NO {
        public boolean accept(FaceOverview overview, int size, int number){
        return overview.getNumberOfFacesOfSize(size)==0;
        }
        
        public String getDescription(){
        return "no";
        }
        },*/
        EXACT {
            public boolean accept(int input, int number){
                return input==number;
            }

            public String getDescription(){
                return "exact";
            }
        },
        AT_LEAST {
            public boolean accept(int input, int number){
                return input>=number;
            }

            public String getDescription(){
                return "at least";
            }
        },
        AT_MOST {
            public boolean accept(int input, int number){
                return input<=number;
            }

            public String getDescription(){
                return "at most";
            }
        };

        public abstract boolean accept(int input, int number);
        public abstract String getDescription();
    }

    private static class FaceOverview {

        private Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        public void addFace(int size){
            Integer i = map.get(size);
            if(i==null){
                map.put(size, 1);
            } else {
                i = i + 1;
                map.put(size, i);
            }
        }

        public int getNumberOfFacesOfSize(int size){
            return getNumberOfFaces(size, AcceptanceSpecifier.EQUAL);
        }

        public int getNumberOfFaces(int size, AcceptanceSpecifier spec){
            int count = 0;
            for (Integer integer : map.keySet()) {
                if(spec.accept(size, integer))
                    count += map.get(integer);
            }

            return count;
        }
    }
    
    private interface SearchCriterium {
        public boolean accept(Graph graph, GraphGUIData graphGUIData, FaceOverview overview);
    }

    private class FaceSearchCriterium implements SearchCriterium {
        private SearchSpecifier spec;
        private AcceptanceSpecifier acceptanceSpec;
        private int size;
        private int number;

        public FaceSearchCriterium(SearchSpecifier spec, AcceptanceSpecifier acceptanceSpec, int size, int number) {
            this.spec = spec;
            this.acceptanceSpec = acceptanceSpec;
            this.size = size;
            this.number = number;
        }

        public boolean accept(Graph graph, GraphGUIData graphGUIData, FaceOverview overview){
            return spec.accept(overview.getNumberOfFaces(size, acceptanceSpec), number);
        }

        @Override
        public String toString(){
            return spec.getDescription() + " " + number + " faces of size " + acceptanceSpec.getDescription() + " " + size;
        }
    }
    
    private class VertexSearchCriterium implements SearchCriterium {
        private SearchSpecifier spec;
        private int number;

        public VertexSearchCriterium(SearchSpecifier spec, int number) {
            this.spec = spec;
            this.number = number;
        }

        public boolean accept(Graph graph, GraphGUIData graphGUIData, FaceOverview overview){
            return spec.accept(graph.getVertices().size(), number);
        }

        @Override
        public String toString(){
            return  spec.getDescription() + " " + number + " vertices";
        }
    }

    private class GroupSearchCriterium implements SearchCriterium {

        private Set<WallpaperGroup> groups;
        private String name;

        public GroupSearchCriterium(Set<WallpaperGroup> groups) {
            this.groups = groups;
            StringBuilder sb = new StringBuilder();
            for (WallpaperGroup group : groups) {
                sb.append(group.toString());
                sb.append(" ");
            }
            name = sb.toString();
        }

        public boolean accept(Graph graph, GraphGUIData graphGUIData, FaceOverview overview) {
            return groups.contains(graphGUIData.getGroup());
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
    private GraphListModel graphListModel;
    private JProgressBar progress;
    private ArrayModel<SearchCriterium> criteria = new ArrayModel<SearchCriterium>();
    private JFrame frame;
    private PGVisualizer visualizer;

    public SearchDialog(GraphListModel graphListModel){
        setTitle("Search");
        this.graphListModel = graphListModel;
        progress = new JProgressBar(0, graphListModel.getSize());
        progress.setStringPainted(true);
        progress.setString("");
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.addTab("Face filters", constructFaceFilterPanel());
        tabs.addTab("Vertex filters", constructVertexFilterPanel());
        tabs.addTab("Group filter", constructGroupFilterPanel());
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(tabs, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 4;
        JList criteriaList = new JList(criteria);
        //criteria.addElement(new VertexSearchCriterium(SearchSpecifier.EXACT, 20));
        criteriaList.setPreferredSize(new Dimension(400, 200));
        add(criteriaList, gbc);
        criteriaList.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "delete_item");
        criteriaList.getActionMap().put("delete_item", new DeleteAction(criteriaList));
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        final JButton filter = new JButton("Filter");
        filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filter.setEnabled(false);
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        if(frame==null){
                            frame = new JFrame("PGVisualizer");
                        } else {
                            frame.setVisible(false);
                            if(visualizer!=null)
                                frame.remove(visualizer);
                        }
                        GraphListModel filteredList = searchList();
                        if(filteredList!=null){
                            visualizer = new PGVisualizer(filteredList);
                            frame.add(visualizer);
                            frame.setJMenuBar(visualizer.getMenuBar(frame));
                            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            frame.pack();
                            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            frame.setVisible(true);
                        }
                        filter.setEnabled(true);
                    }
                });
                t.start();
            }
        });
        add(filter, gbc);
        gbc.gridx++;
        add(new JButton(new AbstractAction("Close") {
            public void actionPerformed(ActionEvent e) {
                SearchDialog.this.setVisible(false);
            }
        }), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        add(progress, gbc);
        pack();
    }

    private GraphListModel searchList(){
        progress.setString("Filtering graphs");
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < graphListModel.getSize(); i++) {
            FaceOverview overview = getFaceOverview(graphListModel.getGraphGUIData(i));
            boolean allowed = true;
            for (SearchCriterium searchCriterium : criteria.getList()) {
                allowed = allowed && searchCriterium.accept(graphListModel.getGraph(i), graphListModel.getGraphGUIData(i), overview);
            }
            if(allowed)
                list.add(i);
            progress.setValue(i+1);
        }
        progress.setString("Done");
        if(list.size()==0)
            return null;
        else
            return new DelegateGraphListModel(graphListModel, list);
    }

    private FaceOverview getFaceOverview(GraphGUIData data){
        String ds = data.getSymbol();
        if(ds==null)
            return new FaceOverview();
        
        if(!ds.startsWith("<") || !ds.endsWith(ds))
            throw new RuntimeException("A .ds file should start with '<' and end with '>'");

        String[] parts = ds.substring(1, ds.length()-1).split(":");
        if(parts.length!=4)
            throw new RuntimeException("Illegal number of sections in file: expected 4, found " + parts.length);
            
        Scanner faceOrbits = new Scanner(parts[3].split(",")[0]);
        FaceOverview faces = new FaceOverview();
        while(faceOrbits.hasNextInt())
            faces.addFace(faceOrbits.nextInt());

        return faces;
    }

    private static class ArrayModel<E> implements ListModel {

        private List<E> list = new ArrayList<E>();
        private List<ListDataListener> listeners = new ArrayList<ListDataListener>();

        public int getSize() {
            return list.size();
        }

        public Object getElementAt(int index) {
            return list.get(index);
        }

        public void addElement(E element){
            list.add(element);
            fire();
        }

        public void removeElement(E element){
            list.remove(element);
            fire();
        }

        public E getElement(int index){
            return list.get(index);
        }

        public List<E> getList(){
            return list;
        }

        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        private void fire(){
            ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, list.size()-1);
            for (ListDataListener l : listeners) {
                l.contentsChanged(e);
            }
        }
    }

    private class DeleteAction extends AbstractAction {

        private JList list;

        public DeleteAction(JList list) {
            this.list = list;
        }

        public void actionPerformed(ActionEvent e) {
            for (Object object : list.getSelectedValues()) {
                criteria.removeElement((SearchCriterium) object);
            }
        }
    }
    
    private JComponent constructFaceFilterPanel(){
        final JComboBox comboBox = new JComboBox(SearchSpecifier.values());
        comboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(((SearchSpecifier)value).getDescription());
                return this;
            }
        });
        final JFormattedTextField numberField = new JFormattedTextField(Integer.valueOf(0));
        numberField.setColumns(3);
        final JComboBox acceptanceComboBox = new JComboBox(AcceptanceSpecifier.values());
        acceptanceComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(((AcceptanceSpecifier)value).getDescription());
                return this;
            }
        });
        final JFormattedTextField sizeField = new JFormattedTextField(Integer.valueOf(4));
        sizeField.setColumns(3);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                criteria.addElement(new FaceSearchCriterium((SearchSpecifier)comboBox.getSelectedItem(), (AcceptanceSpecifier)acceptanceComboBox.getSelectedItem(), (Integer)sizeField.getValue(), (Integer)numberField.getValue()));
            }
        });
        JPanel comboBoxPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        comboBoxPanel.add(comboBox, gbc);
        comboBoxPanel.add(numberField, gbc);
        comboBoxPanel.add(new JLabel("faces of size"), gbc);
        comboBoxPanel.add(acceptanceComboBox, gbc);
        comboBoxPanel.add(sizeField, gbc);
        comboBoxPanel.add(addButton, gbc);
        return comboBoxPanel;
    }

    private JComponent constructVertexFilterPanel(){
        final JComboBox comboBox = new JComboBox(SearchSpecifier.values());
        comboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(((SearchSpecifier)value).getDescription());
                return this;
            }
        });
        final JFormattedTextField numberField = new JFormattedTextField(Integer.valueOf(0));
        numberField.setColumns(3);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                criteria.addElement(new VertexSearchCriterium((SearchSpecifier)comboBox.getSelectedItem(), (Integer)numberField.getValue()));
            }
        });
        JPanel comboBoxPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        comboBoxPanel.add(comboBox, gbc);
        comboBoxPanel.add(numberField, gbc);
        comboBoxPanel.add(new JLabel("vertices"), gbc);
        comboBoxPanel.add(addButton, gbc);
        return comboBoxPanel;
    }

    private JComponent constructGroupFilterPanel(){
        JPanel groupFilterPanel = new JPanel(new BorderLayout());
        final CheckBoxSelectionPanel<WallpaperGroup> groupsPanel = new CheckBoxSelectionPanel<WallpaperGroup>();
        groupsPanel.addElement(WallpaperGroup.CM);
        groupsPanel.addElement(WallpaperGroup.PM);
        groupsPanel.addElement(WallpaperGroup.PG);
        groupsPanel.addElement(WallpaperGroup.P1);
        groupsPanel.newLine();
        groupsPanel.addElement(WallpaperGroup.C2MM);
        groupsPanel.addElement(WallpaperGroup.P2MM);
        groupsPanel.addElement(WallpaperGroup.P2MG);
        groupsPanel.addElement(WallpaperGroup.P2GG);
        groupsPanel.addElement(WallpaperGroup.P2);
        groupsPanel.newLine();
        groupsPanel.addElement(WallpaperGroup.P31M);
        groupsPanel.addElement(WallpaperGroup.P3M1);
        groupsPanel.addElement(WallpaperGroup.P3);
        groupsPanel.newLine();
        groupsPanel.addElement(WallpaperGroup.P4MM);
        groupsPanel.addElement(WallpaperGroup.P4GM);
        groupsPanel.addElement(WallpaperGroup.P4);
        groupsPanel.newLine();
        groupsPanel.addElement(WallpaperGroup.P6M);
        groupsPanel.addElement(WallpaperGroup.P6);
        groupsPanel.close();
        groupFilterPanel.add(groupsPanel, BorderLayout.CENTER);
        JButton addButton = new JButton(new AbstractAction("Add") {

            public void actionPerformed(ActionEvent e) {
                criteria.addElement(new GroupSearchCriterium(groupsPanel.getSelectedElements()));
            }
        });
        groupFilterPanel.add(addButton, BorderLayout.SOUTH);
        return groupFilterPanel;
    }
}
