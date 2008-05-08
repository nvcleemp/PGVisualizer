/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.embedder;

import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;
import azul.toroidalembedder.gui.GraphModel;
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

    public AbstractTemperedEmbedder(GraphModel graphModel) {
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
   /* 
    protected void moveTo(Vertex v, double x, double y){
        Geometry2D.Geometry2DPoint startPoint = new Geometry2D.Geometry2DPoint(v.getX(graph.getFundamentalDomain()), v.getY(graph.getFundamentalDomain()));
        Geometry2D.Geometry2DPoint endPoint = new Geometry2D.Geometry2DPoint(x, y);
        Geometry2D.Geometry2DLineSegment movement = new Geometry2D.Geometry2DLineSegment(startPoint, endPoint);
        double movementLengthS = movement.getLengthSquared();
        
        double minLength = 0;
        Geometry2D.Geometry2DPoint minIntersection = null;
        for (Vertex vertex : graph.getVertices()) {
            if(vertex!=v){
                for (Edge edge : vertex.getEdges()) {
                    if(edge.getEnd()!=v || edge.getTargetX()!=0 || edge.getTargetY()!=0){
                        Geometry2D.Geometry2DPoint edgeStartPoint = new Geometry2D.Geometry2DPoint(edge.getStart().getX(graph.getFundamentalDomain()), edge.getStart().getY(graph.getFundamentalDomain()));
                        Geometry2D.Geometry2DPoint edgeEndPoint = new Geometry2D.Geometry2DPoint(edge.getEnd().getX(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()), edge.getEnd().getY(edge.getTargetX(), edge.getTargetY(), graph.getFundamentalDomain()));
                        try {
                            Geometry2D.Geometry2DPoint intersection = Geometry2D.lineIntersection(edgeStartPoint, edgeEndPoint, startPoint, endPoint);
                            if(intersection==null)
                                throw new Geometry2D.GeometricException("No intersection");
                            //check to see if intersection lies on edge and on movement line
                            double edgeLengthS = Geometry2D.distanceSquared(edgeStartPoint, edgeEndPoint);
                            if(Geometry2D.distanceSquared(intersection, edgeStartPoint) <= edgeLengthS &&
                               Geometry2D.distanceSquared(intersection, edgeEndPoint) <= edgeLengthS &&
                               Geometry2D.distanceSquared(intersection, startPoint) <= movementLengthS &&
                               Geometry2D.distanceSquared(intersection, endPoint) <= movementLengthS){
                               double distance = Geometry2D.distanceSquared(intersection, startPoint);
                               if(minIntersection==null || distance < minLength){
                                   minLength = distance;
                                   minIntersection = intersection;
                               }
                            }
                        } catch (Geometry2D.GeometricException geometricException) {
                            System.out.println(geometricException);
                            // do nothing
                        }
                    }
                }
            }
        }
        
        if(minIntersection == null){
            double dx = x - v.getX(graph.getFundamentalDomain());
            double dy = y - v.getY(graph.getFundamentalDomain());
            v.translate(dx, dy, graph.getFundamentalDomain());
        } else {
            //limit movement to minimal intersection
            double dx = (minIntersection.x - v.getX(graph.getFundamentalDomain()))*0.9;
            double dy = (minIntersection.y - v.getY(graph.getFundamentalDomain()))*0.9;
            v.translate(dx, dy, graph.getFundamentalDomain());
        }
            
        for (Vertex vertex : graph.getVertices()) {
            Geometry2D.Geometry2DPoint vertexPoint = new Geometry2D.Geometry2DPoint(vertex.getX(graph.getFundamentalDomain()), vertex.getY(graph.getFundamentalDomain()));
            if(Geometry2D.distancePointLineSegmentSquared(vertexPoint, movement) < 0.1*movementLengthS){
                
            }
        }

    }*/
}
