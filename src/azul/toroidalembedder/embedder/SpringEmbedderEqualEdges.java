/*
 * DefaultEmbedder.java
 *
 * Created on January 10, 2008, 4:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.graph.Graph;
import azul.toroidalembedder.graph.Edge;
import azul.toroidalembedder.graph.Vertex;

import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class SpringEmbedderEqualEdges implements Embedder{
    
    //private double edge_length = 0.3;
    //private double non_edge_length = 3*edge_length;
    private double force = 0.01;
    private double friction = 0.85;
    private Graph graph;
    private double[][] changes;
    private List<Vertex> vertices;
    
    
    /** Creates a new instance of DefaultEmbedder */
    public SpringEmbedderEqualEdges(Graph graph) {
        this.graph = graph;
        vertices = graph.getVertices();
    }

    public void embed() {
        if(changes==null)
            initialize();
        
        int counter = 0;
        double edge_length = 0.0;
        for (int i = 0; i<vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            for (Edge edge : vertex.getEdges()) {
                double dx = (vertex.getX(graph.getFundamentalDomain()) - edge.getEnd().getX(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                double dy = (vertex.getY(graph.getFundamentalDomain()) - edge.getEnd().getY(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                edge_length += Math.hypot(dx, dy);
                counter++;
            }
        }
        edge_length/=counter;

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
    
}