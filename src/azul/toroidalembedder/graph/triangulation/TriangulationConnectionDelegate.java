/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph.triangulation;

import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Vertex;

/**
 *
 * @author nvcleemp
 */
public class TriangulationConnectionDelegate implements Edge{
    
    private TriangulationConnection delegate;
    private TriangulationNodeDelegate start;
    private TriangulationNodeDelegate end;
    private TriangulationConnectionDelegate inverse;

    public TriangulationConnectionDelegate(TriangulationConnection delegate, TriangulationNodeDelegate start, TriangulationNodeDelegate end) {
        this.delegate = delegate;
        this.start = start;
        this.end = end;
    }

    public Vertex getEnd() {
        return end;
    }

    public Edge getInverse() {
        return inverse;
    }

    public Vertex getStart() {
        return start;
    }

    public int getTargetX() {
        return delegate.getTargetX();
    }

    public int getTargetY() {
        return delegate.getTargetY();
    }

    public void translateTartget(int dx, int dy) {
        delegate.translateTartget(dx, dy);
    }

    public double getLength(FundamentalDomain fd) {
        double dx = start.getX(fd) - end.getX(getTargetX(), getTargetY(), fd);
        double dy = start.getY(fd) - end.getY(getTargetX(), getTargetY(), fd);
        return Math.hypot(dx, dy);
    }
    
    public void setInverse(TriangulationConnectionDelegate inverse) {
        this.inverse = inverse;
    }
}
