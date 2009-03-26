package be.ugent.caagt.pg.visualizer.structures.jmol;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nvcleemp
 */
public class JmolToggleMenuItem extends JCheckBoxMenuItem{

    public JmolToggleMenuItem(String text, final String onAction, final String offAction, final JmolData data) {
        this(text, onAction, offAction, data, false);
    }

    public JmolToggleMenuItem(String text, final String onAction, final String offAction, final JmolData data, boolean isSelected) {
        super(text, isSelected);
        addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if(isSelected()){
                    data.getViewer().evalString(onAction);
                } else {
                    data.getViewer().evalString(offAction);
                }
            }
        });
    }

}
