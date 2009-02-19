/* ListSelectionNavigator.java
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

package be.ugent.caagt.pg.visualizer.gui.util;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
    private Action previousAction = new MoveAction("<", -1);
    private Action nextAction = new MoveAction(">", 1);
    private JButton start = new JButton("|<<");
    private JButton left = new JButton(previousAction);
    private JButton right = new JButton(nextAction);
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

    public Action getNextAction() {
        return nextAction;
    }

    public Action getPreviousAction() {
        return previousAction;
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

    private class MoveAction extends AbstractAction{
        
        private int step;

        public MoveAction(String name, int step) {
            super(name);
            this.step = step;
        }

        public void actionPerformed(ActionEvent e) {
            setSelection(ListSelectionNavigator.this.selectionModel.getMinSelectionIndex()+step);
        }
        
    }
}
