/*
 * MeanEdgeLengthEnergyCalculator.java
 */

package azul.toroidalembedder.energy;

import azul.toroidalembedder.graph.FundamentalDomain;
import azul.toroidalembedder.graph.general.Edge;
import azul.toroidalembedder.graph.general.Graph;
import azul.toroidalembedder.graph.general.Vertex;

/**
 * Calculates an energy based on the difference of length between the edges.
 */
public class MeanEdgeLengthEnergyCalculator implements EnergyCalculator{

    public double calculateEnergy(Graph graph, FundamentalDomain domain) {
        double mean = 0;
        int count = 0;
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : vertex.getEdges()) {
                mean += graph.getEdgeLength(edge, domain);
                count++;
            }
        }
        mean /= count;
        
        double energy=0;
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : vertex.getEdges()) {
                double d = graph.getEdgeLength(edge, domain);
                energy += (mean - d) * (mean - d);
            }
        }
        
        return energy;
    }

}
