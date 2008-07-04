/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.graph.triangulation;

import be.ugent.twi.pg.graph.FundamentalDomain;
import be.ugent.twi.pg.graph.Edge;
import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.graph.Vertex;
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
