/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.graph.triangulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class TriangulatedGraph {
    
    List<TriangulationNode> vertices = new ArrayList<TriangulationNode>();
    List<TriangulationNode> edges = new ArrayList<TriangulationNode>();
    List<TriangulationNode> faces = new ArrayList<TriangulationNode>();
    Map<String,List<TriangulationNode>> map = new HashMap<String, List<TriangulationNode>>();
    
    {
        map.put("v", vertices);
        map.put("e", edges);
        map.put("f", faces);
    }

    public void addVertex(double x, double y){
        TriangulationNode node = new TriangulationNode(TriangulationNode.Type.VERTEX, x, y);
        node.setIndex(vertices.size());
        vertices.add(node);
    }
    
    public void addEdge(double x, double y){
        TriangulationNode node = new TriangulationNode(TriangulationNode.Type.EDGE, x, y);
        node.setIndex(edges.size());
        edges.add(node);
    }
    
    public void addFace(double x, double y){
        TriangulationNode node = new TriangulationNode(TriangulationNode.Type.FACE, x, y);
        node.setIndex(faces.size());
        faces.add(node);
    }
    
    public TriangulationConnection addConnection(String type1, int node1, String type2, int node2, int targetX, int targetY){
        TriangulationConnection connection = new TriangulationConnection(map.get(type1).get(node1), map.get(type2).get(node2), targetX, targetY);
        map.get(type1).get(node1).addConnection(connection);
        map.get(type2).get(node2).addConnection(connection.getInverse());
        return connection;
    }
    
    public int getNumberOfVertices(){
        return vertices.size();
    }
    
    public int getNumberOfEdges(){
        return edges.size();
    }
    
    public int getNumberOfFaces(){
        return faces.size();
    }

    public List<TriangulationNode> getVertices() {
        return vertices;
    }

    public List<TriangulationNode> getEdges() {
        return edges;
    }

    public List<TriangulationNode> getFaces() {
        return faces;
    }

}
