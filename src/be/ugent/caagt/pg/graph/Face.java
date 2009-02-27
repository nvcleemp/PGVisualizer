/* Face.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
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

package be.ugent.caagt.pg.graph;

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
    private List<Edge> faceEdges;

    public void add(Vertex v){
        vertices.add(v);
        faceEdges = null;
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
        if(faceEdges!=null)
            return faceEdges;
        faceEdges = new ArrayList<Edge>();
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
