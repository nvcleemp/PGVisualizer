/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class ListSelectionNavigator extends JPanel implements ListSelectionListener, ListDataListener{
    
    private ListSelectionModel selectionModel;
    private ListModel model;
    private JButton start = new JButton("|<<");
    private JButton left = new JButton("<");
    private JButton right = new JButton(">");
    private JButton end = new JButton(">>|");
    private JFormattedTextField currentSelection = new JFormattedTextField(1);
    private JLabel total = new JLabel("/ ");
    private JButton enter = new JButton("goto");

    public ListSelectionNavigator(ListSelectionModel selectionModel, ListModel model) {
        this.selectionModel = selectionModel;
        this.model = model;
        selectionModel.addListSelectionListener(this);
        model.addListDataListener(this);
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModel.setSelectionInterval(0, 0);
        total.setText("/ " + model.getSize());
        currentSelection.setHorizontalAlignment(JFormattedTextField.RIGHT);
        currentSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSelection((Integer)currentSelection.getValue()-1);
            }
        });
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSelection(0);
            }
        });
        left.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSelection(ListSelectionNavigator.this.selectionModel.getMinSelectionIndex()-1);
            }
        });
        right.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSelection(ListSelectionNavigator.this.selectionModel.getMinSelectionIndex()+1);
            }
        });
        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSelection((Integer)currentSelection.getValue()-1);
            }
        });
        end.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSelection(ListSelectionNavigator.this.model.getSize() - 1);
            }
        });
        setLayout(new GridLayout(0,7));
        add(start);
        add(left);
        add(currentSelection);
        add(total);
        add(enter);
        add(right);
        add(end);
    }

    public void valueChanged(ListSelectionEvent e) {
        left.setEnabled(selectionModel.getMinSelectionIndex()!=0);
        right.setEnabled(selectionModel.getMaxSelectionIndex()!=model.getSize()-1);
        currentSelection.setValue(selectionModel.getMinSelectionIndex() + 1);
    }

    public void intervalAdded(ListDataEvent e) {
        total.setText("/ " + model.getSize());
    }

    public void intervalRemoved(ListDataEvent e) {
        total.setText("/ " + model.getSize());
    }

    public void contentsChanged(ListDataEvent e) {
        total.setText("/ " + model.getSize());
    }
    
    private void setSelection(int position){
        if(position<0){
            position=0;
        } else if(position>=model.getSize()){
            position=model.getSize()-1;
        }
        selectionModel.setSelectionInterval(position, position);
    }
    
    public static void main(String[] args) {
        javax.swing.DefaultListModel model = new javax.swing.DefaultListModel();
        for (int i = 0; i < 10; i++)
            model.addElement("test " + i);
        javax.swing.JFrame frame = new javax.swing.JFrame("Demo");
        javax.swing.JList list = new javax.swing.JList(model);
        frame.setLayout(new java.awt.BorderLayout());
        frame.add(list, java.awt.BorderLayout.CENTER);
        frame.add(new ListSelectionNavigator(list.getSelectionModel(), model), java.awt.BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
