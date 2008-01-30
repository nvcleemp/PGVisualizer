/*
 * Edge.java
 *
 * Created on January 10, 2008, 11:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph;

/**
 *
 * @author nvcleemp
 */
public class Edge {
    
    private Vertex start;
    private Vertex end;
    
    //the location of end. Start is always at (0,0).
    private int targetX;
    private int targetY;
    
    private Edge inverse;
    
    /** Creates a new instance of Edge */
    public Edge(Vertex start, Vertex end, int targetX, int targetY) {
        this.start = start;
        this.end = end;
        this.targetX = targetX;
        this.targetY = targetY;
    }
    
    public int getTargetX(){
        return targetX;
    }

    public int getTargetY(){
        return targetY;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    public Edge getInverse() {
        return inverse;
    }

    void setInverse(Edge inverse) {
        this.inverse = inverse;
    }
    
    public void translateTartget(int dx, int dy){
        targetX += dx;
        targetY += dy;
    }
    
    public static Edge getEdge(Vertex start, Vertex end, int targetX, int targetY){
        Edge edge = new Edge(start, end, targetX, targetY);
        Edge inverse = new Edge(end, start, -targetX, -targetY);
        edge.setInverse(inverse);
        inverse.setInverse(edge);
        return edge;
    }
}
