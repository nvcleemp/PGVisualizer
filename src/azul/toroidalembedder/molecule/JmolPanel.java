package azul.toroidalembedder.molecule;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;
import org.jmol.api.JmolSimpleViewer;

public class JmolPanel extends JPanel {

    private JmolSimpleViewer viewer;
    private PGJmolAdapter adapter;

    public JmolPanel() {
        super();
        adapter = new PGJmolAdapter();
        viewer = JmolSimpleViewer.allocateSimpleViewer(this, adapter);
    }

    public void setMolecule(Molecule molecule){
        adapter.setMolecule(molecule);
    }
    
    public JmolSimpleViewer getViewer() {
        return viewer;
    }
    
    final Dimension currentSize = new Dimension();
    final Rectangle rectClip = new Rectangle();
    final Dimension preferredSize = new Dimension(500, 300);

    @Override
    public void paint(Graphics g) {
        getSize(currentSize);
        g.getClipBounds(rectClip);
        viewer.renderScreenImage(g, currentSize, rectClip);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }
    
    
}
