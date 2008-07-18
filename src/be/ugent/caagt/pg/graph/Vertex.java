/* Vertex.java
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

package be.ugent.caagt.pg.graph;

import java.util.List;

/**
 *
 * @author nvcleemp
 */
public interface Vertex {

    public void addVertexListener(VertexListener listener);

    /**
     * returns the degree of this vertex.
     *
     * @return The degree of this vertex.
     */
    public int getDegree();

    /**
     * returns the list of edges that are incident with this vertex. All the edges will
     * have this vertex as start.
     *
     * @return The list of edges that are incident with this vertex.
     */
    public List<? extends Edge> getEdges();

    public int getIndex();

    /**
     * returns the x coordinate of the vertex in the unit square
     *
     * @return the x coordinate of the vertex in the unit square
     */
    public double getRawX();

    /**
     * returns the y coordinate of the vertex in the unit square
     *
     * @return the y coordinate of the vertex in the unit square
     */
    public double getRawY();

    /**
     * returns the x coordinate of the vertex in the given fundamental domain at (0,0)
     *
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the x coordinate of the vertex in the given fundamental domain at (0,0)
     */
    public double getX(FundamentalDomain fundamentalDomain);

    /**
     * returns the x coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     *
     * @param domainX The x coordinate of the required fundamental domain
     * @param domainY The y coordinate of the required fundamental domain
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the x coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     */
    public double getX(int domainX, int domainY, FundamentalDomain fundamentalDomain);

    /**
     * returns the y coordinate of the vertex in the given fundamental domain at (0,0)
     *
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the y coordinate of the vertex in the given fundamental domain at (0,0)
     */
    public double getY(FundamentalDomain fundamentalDomain);

    /**
     * returns the y coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     *
     * @param domainX The x coordinate of the required fundamental domain
     * @param domainY The y coordinate of the required fundamental domain
     * @param fundamentalDomain The fundamental domain for which the coordinate needs to be calculated
     * @return the y coordinate of the vertex in the given fundamental domain at (domainX,domainY)
     */
    public double getY(int domainX, int domainY, FundamentalDomain fundamentalDomain);

    public void setRawCoordinates(double x, double y);

    /**
     * Moves the vertex along the vector (<code>dx</code>, <code>dy</code>) in the coordinate system
     * described by the <code>fundamentalDomain</code>.
     *
     * @param dx The amount to move along the X-axis.
     * @param dy The amount to move along the Y-axis.
     * @param fundamentalDomain The system in which the amounts are given.
     */
    public void translate(double dx, double dy, FundamentalDomain fundamentalDomain);

}
