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
public class Graph implements VertexListener, FundamentalDomainListener {
    
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private List<GraphListener> listeners = new ArrayList<GraphListener>();
    private List<Face> faces = new ArrayList<Face>();
    private FundamentalDomain fundamentalDomain;
    
    /** Creates a new instance of Graph */
    public Graph() {
        this(new FundamentalDomain());
    }

    public Graph(FundamentalDomain fundamentalDomain) {
        this.fundamentalDomain = fundamentalDomain;
        fundamentalDomain.addFundamentalDomainListener(this);
    }
    
    public List<Vertex> getVertices(){
        return vertices;
    }
    
    public Vertex getVertex(int index){
        int i = 0;
        while(i<vertices.size() && vertices.get(i).getIndex()!=index)
            i++;
        if(i==vertices.size())
            return null;
        else
            return vertices.get(i);
    }
    
    public boolean addVertex(Vertex vertex){
        if(vertex.getIndex()!=-1)
            return false;
        vertex.addGraphListener(this);
        vertex.setIndex(vertices.size());
        vertices.add(vertex);
        fireGraphChanged();
        return true;
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
    
    public double getEdgeLength(Edge e){
        return getEdgeLength(e, fundamentalDomain);
    }
    
    public double getEdgeLength(Edge e, FundamentalDomain domain){
        double dx = e.getStart().getX(domain) - e.getEnd().getX(e.getTargetX(), e.getTargetY(), domain);
        double dy = e.getStart().getY(domain) - e.getEnd().getY(e.getTargetX(), e.getTargetY(), domain);
        return Math.hypot(dx, dy);
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
            oldDomain.removeFundamentalDomainListener(this);
            this.fundamentalDomain = fundamentalDomain;
            fundamentalDomain.addFundamentalDomainListener(this);
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
    
    private void fireFundamentalDomainShapeChanged(){
        for (GraphListener listener : listeners) {
            listener.fundamentalDomainShapeChanged();
        }
    }
    
    public void addGraphListener(GraphListener listener){
        listeners.add(listener);
    }

    public void removeGraphListener(GraphListener listener){
        listeners.remove(listener);
    }

    public void vertexMoved() {
        fireGraphChanged();
    }

    public void fundamentalDomainShapeChanged() {
        fireFundamentalDomainShapeChanged();
    }
    
    public boolean addFace(Face f){
        if(f.getIndex()!=-1)
            return false;
        f.setIndex(faces.size());
        faces.add(f);
        return true;
    }
    
    public List<Face> getFaces(){
        return faces; //TODO: return copy of list
    }
}
