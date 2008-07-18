/* SpringEmbedderExpandSmallFaces.java
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

package be.ugent.caagt.pg.embedder;


import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import java.awt.Shape;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class SpringEmbedderExpandSmallFaces extends AbstractEmbedder {
    
    private double edge_length = 0;
    private double non_edge_length;
    private double force;
    private double friction;
    private double[][] changes;
    private List<? extends Vertex> vertices;
    
    
    /** Creates a new instance of DefaultEmbedder */
    public SpringEmbedderExpandSmallFaces(Graph graph) {
        this(graph, 3, 0.01, 0.85);
    }

    public SpringEmbedderExpandSmallFaces(GraphListModel graphModel) {
        this(graphModel, 3, 0.01, 0.85);
    }

    public SpringEmbedderExpandSmallFaces(Graph graph, double non_edge_length, double force, double friction) {
        super(graph);
        this.non_edge_length = non_edge_length;
        this.force = force;
        this.friction = friction;
        vertices = graph.getVertices();
    }

    public SpringEmbedderExpandSmallFaces(GraphListModel graphModel, double non_edge_length, double force, double friction) {
        super(graphModel);
        this.non_edge_length = non_edge_length;
        this.force = force;
        this.friction = friction;
        vertices = graph.getVertices();
    }

    public void embed() {
        if(changes==null)
            initialize();
        
        for (int i = 0; i<vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            for (Edge edge : vertex.getEdges()) {
                double dx = (vertex.getX(graph.getFundamentalDomain()) - edge.getEnd().getX(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                double dy = (vertex.getY(graph.getFundamentalDomain()) - edge.getEnd().getY(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                double change = Math.hypot(dx, dy);
                if(change == 0){
                    dx = 1.0;
                    dy = 0.0;
                } else {
                    dx /= change;
                    dy /= change;
                }
                //change = (change - edge_length)/edge_length;
                dx *= change * force;
                dy *= change * force;
                changes[i][0] -= dx;
                changes[i][1] -= dy;
            }            
        }
        
        for (Face f : ((DefaultGraph)graph).getFaces()) {
            if(f.getSize() < 8){
                for (int i = 0; i < f.getSize()-1; i++) {
                    for (int j = 1; j < f.getSize(); j++) {
                        Vertex v1 = f.getVertexAt(i);
                        Vertex v2 = f.getVertexAt(j);
                        boolean connected = false;
                        for (Edge edge : v1.getEdges()) {
                            connected = connected || edge.getEnd().equals(v2);
                        }
                        if(!connected){
                            
                        }
                    }


                }

                
            }
        }
                
        for (int i=0; i < vertices.size(); i++)
            for (int j=0; j < 2; j++)
                changes[i][j] *= friction;

        for (int i=0; i < vertices.size(); i++)
            vertices.get(i).translate(changes[i][0], changes[i][1], graph.getFundamentalDomain());
    }

    public void initialize() {
        changes = new double[vertices.size()][2];
    }

    @Override
    protected void resetEmbedder() {
        changes = null;
        vertices = graph.getVertices();
    }
    
}