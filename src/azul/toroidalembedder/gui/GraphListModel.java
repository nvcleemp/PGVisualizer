/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.general.Graph;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nvcleemp
 */
public interface GraphListModel extends ListModel {
    
    public Graph getGraph(int index);

    public GraphGUIData getGraphGUIData(int index);

    public String exportUpdatedGraphs();
    
    public Graph getSelectedGraph();
    
    public GraphGUIData getSelectedGraphGUIData();
    
    public void addGraphModelListener(GraphListModelListener l);
    
    public void removeGraphModelListener(GraphListModelListener l);

    public ListSelectionModel getSelectionModel();

    public void addListSelectionListener(ListSelectionListener l);

    public void removeListSelectionListener(ListSelectionListener l);
    
    public void commitGraph(int index);
    
    public void commitSelectedGraph();
    
    public void revertGraph(int index);
    
    public void revertSelectedGraph();
    
    public String getString(int index);
    
    public void putGraph(Graph g, int index);

}
