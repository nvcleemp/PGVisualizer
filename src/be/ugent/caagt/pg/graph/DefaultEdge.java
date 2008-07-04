/*
 * DefaultEdge.java
 *
 * Created on January 10, 2008, 11:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.graph;

/**
 *
 * @author nvcleemp
 */
public class DefaultEdge implements Edge {
    
    private DefaultVertex start;
    private DefaultVertex end;
    
    //the location of end. Start is always at (0,0).
    private int targetX;
    private int targetY;
    
    private DefaultEdge inverse;
    
    /** Creates a new instance of DefaultEdge */
    public DefaultEdge(DefaultVertex start, DefaultVertex end, int targetX, int targetY) {
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

    public DefaultVertex getStart() {
        return start;
    }

    public DefaultVertex getEnd() {
        return end;
    }

    public DefaultEdge getInverse() {
        return inverse;
    }

    void setInverse(DefaultEdge inverse) {
        this.inverse = inverse;
    }
    
    public void translateTartget(int dx, int dy){
        targetX += dx;
        targetY += dy;
    }
    
    public static DefaultEdge getEdge(DefaultVertex start, DefaultVertex end, int targetX, int targetY){
        DefaultEdge edge = new DefaultEdge(start, end, targetX, targetY);
        DefaultEdge inverse = new DefaultEdge(end, start, -targetX, -targetY);
        edge.setInverse(inverse);
        inverse.setInverse(edge);
        return edge;
    }

    public double getLength(FundamentalDomain fd) {
        double dx = start.getX(fd) - end.getX(targetX, targetY, fd);
        double dy = start.getY(fd) - end.getY(targetX, targetY, fd);
        return Math.hypot(dx, dy);
    }
}
