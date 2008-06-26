/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class AzulenoidInfo extends JPanel implements ListSelectionListener{
    
    private GraphListModel model;
    private JLabel numberLabel;

    public AzulenoidInfo(GraphListModel model) {
        this.model = model;
        if(model.getSelectedGraph()==null)
            model.getSelectionModel().setSelectionInterval(0, 0);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Number of azulenes per domain: "), gbc);
        gbc.gridx = 1;
        numberLabel = new JLabel("" +  model.getSelectedGraph().getVertices().size()/10);
        add(numberLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JButton symbol = new JButton("Original Delaney symbol");
        symbol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog((Frame)null, "Delaney symbol", true);
                dialog.setSize(360, 360);
                dialog.add(new JScrollPane(new JTextArea(AzulenoidInfo.this.model.getSelectedGraphGUIData().export())));
                dialog.setVisible(true);
            }
        });
        add(symbol, gbc);
        model.addListSelectionListener(this);
        setName("Azulenoids info");
    }

    public void valueChanged(ListSelectionEvent e) {
        numberLabel.setText("" +  model.getSelectedGraph().getVertices().size()/10);
    }

}
