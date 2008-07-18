/* TriangulationNode.java
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

package be.ugent.caagt.pg.graph.triangulation;

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
