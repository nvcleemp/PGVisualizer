/*
 * DefaultVertex.java
 *
 * Created on January 10, 2008, 11:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class DefaultVertex implements Vertex {
    
    private List<DefaultEdge> edges = new ArrayList<DefaultEdge>();
    private List<VertexListener> listeners = new ArrayList<VertexListener>();
    private double x;
    private double y;
    private int index = -1;
    
    /** Creates a new instance of DefaultVertex */
    public DefaultVertex(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * returns the x coordinate of the vertex in the unit square
     * 
     * @return the x coordinate of the vertex in the unit square
     */
    public double getRawX(){
        return x;
    }

    /**
     * returns the y coordinate of the vertex in the unit square
     * 
     * @return the y coordinate of the vertex in the unit square
     */
    public double getRawY(){
        return y;
    }

    /**
     * returns the x coordinate of the vertex in the given fundamental domain at (0,0)
     * 
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the x coordinate of the vertex in the given fundamental domain at (0,0)
     */
    public double getX(FundamentalDomain fundamentalDomain){
        return getX(0, 0, fundamentalDomain);
    }
    
    /**
     * returns the y coordinate of the vertex in the given fundamental domain at (0,0)
     * 
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the y coordinate of the vertex in the given fundamental domain at (0,0)
     */
    public double getY(FundamentalDomain fundamentalDomain){
        return getY(0, 0, fundamentalDomain);
    }

    /**
     * returns the x coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     * 
     * @param domainX The x coordinate of the required fundamental domain
     * @param domainY The y coordinate of the required fundamental domain
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the x coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     */
    public double getX(int domainX, int domainY, FundamentalDomain fundamentalDomain){
        return fundamentalDomain.getOrigin(domainX, domainY).getX() + (fundamentalDomain.transform(x, y))[0];
    }
    
    /**
     * returns the y coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     * 
     * @param domainX The x coordinate of the required fundamental domain
     * @param domainY The y coordinate of the required fundamental domain
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the y coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     */
    public double getY(int domainX, int domainY, FundamentalDomain fundamentalDomain){
        return fundamentalDomain.getOrigin(domainX, domainY).getY() + (fundamentalDomain.transform(x, y))[1];
    }

    /**
     * returns the list of edges that are incident with this vertex. All the edges will
     * have this vertex as start.
     * 
     * @return The list of edges that are incident with this vertex.
     */
    public List<DefaultEdge> getEdges() {
        return edges;
    }
    
    /**
     * returns the degree of this vertex.
     * 
     * @return The degree of this vertex.
     */
    public int getDegree() {
        return edges.size();
    }
    
    public void addEdge(DefaultEdge e){
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
    
    /**
     * Moves the vertex along the vector (<code>dx</code>, <code>dy</code>) in the coordinate system 
     * described by the <code>fundamentalDomain</code>.
     * 
     * @param dx The amount to move along the X-axis.
     * @param dy The amount to move along the Y-axis.
     * @param fundamentalDomain The system in which the amounts are given.
     */
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
        
        for (DefaultEdge edge : edges){
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
    
    public void addVertexListener(VertexListener listener){
        listeners.add(listener);
    }
}
