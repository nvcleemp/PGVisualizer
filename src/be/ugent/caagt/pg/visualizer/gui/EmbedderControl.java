/* EmbedderControl.java
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

import be.ugent.caagt.pg.embedder.FastDomainAngleEmbedder;
import be.ugent.caagt.pg.embedder.FastDomainEdgeEmbedder;
import be.ugent.caagt.pg.embedder.RandomEmbedder;
import be.ugent.caagt.pg.embedder.SpringEmbedder;
import be.ugent.caagt.pg.embedder.SpringEmbedder2Zero;
import be.ugent.caagt.pg.embedder.SpringEmbedder2ZeroContractFaces;
import be.ugent.caagt.pg.embedder.SpringEmbedderContractFaces;
import be.ugent.caagt.pg.embedder.SpringEmbedderEqualEdges;
import be.ugent.caagt.pg.embedder.TemperedSpringEmbedder;
import be.ugent.caagt.pg.embedder.TutteEmbedder;
import be.ugent.caagt.pg.embedder.energy.AngleEnergyCalculator;
import be.ugent.caagt.pg.embedder.energy.MeanEdgeLengthEnergyCalculator;
import be.ugent.caagt.pg.graph.Graph;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nvcleemp
 */
public class EmbedderControl extends JPanel{

    private EmbedderComboBoxModel model = new EmbedderComboBoxModel();
    private ActionListener embedAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if(model.getSelectedEmbedder()!=null)
                model.getSelectedEmbedder().embed();
        }
    };
    private Timer timer = new Timer(100, embedAction);
    private ChangeListener changeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            JButton button = (JButton)(e.getSource());
            ButtonModel model = button.getModel();
            if (model != null) {
                if (model.isPressed()) {
                    if (!timer.isRunning()) {
                        if(EmbedderControl.this.model.getSelectedEmbedder()!=null)
                            EmbedderControl.this.model.getSelectedEmbedder().initialize();
                        timer.start();
                    }
                } else if (timer.isRunning()) {
                    timer.stop();
                }
            }
        }
    };
    
    private EmbedderControl() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        JComboBox comboBox = new JComboBox(model);
        comboBox.setPrototypeDisplayValue("Spring embedder equal edges");
        add(comboBox, gbc);
        gbc.gridx = 1;
        JButton runEmbedder = new JButton("Run embedder");
        runEmbedder.addChangeListener(changeListener);
        timer.setInitialDelay(0);
        add(runEmbedder, gbc);
        setName("Embedder");
    }
    
    public EmbedderControl(Graph graph){
        this();
        model.addEmbedder("Spring embedder", new SpringEmbedder(graph));
        model.setSelectedItem("Spring embedder");
        model.addEmbedder("Tempered Spring embedder", new TemperedSpringEmbedder(graph));
        model.addEmbedder("Spring embedder equal edges", new SpringEmbedderEqualEdges(graph));
        model.addEmbedder("Spring embedder to zero", new SpringEmbedder2Zero(graph));
        model.addEmbedder("Random embedder", new RandomEmbedder(graph));
        model.addEmbedder("Domain angle embedder using edge length", new FastDomainAngleEmbedder(graph, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain angle embedder using edge angles", new FastDomainAngleEmbedder(graph, 0.1, 1, new AngleEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge length", new FastDomainEdgeEmbedder(graph, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge angles", new FastDomainEdgeEmbedder(graph, 0.1, 1, new AngleEnergyCalculator()));
    }

    public EmbedderControl(GraphListModel graphModel){
        this();
        model.addEmbedder("Spring embedder", new SpringEmbedder(graphModel));
        model.setSelectedItem("Spring embedder");
        model.addEmbedder("Spring embedder equal edges", new SpringEmbedderEqualEdges(graphModel));
        model.addEmbedder("Spring embedder minimal equal edges", new SpringEmbedderEqualEdges(graphModel));
        model.addEmbedder("Spring embedder to zero", new SpringEmbedder2Zero(graphModel));
        model.addEmbedder("Spring embedder (contract faces)", new SpringEmbedderContractFaces(graphModel));
        model.addEmbedder("Spring embedder to zero (contract faces)", new SpringEmbedder2ZeroContractFaces(graphModel));
        model.addEmbedder("Random embedder", new RandomEmbedder(graphModel));
        model.addEmbedder("Tutte embedder", new TutteEmbedder(graphModel));
        model.addEmbedder("Domain angle embedder using edge length", new FastDomainAngleEmbedder(graphModel, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain angle embedder using edge angles", new FastDomainAngleEmbedder(graphModel, 0.1, 1, new AngleEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge length", new FastDomainEdgeEmbedder(graphModel, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge angles", new FastDomainEdgeEmbedder(graphModel, 0.1, 1, new AngleEnergyCalculator()));
    }

}
