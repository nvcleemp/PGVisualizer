/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph.triangulation;

/**
 *
 * @author nvcleemp
 */
public class TriangulationConnection {
    
    private TriangulationNode start;
    private TriangulationNode end;
    
    //the location of end. Start is always at (0,0).
    private int targetX;
    private int targetY;
    
    private TriangulationConnection inverse;

    public TriangulationConnection(TriangulationNode start, TriangulationNode end, int targetX, int targetY) {
        this.start = start;
        this.end = end;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public TriangulationNode getEnd() {
        return end;
    }

    public TriangulationNode getStart() {
        return start;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public TriangulationConnection getInverse() {
        return inverse;
    }

    public void setInverse(TriangulationConnection inverse) {
        this.inverse = inverse;
    }

    public static TriangulationConnection getConnection(TriangulationNode start, TriangulationNode end, int targetX, int targetY){
        TriangulationConnection connection = new TriangulationConnection(start, end, targetX, targetY);
        TriangulationConnection inverse = new TriangulationConnection(end, start, -targetX, -targetY);
        connection.setInverse(inverse);
        inverse.setInverse(connection);
        return connection;
    }
}
