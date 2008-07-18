/* TriangulationConnectionDelegate.java
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

import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Vertex;

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
