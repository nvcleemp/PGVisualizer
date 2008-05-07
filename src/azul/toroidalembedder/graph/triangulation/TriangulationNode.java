/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.graph.triangulation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class TriangulationNode {
    public enum Type{
        VERTEX, EDGE, FACE;
    }
    
    private Type type;
    private double x;
    private double y;
    private int index = -1;
    private List<TriangulationConnection> connections = new ArrayList<TriangulationConnection>();

    public TriangulationNode(Type type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public void addConnection(TriangulationConnection connection){
        connections.add(connection);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public void setCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public List<TriangulationConnection> getConnections() {
        return connections;
    }
}
