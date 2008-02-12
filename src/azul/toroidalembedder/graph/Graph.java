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
    
    private FundamentalDomain fundamentalDomain;
    
    /** Creates a new instance of Graph */
    public Graph() {
        fundamentalDomain = new FundamentalDomain();
    }

    public Graph(FundamentalDomain fundamentalDomain) {
        this.fundamentalDomain = fundamentalDomain;
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

    public void setFundamentalDomain(FundamentalDomain fundamentalDomain) {
        if(!this.fundamentalDomain.equals(fundamentalDomain)){
            FundamentalDomain oldDomain = this.fundamentalDomain;
            this.fundamentalDomain = fundamentalDomain;
            fireFundamentalDomainChanged(oldDomain);
        }
    }
    
    private void fireGraphChanged(){
        for (GraphListener listener : listeners) {
            listener.graphChanged();
        }
    }
    
    private void fireFundamentalDomainChanged(FundamentalDomain oldDomain){
        for (GraphListener listener : listeners) {
            listener.fundamentalDomainChanged(oldDomain);
        }
    }
    
    public void addGraphListener(GraphListener listener){
        listeners.add(listener);
    }

    public void vertexMoved() {
        fireGraphChanged();
    }
}
