/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph;

import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Vertex;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class Face {
    
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private int index = -1;

    public void add(Vertex v){
        vertices.add(v);
    }
    
    public Vertex getVertexAt(int i){
        return vertices.get(i);
    }
    
    
    //TODO: use getEdges for getShape and cache the list of edges
    public Shape getShape(FundamentalDomain d){
        GeneralPath edge = new GeneralPath();
        int x = 0;
        int y = 0;
        edge.moveTo((float)vertices.get(0).getX(x, y, d), (float)vertices.get(0).getY(x, y, d));
        Edge lastEdge = null;
        for (int i = 1; i < vertices.size(); i++) {
            List<? extends Edge> edges = vertices.get(i-1).getEdges();
            int j = 0;
            while(j<edges.size() && !(edges.get(j).getEnd().equals(vertices.get(i)) && !edges.get(j).getInverse().equals(lastEdge)))
                j++;
            if(j<edges.size()){
                x += edges.get(j).getTargetX();
                y += edges.get(j).getTargetY();
                edge.lineTo((float)vertices.get(i).getX(x, y, d), (float)vertices.get(i).getY(x, y, d));
                lastEdge = edges.get(j);
            } else {
                throw new RuntimeException("incorrect face");
            }
        }
        edge.closePath();
        List<? extends Edge> edges = vertices.get(vertices.size()-1).getEdges();
        int j = 0;
        while(j<edges.size() && !edges.get(j).getEnd().equals(vertices.get(0)) && edges.get(j).getTargetX()!=x && edges.get(j).getTargetY()!=y)
            j++;
        if(j<edges.size())
            return edge;
        else
            throw new RuntimeException("incorrect face");
    }
    
    public List<Edge> getEdges(){
        List<Edge> faceEdges = new ArrayList<Edge>();
        int x = 0;
        int y = 0;
        Edge lastEdge = null;
        for (int i = 1; i < vertices.size(); i++) {
            List<? extends Edge> edges = vertices.get(i-1).getEdges();
            int j = 0;
            while(j<edges.size() && !(edges.get(j).getEnd().equals(vertices.get(i)) && !edges.get(j).getInverse().equals(lastEdge)))
                j++;
            if(j<edges.size()){
                x += edges.get(j).getTargetX();
                y += edges.get(j).getTargetY();
                faceEdges.add(edges.get(j));
                lastEdge = edges.get(j);
            } else {
                throw new RuntimeException("incorrect face");
            }
        }
        List<? extends Edge> edges = vertices.get(vertices.size()-1).getEdges();
        int j = 0;
        while(j<edges.size() && !edges.get(j).getEnd().equals(vertices.get(0)) && edges.get(j).getTargetX()!=x && edges.get(j).getTargetY()!=y)
            j++;
        if(j<edges.size()){
            faceEdges.add(edges.get(j));
            return faceEdges;
        } else
            throw new RuntimeException("incorrect face");
    }
    
    public int getSize(){
        return vertices.size();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
