/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public class GraphOperations extends JPanel implements GraphModelListener {
    private Graph graph;
    private GraphModel model = null;
    private GraphShiftOperations shiftOperations;

    private GraphOperations() {
        setLayout(new BorderLayout());
        JPanel north = new JPanel(new GridLayout(0, 2));
        north.add(new JButton(new FlipXAction()));
        north.add(new JButton(new RotateLeftAction()));
        north.add(new JButton(new FlipYAction()));
        north.add(new JButton(new RotateRightAction()));
        add(north, BorderLayout.NORTH);
        shiftOperations = new GraphShiftOperations(false);
        add(shiftOperations, BorderLayout.SOUTH);
        setBorder(BorderFactory.createTitledBorder("Graph operations"));
    }
    
    public GraphOperations(Graph graph) {
        this();
        this.graph = graph;
        shiftOperations.setGraph(graph);
    }
    
    public GraphOperations(GraphModel model) {
        this(model.getSelectedGraph());
        this.model = model;
        model.addGraphModelListener(this);
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
        shiftOperations.setGraph(graph);
    }
    
    private class FlipXAction extends AbstractAction{
        
        public FlipXAction(){
            super("Flip horizontally");
        }
        
        public void actionPerformed(ActionEvent e) {
            for (Vertex vertex : graph.getVertices()){
                vertex.setRawCoordinates(-vertex.getRawX(), vertex.getRawY());
                for (Edge edge : vertex.getEdges())
                    edge.translateTartget(-2*edge.getTargetX(), 0);
            }
        }
    }
    
    private class FlipYAction extends AbstractAction{
        
        public FlipYAction(){
            super("Flip vertically");
        }
        
        public void actionPerformed(ActionEvent e) {
            for (Vertex vertex : graph.getVertices()){
                vertex.setRawCoordinates(vertex.getRawX(), -vertex.getRawY());
                for (Edge edge : vertex.getEdges())
                    edge.translateTartget(0, -2*edge.getTargetY());
            }
        }
    }
    
    private class RotateLeftAction extends AbstractAction{
        
        public RotateLeftAction(){
            super("Rotate left");
        }

        public void actionPerformed(ActionEvent e) {
            for (Vertex vertex : graph.getVertices()){
                vertex.setRawCoordinates(vertex.getRawY(), -vertex.getRawX());
                for (Edge edge : vertex.getEdges())
                    edge.translateTartget(-edge.getTargetX() + edge.getTargetY(), -edge.getTargetX() - edge.getTargetY());
            }
        }
    }
    
    private class RotateRightAction extends AbstractAction{
        
        public RotateRightAction(){
            super("Rotate right");
        }

        public void actionPerformed(ActionEvent e) {
            for (Vertex vertex : graph.getVertices()){
                vertex.setRawCoordinates(-vertex.getRawY(), vertex.getRawX());
                for (Edge edge : vertex.getEdges())
                    edge.translateTartget(-edge.getTargetX() - edge.getTargetY(), edge.getTargetX() - edge.getTargetY());
            }
        }
    }

    public void selectedGraphChanged() {
        if(model!=null)
            setGraph(model.getSelectedGraph());
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
    
}
