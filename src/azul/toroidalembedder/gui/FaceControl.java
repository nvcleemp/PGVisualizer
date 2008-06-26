/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Face;
import azul.toroidalembedder.gui.toggler.FillFacesToggler;
import azul.toroidalembedder.gui.toggler.Toggler;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public class FaceControl extends JPanel implements ListSelectionListener{
    
    private static final Color AZULENE_BLUE = new Color(0, 100, 200);
    
    private TorusView torusView;
    private GraphListModel graphListModel;

    public FaceControl(TorusView torusView, GraphListModel graphListModel) {
        this.torusView = torusView;
        this.graphListModel = graphListModel;
        graphListModel.addListSelectionListener(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        Toggler fillFaces = new FillFacesToggler(torusView);
        final JCheckBox drawFaces = fillFaces.getJCheckBox(); //new JCheckBox("Fill faces", torusView.isPaintFaces());
        //drawFaces.addItemListener(new ItemListener() {
        //    public void itemStateChanged(ItemEvent e) {
        //        FaceControl.this.torusView.setPaintFaces(drawFaces.isSelected());
        //    }
        //});
        add(drawFaces, gbc);
        final JSlider transparency = new JSlider(0, 255, torusView.getTransparency());
        transparency.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                FaceControl.this.torusView.setTransparency(transparency.getValue());
            }
        });
        gbc.gridx++;
        gbc.weightx = 1;
        add(transparency, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(new JButton(new ColorAction()), gbc);
        gbc.gridy++;
        add(new JButton(new RemoveColorAction()), gbc);
        setName("Face");
    }



    public void valueChanged(ListSelectionEvent e) {
        //TODO: still use?
    }
    
    private class ColorAction extends AbstractAction {
        
        public ColorAction() {
            super("Color selected faces");
        }

        public void actionPerformed(ActionEvent e) {
            if(torusView.getFaceHighlight()==null)
                torusView.setFaceHighlight(new DefaultFaceHightlighter());

            Color selected = JColorChooser.showDialog(torusView, "Select color", AZULENE_BLUE);
            if(selected != null){
                FaceHighlighter theHighlighter = torusView.getFaceHighlight();
                for (Face f : torusView.getFaceSelectionModel().getSelectedFaces()) {
                    theHighlighter.setColor(f, selected);
                }
                torusView.repaint();
            }
        }
    }

    private class RemoveColorAction extends AbstractAction {
        
        public RemoveColorAction() {
            super("Remove color of selected faces");
        }

        public void actionPerformed(ActionEvent e) {
            if(torusView.getFaceHighlight()==null)
                return;

            FaceHighlighter theHighlighter = torusView.getFaceHighlight();
            for (Face f : torusView.getFaceSelectionModel().getSelectedFaces()) {
                theHighlighter.setColor(f, null);
            }
            torusView.repaint();
        }
    }

}
