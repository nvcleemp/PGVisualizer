/* DefaultEdge.java
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
