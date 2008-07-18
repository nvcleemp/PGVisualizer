/* TriangulatedGraph.java
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
