/* TriangulatedGraphDelegate.java
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
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class TriangulatedGraphDelegate implements Graph{
    
    private TriangulatedGraph delegate;
    private FundamentalDomain domain = new FundamentalDomain();
    private List<TriangulationNodeDelegate> vertices;
    private Map<TriangulationConnection,TriangulationConnectionDelegate> edgeMap = new HashMap<TriangulationConnection, TriangulationConnectionDelegate>();
    private Map<TriangulationNode,TriangulationNodeDelegate> vertexMap = new HashMap<TriangulationNode, TriangulationNodeDelegate>();

    public TriangulatedGraphDelegate(TriangulatedGraph delegate) {
        this.delegate = delegate;
        //int order = delegate.getNumberOfVertices() + delegate.getNumberOfEdges() + delegate.getNumberOfFaces();
        vertices = new ArrayList<TriangulationNodeDelegate>();
        for (TriangulationNode node : delegate.getVertices()) {
            TriangulationNodeDelegate vertex = new TriangulationNodeDelegate(node, vertices.size());
            vertices.add(vertex);
            vertexMap.put(node, vertex);
        }
        for (TriangulationNode node : delegate.getEdges()) {
            TriangulationNodeDelegate vertex = new TriangulationNodeDelegate(node, vertices.size());
            vertices.add(vertex);
            vertexMap.put(node, vertex);
        }
        for (TriangulationNode node : delegate.getFaces()) {
            TriangulationNodeDelegate vertex = new TriangulationNodeDelegate(node, vertices.size());
            vertices.add(vertex);
            vertexMap.put(node, vertex);
        }
        for (TriangulationNodeDelegate vertex : vertices) {
            vertex.createEdges(this);
        }

    }
    
    public double getEdgeLength(Edge e) {
        return e.getLength(domain);
    }

    public Vertex getVertex(int index) {
        return vertices.get(index);
    }

    public List<? extends Vertex> getVertices() {
        return vertices;
    }

    public void translate(double dx, double dy) {
        for (TriangulationNodeDelegate vertex : vertices) {
            vertex.translate(dx, dy, domain);
        }
    }

    public FundamentalDomain getFundamentalDomain() {
        return domain;
    }

    TriangulationConnectionDelegate getEdgeFor(TriangulationConnection connection){
        TriangulationConnectionDelegate edge = edgeMap.get(connection);
        if(edge==null){
            edge = new TriangulationConnectionDelegate(connection, vertexMap.get(connection.getStart()), vertexMap.get(connection.getEnd()));
            edgeMap.put(connection, edge);
            edgeMap.put(connection.getInverse(), new TriangulationConnectionDelegate(connection, vertexMap.get(connection.getEnd()), vertexMap.get(connection.getStart())));
            edge.setInverse(edgeMap.get(connection.getInverse()));
            edgeMap.get(connection.getInverse()).setInverse(edge);
        }
        return edge;
    }
}
