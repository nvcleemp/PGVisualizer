/*
 * Vertex.java
 *
 * Created on January 10, 2008, 11:48 AM
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
public class Vertex {
    
    private List<Edge> edges = new ArrayList<Edge>();
    private List<VertexListener> listeners = new ArrayList<VertexListener>();
    private double x;
    private double y;
    private int index = -1;
    
    private FundamentalDomain.CoordinateAdjustment adjustment = new FundamentalDomain.CoordinateAdjustment();
    
    /** Creates a new instance of Vertex */
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX(FundamentalDomain fundamentalDomain){
        return getX(0, 0, fundamentalDomain);
    }
    
    public double getY(FundamentalDomain fundamentalDomain){
        return getY(0, 0, fundamentalDomain);
    }

    public double getX(int domainX, int domainY, FundamentalDomain fundamentalDomain){
        return fundamentalDomain.getOrigin(domainX, domainY).getX() + x;
    }
    
    public double getY(int domainX, int domainY, FundamentalDomain fundamentalDomain){
        return fundamentalDomain.getDomainHeight()*domainY + y;
    }

    public List<Edge> getEdges() {
        return edges;
    }
    
    public void addEdge(Edge e){
        if(equals(e.getStart()))
            edges.add(e);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public void translate(double dx, double dy, FundamentalDomain fundamentalDomain){
        fundamentalDomain.reduceCoordinates(adjustment, x + dx, y + dy);
        x = adjustment.x;
        y = adjustment.y;
        for (Edge edge : edges){
            edge.translateTartget(adjustment.horizontalTranslate,adjustment.verticalTranslate);
            edge.getInverse().translateTartget(-adjustment.horizontalTranslate,-adjustment.verticalTranslate);
        }
        fireVertexMoved();
    }
    

    private void fireVertexMoved(){
        for (VertexListener listener : listeners) {
            listener.vertexMoved();
        }
    }
    
    public void addGraphListener(VertexListener listener){
        listeners.add(listener);
    }
}
