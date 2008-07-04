/*
 * DefaultEmbedder.java
 *
 * Created on January 10, 2008, 4:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.embedder;


import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class SpringEmbedder extends AbstractEmbedder {
    
    private double edge_length;
    private double non_edge_length;
    private double force;
    private double friction;
    private double[][] changes;
    private List<? extends Vertex> vertices;
    
    
    /** Creates a new instance of DefaultEmbedder */
    public SpringEmbedder(Graph graph) {
        this(graph, 0.3, 3, 0.01, 0.85);
    }

    public SpringEmbedder(GraphListModel graphModel) {
        this(graphModel, 0.3, 3, 0.01, 0.85);
    }

    public SpringEmbedder(Graph graph, double edge_length, double non_edge_length_factor, double force, double friction) {
        super(graph);
        this.edge_length = edge_length;
        this.non_edge_length = non_edge_length_factor * edge_length;
        this.force = force;
        this.friction = friction;
        vertices = graph.getVertices();
    }

    public SpringEmbedder(GraphListModel graphModel, double edge_length, double non_edge_length_factor, double force, double friction) {
        super(graphModel);
        this.edge_length = edge_length;
        this.non_edge_length = non_edge_length_factor * edge_length;
        this.force = force;
        this.friction = friction;
        vertices = graph.getVertices();
    }

    public void embed() {
        if(changes==null)
            initialize();
        
        for (int i = 0; i<vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            for (Edge edge : vertex.getEdges()) {
                double dx = (vertex.getX(graph.getFundamentalDomain()) - edge.getEnd().getX(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                double dy = (vertex.getY(graph.getFundamentalDomain()) - edge.getEnd().getY(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                double change = Math.hypot(dx, dy);
                if(change == 0){
                    dx = 1.0;
                    dy = 0.0;
                } else {
                    dx /= change;
                    dy /= change;
                }
                change = (change - edge_length)/edge_length;
                dx *= change * force;
                dy *= change * force;
                changes[i][0] -= dx;
                changes[i][1] -= dy;
            }            
        }
                
        for (int i=0; i < vertices.size(); i++)
            for (int j=0; j < 2; j++)
                changes[i][j] *= friction;

        for (int i=0; i < vertices.size(); i++)
            vertices.get(i).translate(changes[i][0], changes[i][1], graph.getFundamentalDomain());
    }

    public void initialize() {
        changes = new double[vertices.size()][2];
    }

    @Override
    protected void resetEmbedder() {
        changes = null;
        vertices = graph.getVertices();
    }
    
}
