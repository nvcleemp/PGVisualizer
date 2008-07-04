/*
 * AngleEnergyCalculator.java
 */

package be.ugent.twi.pg.embedder.energy;

import be.ugent.twi.pg.graph.FundamentalDomain;
import be.ugent.twi.pg.graph.Edge;
import be.ugent.twi.pg.graph.Graph;
import be.ugent.twi.pg.graph.Vertex;
import java.util.Arrays;
import java.util.List;

/**
 * Calculates an energy based on the angles between the edges.
 */
public class AngleEnergyCalculator implements EnergyCalculator{

    public double calculateEnergy(Graph graph, FundamentalDomain domain) {
        double energy=0;
        for (Vertex vertex : graph.getVertices()) {
            double[] angles = new double[vertex.getDegree()];
            List<? extends Edge> edges = vertex.getEdges();
            for (int i = 0; i < angles.length; i++) {
                double x = edges.get(i).getEnd().getX(edges.get(i).getTargetX(), edges.get(i).getTargetY(), domain) - vertex.getX(domain);
                double y = edges.get(i).getEnd().getY(edges.get(i).getTargetX(), edges.get(i).getTargetY(), domain) - vertex.getY(domain);
                angles[i] = Math.atan2(y, x);
            }
            Arrays.sort(angles);
            
            double ideal = 2*Math.PI/vertex.getDegree();
            for (int i = 0; i < angles.length - 1; i++) {
                double angle = angles[i+1] - angles[i];
                energy += (ideal-angle)*(ideal-angle);
            }
            double angle = angles[0] - angles[angles.length-1] + 2*Math.PI;
            energy += (ideal-angle)*(ideal-angle);
        }
        
        return energy;
    }

}
