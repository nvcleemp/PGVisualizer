/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.embedder.FastDomainAngleEmbedder;
import azul.toroidalembedder.embedder.FastDomainEdgeEmbedder;
import azul.toroidalembedder.embedder.RandomEmbedder;
import azul.toroidalembedder.embedder.SpringEmbedder;
import azul.toroidalembedder.embedder.SpringEmbedderEqualEdges;
import azul.toroidalembedder.embedder.TemperedSpringEmbedder;
import azul.toroidalembedder.embedder.TutteEmbedder;
import azul.toroidalembedder.energy.AngleEnergyCalculator;
import azul.toroidalembedder.energy.MeanEdgeLengthEnergyCalculator;
import azul.toroidalembedder.graph.general.Graph;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
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
        setBorder(BorderFactory.createTitledBorder("Embedder"));
    }
    
    public EmbedderControl(Graph graph){
        this();
        model.addEmbedder("Spring embedder", new SpringEmbedder(graph));
        model.setSelectedItem("Spring embedder");
        model.addEmbedder("Tempered Spring embedder", new TemperedSpringEmbedder(graph));
        model.addEmbedder("Spring embedder equal edges", new SpringEmbedderEqualEdges(graph));
        model.addEmbedder("Random embedder", new RandomEmbedder(graph));
        model.addEmbedder("Domain angle embedder using edge length", new FastDomainAngleEmbedder(graph, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain angle embedder using edge angles", new FastDomainAngleEmbedder(graph, 0.1, 1, new AngleEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge length", new FastDomainEdgeEmbedder(graph, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge angles", new FastDomainEdgeEmbedder(graph, 0.1, 1, new AngleEnergyCalculator()));
    }

    public EmbedderControl(GraphModel graphModel){
        this();
        model.addEmbedder("Spring embedder", new SpringEmbedder(graphModel));
        model.setSelectedItem("Spring embedder");
        model.addEmbedder("Spring embedder equal edges", new SpringEmbedderEqualEdges(graphModel));
        model.addEmbedder("Spring embedder minimal equal edges", new SpringEmbedderEqualEdges(graphModel));
        model.addEmbedder("Random embedder", new RandomEmbedder(graphModel));
        model.addEmbedder("Tutte embedder", new TutteEmbedder(graphModel));
        model.addEmbedder("Domain angle embedder using edge length", new FastDomainAngleEmbedder(graphModel, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain angle embedder using edge angles", new FastDomainAngleEmbedder(graphModel, 0.1, 1, new AngleEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge length", new FastDomainEdgeEmbedder(graphModel, 0.1, 1, new MeanEdgeLengthEnergyCalculator()));
        model.addEmbedder("Domain edge embedder using edge angles", new FastDomainEdgeEmbedder(graphModel, 0.1, 1, new AngleEnergyCalculator()));
    }

}
