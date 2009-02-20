/* AbstractTemperedEmbedder.java
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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author nvcleemp
 */
public abstract class AbstractTemperedEmbedder extends AbstractEmbedder{

    public AbstractTemperedEmbedder(Graph graph) {
        super(graph);
    }

    public AbstractTemperedEmbedder(GraphListModel graphModel) {
        super(graphModel);
    }
    
    protected void translate(Vertex v, double dx, double dy){
        if(dx==0 && dy==0)
            return;
        int i = 0;
        boolean done = false;
        while(i<20 && !done){
            Point2D newposition = new Point2D.Double(v.getX(graph.getFundamentalDomain()) + dx, v.getY(graph.getFundamentalDomain()) + dy);
            done = true;
            //check all edges -> when intersection: done = false
            for (Edge edge : v.getEdges()) {
                Point2D otherVertex = new Point2D.Double(
                        edge.getEnd().getX(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()),
                        edge.getEnd().getY(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                Line2D newEdge = new Line2D.Double(newposition, otherVertex);
                for (Vertex vertex : graph.getVertices()) {
                    if(vertex!=v && vertex != edge.getEnd()){
                        for (Edge otherEdge : vertex.getEdges()) {
                            if(otherEdge.getEnd()!=v && otherEdge.getEnd()!=edge.getEnd()){ //|| otherEdge.getTargetX()!=0 || otherEdge.getTargetY()!=0){
                                done = done && !newEdge.intersectsLine(vertex.getX(graph.getFundamentalDomain()), vertex.getY(graph.getFundamentalDomain()),
                                        otherEdge.getEnd().getX(otherEdge.getTargetX(), otherEdge.getTargetY(), graph.getFundamentalDomain()),
                                        otherEdge.getEnd().getY(otherEdge.getTargetX(), otherEdge.getTargetY(), graph.getFundamentalDomain()));
                            }
                        }
                    }
                }
            }
            if(!done){
                dx /= 2;
                dy /= 2;
            }
            i++;
        }
        if(done){
            v.translate(dx, dy, graph.getFundamentalDomain());
        }
    }
}
