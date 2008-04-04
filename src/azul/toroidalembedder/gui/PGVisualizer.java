/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.BoxLayout;
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
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.add(new ViewController(view));
        controls.add(new GraphOperations(model));
        controls.add(new EmbedderControl(model));
        controls.add(new ExportControl(view, model));
        add(controls, BorderLayout.EAST);
    }
    
}
