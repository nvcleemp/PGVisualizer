/* TriangulationNodeDelegate.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.graph.triangulation;

import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.VertexListener;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Vertex;
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
