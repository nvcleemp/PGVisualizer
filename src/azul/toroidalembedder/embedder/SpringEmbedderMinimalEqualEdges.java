/*
 * DefaultEmbedder.java
 *
 * Created on January 10, 2008, 4:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;


import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import azul.toroidalembedder.gui.GraphModel;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class SpringEmbedderMinimalEqualEdges extends AbstractEmbedder {
    
    //private double edge_length = 0.3;
    //private double non_edge_length = 3*edge_length;
    private double force = 0.01;
    private double friction = 0.85;
    private double[][] changes;
    private List<? extends Vertex> vertices;
    
    
    /** Creates a new instance of DefaultEmbedder */
    public SpringEmbedderMinimalEqualEdges(Graph graph) {
        super(graph);
        vertices = graph.getVertices();
    }

    public SpringEmbedderMinimalEqualEdges(GraphModel graphModel) {
        super(graphModel);
        vertices = graph.getVertices();
    }

    public void embed() {
        if(changes==null)
            initialize();
        
        int counter = 0;
        double edge_length = 0.3;
        for (Vertex vertex : vertices) {
            for (Edge edge : vertex.getEdges()) {
                double dx = (vertex.getX(graph.getFundamentalDomain()) - edge.getEnd().getX(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                double dy = (vertex.getY(graph.getFundamentalDomain()) - edge.getEnd().getY(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                double currentLength = Math.hypot(dx, dy);
                if(currentLength<edge_length)
                    edge_length = currentLength;
            }
        }

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