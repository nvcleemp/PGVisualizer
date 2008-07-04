/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.graph;

import java.util.List;

/**
 *
 * @author nvcleemp
 */
public interface Graph {

    public double getEdgeLength(Edge e);

    public Vertex getVertex(int index);

    public List<? extends Vertex> getVertices();

    public void translate(double dx, double dy);

    FundamentalDomain getFundamentalDomain();

}
