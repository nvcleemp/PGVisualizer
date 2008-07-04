/*
 * DefaultEmbedder.java
 *
 * Created on January 10, 2008, 4:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package be.ugent.twi.pg.embedder;


import be.ugent.twi.pg.graph.DefaultGraph;
import be.ugent.twi.pg.graph.Face;
import be.ugent.twi.pg.graph.FundamentalDomain;
import be.ugent.twi.pg.graph.Edge;
import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.graph.Vertex;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class SpringEmbedder2ZeroContractFaces extends AbstractEmbedder {
    
    private double edge_length = 0;
    private double non_edge_length;
    private double force;
    private double friction;
    private double[][] changes;
    private List<? extends Vertex> vertices;
    
    
    /** Creates a new instance of DefaultEmbedder */
    public SpringEmbedder2ZeroContractFaces(Graph graph) {
        this(graph, 3, 0.01, 0.85);
    }

    public SpringEmbedder2ZeroContractFaces(GraphListModel graphModel) {
        this(graphModel, 3, 0.01, 0.85);
    }

    public SpringEmbedder2ZeroContractFaces(Graph graph, double non_edge_length, double force, double friction) {
        super(graph);
        this.non_edge_length = non_edge_length;
        this.force = force;
        this.friction = friction;
        vertices = graph.getVertices();
    }

    public SpringEmbedder2ZeroContractFaces(GraphListModel graphModel, double non_edge_length, double force, double friction) {
        super(graphModel);
        this.non_edge_length = non_edge_length;
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
                //change = (change - edge_length)/edge_length;
                dx *= change * force;
                dy *= change * force;
                changes[i][0] -= dx;
                changes[i][1] -= dy;
            }            
        }
        
        for (Face f : ((DefaultGraph)graph).getFaces()) {
            //if(f.getSize() > 10){
                //calculate center of face
                int x = 0;
                int y = 0;
                FundamentalDomain d = graph.getFundamentalDomain();
                double centerX = f.getVertexAt(0).getX(x, y, d);
                double centerY = f.getVertexAt(0).getY(x, y, d);
                int[][] xy = new int[f.getSize()][2];
                xy[0][0] = x;
                xy[0][1] = y;
                for (int i = 1; i < f.getSize(); i++) {
                    List<? extends Edge> edges = f.getVertexAt(i-1).getEdges();
                    int j = 0;
                    while(j<edges.size() && !edges.get(j).getEnd().equals(f.getVertexAt(i)))
                        j++;
                    if(j<edges.size()){
                        x += edges.get(j).getTargetX();
                        y += edges.get(j).getTargetY();
                        centerX += f.getVertexAt(i).getX(x, y, d);
                        centerY += f.getVertexAt(i).getY(x, y, d);
                        xy[i][0] = x;
                        xy[i][1] = y;
                    } else {
                        throw new RuntimeException("incorrect face");
                    }
                }
                centerX /= f.getSize();
                centerY /= f.getSize();
                //calculated forces
                for (int i = 0; i < f.getSize(); i++) {
                    Vertex vertex = f.getVertexAt(i);
                    double dx = (vertex.getX(d) - (centerX - xy[i][0]*d.getHorizontalSide() - xy[i][1]*d.getHorizontalShift()));
                    double dy = (vertex.getY(d) - (centerY - xy[i][1]*d.getDomainHeight()));
                    double change = Math.hypot(dx, dy);
                    if(change == 0){
                        dx = 1.0;
                        dy = 0.0;
                    } else {
                        dx /= change;
                        dy /= change;
                    }
                    //change = (change - edge_length)/edge_length;
                    dx *= change * force*0.5;
                    dy *= change * force*0.5;
                    changes[vertex.getIndex()][0] -= dx;
                    changes[vertex.getIndex()][1] -= dy;
                }

           // }
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
