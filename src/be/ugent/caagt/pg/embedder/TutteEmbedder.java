/* TutteEmbedder.java
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

package be.ugent.caagt.pg.embedder;

import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class TutteEmbedder extends AbstractEmbedder {
    
    List<Vertex> initialVertices = null;
    List<Vertex> embeddedVertices = null;
    
    /** Creates a new instance of DefaultEmbedder */
    public TutteEmbedder(Graph graph) {
        super(graph);
    }

    public TutteEmbedder(GraphListModel graphModel) {
        super(graphModel);
    }


    @Override
    protected void resetEmbedder() {
        initialVertices = new ArrayList<Vertex>();
        for (Vertex vertex : graph.getVertices()) {
            boolean external = false;
            List<? extends Edge> edges = vertex.getEdges();
            int i = 0;
            while(!external && i<edges.size()){
                external = edges.get(i).getTargetX()!=0 || edges.get(i).getTargetY()!=0;
                i++;
            }
            if(external)
                initialVertices.add(vertex);
        }
        embeddedVertices = null;
    }

    public void initialize() {
        if(initialVertices == null)
            return;
        embeddedVertices = new ArrayList<Vertex>(initialVertices);
        List<Vertex> vertices = new ArrayList<Vertex>(graph.getVertices());
        int previous = vertices.size();
        vertices.removeAll(embeddedVertices);
        while(vertices.size()!=previous){
            previous = vertices.size();
            for (Vertex vertex : vertices) {
                List<Vertex> neighbours = new ArrayList<Vertex>();
                for (Edge edge : vertex.getEdges()) {
                    if(edge.getTargetX()==0 && edge.getTargetY()==0 && !neighbours.contains(edge.getEnd()))
                        neighbours.add(edge.getEnd());
                }
                if(neighbours.size()==2 || neighbours.size()==3){
                    double x = 0;
                    double y = 0;
                    for (Vertex v : neighbours) {
                        x += v.getRawX();
                        y += v.getRawY();
                    }
                    vertex.setRawCoordinates(x/3, y/3);
                    embeddedVertices.add(vertex);
                }
            }
            vertices.removeAll(embeddedVertices);
        }
    }

    public void embed() {
        if(initialVertices == null)
            return;
        else if(embeddedVertices == null)
            initialize();
    }
}
