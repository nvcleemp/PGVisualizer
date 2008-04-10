/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph;

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
    
    public Shape getShape(FundamentalDomain d){
        GeneralPath edge = new GeneralPath();
        int x = 0;
        int y = 0;
        edge.moveTo((float)vertices.get(0).getX(x, y, d), (float)vertices.get(0).getY(x, y, d));
        for (int i = 1; i < vertices.size(); i++) {
            List<Edge> edges = vertices.get(i-1).getEdges();
            int j = 0;
            while(j<edges.size() && !edges.get(j).getEnd().equals(vertices.get(i)))
                j++;
            if(j<edges.size()){
                x += edges.get(j).getTargetX();
                y += edges.get(j).getTargetY();
                edge.lineTo((float)vertices.get(i).getX(x, y, d), (float)vertices.get(i).getY(x, y, d));
            } else {
                throw new RuntimeException("incorrect face");
            }
        }
        edge.closePath();
        return edge;
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
