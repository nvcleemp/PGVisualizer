/* MeanEdgeLengthEnergyCalculator.java
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
