/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.graph.triangulation;

import be.ugent.twi.pg.graph.FundamentalDomain;
import be.ugent.twi.pg.graph.VertexListener;
import be.ugent.twi.pg.graph.Edge;
import be.ugent.twi.pg.graph.Vertex;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class TriangulationNodeDelegate implements Vertex{
    
    private TriangulationNode delegate;
    private int index = -1;
    private List<TriangulationConnectionDelegate> edges = new ArrayList<TriangulationConnectionDelegate>();
    
    public TriangulationNodeDelegate(TriangulationNode delegate, int index){
        this.delegate = delegate;
        this.index = index;
    }

    void createEdges(TriangulatedGraphDelegate graph){
        for (TriangulationConnection connection : delegate.getConnections()) {
            edges.add(graph.getEdgeFor(connection));
        }
    }
    
    public void addVertexListener(VertexListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getDegree() {
        return edges.size();
    }

    public List<? extends Edge> getEdges() {
        return edges;
    }

    public int getIndex() {
        return index;
    }

    public double getRawX() {
        return delegate.getX();
    }

    public double getRawY() {
        return delegate.getY();
    }

    public double getX(FundamentalDomain fundamentalDomain) {
        return fundamentalDomain.transform(delegate.getX(), delegate.getY())[0];
    }

    public double getX(int domainX, int domainY, FundamentalDomain fundamentalDomain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getY(FundamentalDomain fundamentalDomain) {
        return fundamentalDomain.transform(delegate.getX(), delegate.getY())[1];
    }

    public double getY(int domainX, int domainY, FundamentalDomain fundamentalDomain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRawCoordinates(double x, double y) {
        delegate.setCoordinates(x, y);
    }

    public void translate(double dx, double dy, FundamentalDomain fundamentalDomain) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
