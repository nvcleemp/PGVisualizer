/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.embedder;

import be.ugent.twi.pg.graph.Edge;
import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.graph.Vertex;
import be.ugent.twi.pg.visualizer.gui.GraphListModel;
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
