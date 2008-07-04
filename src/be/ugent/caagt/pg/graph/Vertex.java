/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
