/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui;

import javax.swing.event.ListDataListener;

/**
 *
 * @author nvcleemp
 */
public interface GraphListModelListener extends ListDataListener {
    
    public void selectedGraphChanged();

}
