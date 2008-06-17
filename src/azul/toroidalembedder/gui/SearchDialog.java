/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
            public boolean accept(FaceOverview overview, int size, int number, AcceptanceSpecifier spec){
                return overview.getNumberOfFaces(size, spec)==number;
            }
            
            public String getDescription(){
                return "exact";
            }
        },
        AT_LEAST {
            public boolean accept(FaceOverview overview, int size, int number, AcceptanceSpecifier spec){
                return overview.getNumberOfFaces(size, spec)>=number;
            }
            
            public String getDescription(){
                return "at least";
            }
        },
        AT_MOST {
            public boolean accept(FaceOverview overview, int size, int number, AcceptanceSpecifier spec){
                return overview.getNumberOfFaces(size, spec)<=number;
            }
            
            public String getDescription(){
                return "at most";
            }
        };
        
        public abstract boolean accept(FaceOverview overview, int size, int number, AcceptanceSpecifier spec);
        public abstract String getDescription();
    }
    
    private class FaceOverview {
        
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
    
    private class SearchCriterium {
        private SearchSpecifier spec;
        private AcceptanceSpecifier acceptanceSpec;
        private int size;
        private int number;

        public SearchCriterium(SearchSpecifier spec, AcceptanceSpecifier acceptanceSpec, int size, int number) {
            this.spec = spec;
            this.acceptanceSpec = acceptanceSpec;
            this.size = size;
            this.number = number;
        }
        
        public boolean accept(FaceOverview overview){
            return spec.accept(overview, size, number, acceptanceSpec);
        }
        
        @Override
        public String toString(){
            return spec.getDescription() + " " + number + " faces of size " + acceptanceSpec.getDescription() + " " + size;
         }
    }
    
    private GraphModel graphListModel;
    private JProgressBar progress;
    private ArrayModel<SearchCriterium> criteria = new ArrayModel<SearchCriterium>();
    private JFrame frame;
    private PGVisualizer visualizer;
    
    public SearchDialog(GraphModel graphListModel){
        setTitle("Search");
        this.graphListModel = graphListModel;
        progress = new JProgressBar(0, graphListModel.getSize());
        progress.setStringPainted(true);
        progress.setString("");
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
                criteria.addElement(new SearchCriterium((SearchSpecifier)comboBox.getSelectedItem(), (AcceptanceSpecifier)acceptanceComboBox.getSelectedItem(), (Integer)sizeField.getValue(), (Integer)numberField.getValue()));
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
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(comboBoxPanel, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 4;
        JList criteriaList = new JList(criteria);
        criteriaList.setPreferredSize(new Dimension(400, 200));
        add(criteriaList, gbc);
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        JButton filter = new JButton("Filter");
        filter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        if(frame==null){
                            frame = new JFrame("PGVisualizer");
                        } else {
                            frame.setVisible(false);
                            if(visualizer!=null)
                                frame.remove(visualizer);
                        }
                        visualizer = new PGVisualizer(searchList());
                        frame.add(visualizer);
                        frame.setJMenuBar(visualizer.getMenuBar(frame));
                        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                        frame.pack();
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                        frame.setVisible(true);
                    }
                });
                t.start();
            }
        });
        add(filter, gbc);
        gbc.gridx++;
        add(new JButton("Close"), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        add(progress, gbc);
        pack();
    }
    
    private GraphModel searchList(){
        progress.setString("Filtering graphs");
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < graphListModel.getSize(); i++) {
            FaceOverview overview = getFaceOverview(graphListModel.getGraphGUIData(i));
            boolean allowed = true;
            for (SearchCriterium searchCriterium : criteria.getList()) {
                allowed = allowed && searchCriterium.accept(overview);
            }
            if(allowed)
                list.add(i);
            progress.setValue(i+1);
        }
        progress.setString("Done");
        return new GraphModel(graphListModel, list);
    }
    
    private FaceOverview getFaceOverview(GraphGUIData data){
        String ds = data.getComment().trim();
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
   
    public static void main(String[] args) {
        GraphModel model = new GraphModel(new File("/Users/nvcleemp/edward/azulenoids-all.pg"));
        SearchDialog d = new SearchDialog(model);
        d.setDefaultCloseOperation(SearchDialog.HIDE_ON_CLOSE);
        d.setModal(false);
        d.setVisible(true);
        //System.exit(0);
        d.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

}
