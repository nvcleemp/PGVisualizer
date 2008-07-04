/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.embedder;

import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractEmbedder implements Embedder{
    
    protected Graph graph;
    protected GraphListModel graphModel = null;
    
    public AbstractEmbedder(Graph graph){
        this.graph = graph;
    }

    public AbstractEmbedder(GraphListModel graphModel){
        this.graphModel = graphModel;
        this.graph = graphModel.getSelectedGraph();
        graphModel.addGraphModelListener(this);
        resetEmbedder();
    }

    public void selectedGraphChanged() {
        if(graphModel!=null){
            graph = graphModel.getSelectedGraph();
            resetEmbedder();
        }
    }

    public void intervalAdded(ListDataEvent e) {
        //
    }

    public void intervalRemoved(ListDataEvent e) {
        //
    }

    public void contentsChanged(ListDataEvent e) {
        //
    }

    protected abstract void resetEmbedder();

}
