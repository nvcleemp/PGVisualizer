/* FastDomainEdgeEmbedder.java
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

package be.ugent.caagt.pg.embedder;

import be.ugent.caagt.pg.embedder.energy.EnergyCalculator;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;

/**
 *
 * @author nvcleemp
 */
public class FastDomainEdgeEmbedder extends AbstractEmbedder {
    
    private double epsilon;
    private double k;
    private EnergyCalculator energyCalculator;

    public FastDomainEdgeEmbedder(Graph graph, double epsilon, double k, EnergyCalculator energyCalculator) {
        super(graph);
        this.epsilon = epsilon;
        this.k = k;
        this.energyCalculator = energyCalculator;
    }

    public FastDomainEdgeEmbedder(GraphListModel graphModel, double epsilon, double k, EnergyCalculator energyCalculator) {
        super(graphModel);
        this.epsilon = epsilon;
        this.k = k;
        this.energyCalculator = energyCalculator;
    }

    public void initialize() {
        //
    }

    public void embed() {
        if(!(graph instanceof DefaultGraph))
            return;
        double energy = energyCalculator.calculateEnergy(graph, graph.getFundamentalDomain());
        double HSE = graph.getFundamentalDomain().getHorizontalSide() - epsilon > 0 ? graph.getFundamentalDomain().getHorizontalSide() - epsilon : epsilon;
        double energy2 = energyCalculator.calculateEnergy(graph, new FundamentalDomain(graph.getFundamentalDomain().getAngle(), HSE, graph.getFundamentalDomain().getVerticalSide()));
        double HS = graph.getFundamentalDomain().getHorizontalSide() + (energy2 - energy)*epsilon*k;
        if(HS <=0)
            HS = epsilon;
        int factor = 1;
        while(HS*factor<0.2)
            factor+=10;
        ((DefaultGraph)graph).getFundamentalDomain().setSides(HS*factor, graph.getFundamentalDomain().getVerticalSide()*factor);
    }

    @Override
    protected void resetEmbedder() {
        //
    }
}
