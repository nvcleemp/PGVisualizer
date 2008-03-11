/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Vertex;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class GraphOperations extends JPanel {
    private Graph graph;

    public GraphOperations(Graph graph) {
        this.graph = graph;
        setLayout(new GridLayout(0, 2));
        add(new JButton(new FlipXAction()));
        add(new JButton(new RotateLeftAction()));
        add(new JButton(new FlipYAction()));
        add(new JButton(new RotateRightAction()));
        setBorder(BorderFactory.createTitledBorder("Graph operations"));
    }
    
    public void setGraph(Graph graph){
        this.graph = graph;
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
    
}
