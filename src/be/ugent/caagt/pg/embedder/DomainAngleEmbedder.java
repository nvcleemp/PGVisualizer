/* DomainAngleEmbedder.java
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

package be.ugent.caagt.pg.embedder;

import be.ugent.caagt.pg.embedder.energy.EnergyCalculator;
import be.ugent.caagt.pg.graph.FundamentalDomain;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;

/**
 *
 * @author nvcleemp
 */
public class DomainAngleEmbedder extends AbstractEmbedder{
    
    private int intervals;
    private int zoom;
    private double initialSearchAngle;
    private double currentSearchAngle;
    private boolean lock = false;
    private EnergyCalculator energyCalculator;

    public DomainAngleEmbedder(Graph graph, int intervals, int zoom, double initialSearchAngle, EnergyCalculator energyCalculator) {
        super(graph);
        this.intervals = intervals;
        this.zoom = zoom;
        this.initialSearchAngle = initialSearchAngle;
        this.energyCalculator = energyCalculator;
    }

    public DomainAngleEmbedder(GraphListModel graphModel, int intervals, int zoom, double initialSearchAngle, EnergyCalculator energyCalculator) {
        super(graphModel);
        this.intervals = intervals;
        this.zoom = zoom;
        this.initialSearchAngle = initialSearchAngle;
        this.energyCalculator = energyCalculator;
    }

    public void initialize() {
        currentSearchAngle = initialSearchAngle;
    }

    public void embed() {
        if(!(graph instanceof DefaultGraph))
            return;
        if(lock)
            return;
        lock = true;
        double minAngle = graph.getFundamentalDomain().getAngle();
        double minCost = energyCalculator.calculateEnergy(graph, graph.getFundamentalDomain());
        
        double startAngle = graph.getFundamentalDomain().getAngle() - currentSearchAngle/2;
        double endAngle = graph.getFundamentalDomain().getAngle() + currentSearchAngle/2;
        double increment = currentSearchAngle/intervals;
        if(startAngle<=0) startAngle = increment;
        if(endAngle>=Math.PI) endAngle = Math.PI - increment;
        
        FundamentalDomain domain = new FundamentalDomain(startAngle, graph.getFundamentalDomain().getHorizontalSide(), graph.getFundamentalDomain().getVerticalSide());
        while(domain.getAngle()<endAngle){
            double cost = energyCalculator.calculateEnergy(graph, domain);
            if(cost < minCost){
                minAngle = domain.getAngle();
                minCost = cost;
            }
            domain.addToAngle(increment);
        }
        if(graph.getFundamentalDomain().getAngle()!=minAngle)
            ((DefaultGraph)graph).getFundamentalDomain().setAngle(minAngle);
        currentSearchAngle /= zoom;
        lock = false;
    }

    @Override
    protected void resetEmbedder() {
        currentSearchAngle = initialSearchAngle;
    }
}
