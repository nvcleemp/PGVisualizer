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
    
    /** Creates a new instance of Vertex */
    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getRawX(){
        return x;
    }

    public double getRawY(){
        return y;
    }

    public double getX(FundamentalDomain fundamentalDomain){
        return getX(0, 0, fundamentalDomain);
    }
    
    public double getY(FundamentalDomain fundamentalDomain){
        return getY(0, 0, fundamentalDomain);
    }

    public double getX(int domainX, int domainY, FundamentalDomain fundamentalDomain){
        return fundamentalDomain.getOrigin(domainX, domainY).getX() + (fundamentalDomain.transform(x, y))[0];
    }
    
    public double getY(int domainX, int domainY, FundamentalDomain fundamentalDomain){
        return fundamentalDomain.getOrigin(domainX, domainY).getY() + (fundamentalDomain.transform(x, y))[1];
    }

    public List<Edge> getEdges() {
        return edges;
    }
    
    public int getDegree() {
        return edges.size();
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

    public void setRawCoordinates(double x, double y) {
        if(x>=-1 && x<=1 && y>=-1 && y<=1){
            this.x=x;
            this.y=y;
            fireVertexMoved();
        } else {
            throw new IllegalArgumentException("Raw coordinates should be between -1 and 1.");
        }
    }
    
    public void translate(double dx, double dy, FundamentalDomain fundamentalDomain){
        double[] dcoords = fundamentalDomain.inverseTransform(dx, dy);
        x = x + dcoords[0];
        y = y + dcoords[1];
        
        int horizontalTranslate = 0;
        int verticalTranslate = 0;
        
        while(x<=-1){
            horizontalTranslate++;
            x+=2;
        }
        while(x>1){
            horizontalTranslate--;
            x-=2;
        }
        
        while(y<=-1){
            verticalTranslate++;
            y+=2;
        }
        while(y>1){
            verticalTranslate--;
            y-=2;
        }
        
        for (Edge edge : edges){
            edge.translateTartget(horizontalTranslate,verticalTranslate);
            edge.getInverse().translateTartget(-horizontalTranslate,-verticalTranslate);
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
