/*
 * Graph.java
 *
 * Created on January 10, 2008, 1:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class Graph implements VertexListener{
    
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private List<GraphListener> listeners = new ArrayList<GraphListener>();
    
    private FundamentalDomain fundamentalDomain = new FundamentalDomain();
    
    /** Creates a new instance of Graph */
    public Graph() {
    }
    
    public List<Vertex> getVertices(){
        return vertices;
    }
    
    public void addVertex(Vertex vertex){
        vertex.addGraphListener(this);
        vertex.setIndex(vertices.size());
        vertices.add(vertex);
        fireGraphChanged();
    }
    
    public Edge addEdge(int start, int end, int targetX, int targetY){
        return addEdge(vertices.get(start), vertices.get(end), targetX, targetY);
    }
    
    public Edge addEdge(Vertex start, Vertex end, int targetX, int targetY){
        if(!(vertices.contains(start) && vertices.contains(end)))
            return null;
        Edge e = Edge.getEdge(start, end, targetX, targetY);
        start.addEdge(e);
        end.addEdge(e.getInverse());
        fireGraphChanged();
        return e;
    }
    
    public void translate(double dx, double dy){
        for(Vertex vertex : vertices)
            vertex.translate(dx, dy, fundamentalDomain);
    }

    public FundamentalDomain getFundamentalDomain() {
        return fundamentalDomain;
    }
    
    private void fireGraphChanged(){
        for (GraphListener listener : listeners) {
            listener.graphChanged();
        }
    }
    
    public void addGraphListener(GraphListener listener){
        listeners.add(listener);
    }

    public void vertexMoved() {
        fireGraphChanged();
    }
}
