/* AngleEnergyCalculator.java
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

package be.ugent.caagt.pg.embedder.energy;

import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.graph.Vertex;
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
