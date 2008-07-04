/*
 * MeanEdgeLengthEnergyCalculator.java
 */

package be.ugent.caagt.pg.embedder.energy;

import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;

/**
 * Calculates an energy based on the difference of length between the edges.
 */
public class MeanEdgeLengthEnergyCalculator implements EnergyCalculator{

    public double calculateEnergy(Graph graph, FundamentalDomain domain) {
        double mean = 0;
        int count = 0;
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : vertex.getEdges()) {
                mean += edge.getLength(domain);
                count++;
            }
        }
        mean /= count;
        
        double energy=0;
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : vertex.getEdges()) {
                double d = edge.getLength(domain);
                energy += (mean - d) * (mean - d);
            }
        }
        
        return energy;
    }

}
