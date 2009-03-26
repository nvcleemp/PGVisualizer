package be.ugent.caagt.pg.visualizer.structures.jmol;

import org.jmol.api.JmolViewer;

/**
 * Object that stores some data about the Jmol display. Currently it saves
 * the translucent state of atoms and bonds, because these need to be reset
 * each time the color changes.
 * 
 * @author nvcleemp
 */
public class JmolData {

    private JmolViewer viewer;
    private boolean atomsTranslucent = false;
    private boolean bondsTranslucent = false;

    public JmolData(JmolViewer viewer) {
        this.viewer = viewer;
    }

    public boolean areAtomsTranslucent() {
        return atomsTranslucent;
    }

    public void setAtomsTranslucent(boolean atomsTranslucent) {
        this.atomsTranslucent = atomsTranslucent;
    }

    public boolean areBondsTranslucent() {
        return bondsTranslucent;
    }

    public void setBondsTranslucent(boolean bondsTranslucent) {
        this.bondsTranslucent = bondsTranslucent;
    }

    public JmolViewer getViewer() {
        return viewer;
    }

}
