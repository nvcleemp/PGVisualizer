/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class PGVisualizer extends JPanel{
    
    private GraphModel model;
    private TorusView view = new TorusView();

    public PGVisualizer(File file) {
        model = new GraphModel(file);
        setLayout(new BorderLayout());
        add(new ListSelectionNavigator(model.getSelectionModel(), model), BorderLayout.NORTH);
        view = new TorusView(model);
        add(view, BorderLayout.CENTER);
        JPanel controls = new JPanel();
        controls.add(new ViewController(view));
        controls.add(new GraphOperations(model));
        controls.add(new EmbedderControl(model));
        JButton output = new JButton("Output");
        output.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println(model.exportUpdatedGraphs());
            }
        });
        controls.add(output);
        add(controls, BorderLayout.SOUTH);
    }
    
}
