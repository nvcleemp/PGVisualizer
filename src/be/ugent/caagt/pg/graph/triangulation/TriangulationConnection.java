/* TriangulationConnection.java
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

    public void translateTartget(int dx, int dy) {
        targetX += dx;
        targetY += dy;
    }
    
    public static TriangulationConnection getConnection(TriangulationNode start, TriangulationNode end, int targetX, int targetY){
        TriangulationConnection connection = new TriangulationConnection(start, end, targetX, targetY);
        TriangulationConnection inverse = new TriangulationConnection(end, start, -targetX, -targetY);
        connection.setInverse(inverse);
        inverse.setInverse(connection);
        return connection;
    }
}
