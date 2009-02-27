/* Polygon2D.java
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

package be.ugent.caagt.pg.visualizer;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 *
 * @author nvcleemp
 */
public class Polygon2D {
    
    private double[] xpoints;
    private double[] ypoints;
    private Line2D[] edges;

    public Polygon2D(double[] xpoints, double[] ypoints) {
        if(xpoints.length!=ypoints.length)
            throw new RuntimeException();
        this.xpoints = xpoints;
        this.ypoints = ypoints;
    }
    
    public void draw(Graphics2D g){
        if(edges==null)
            makeEdges();
        for (Line2D line : edges)
            g.draw(line);
    }
    
    private void makeEdges(){
        int n = xpoints.length;
        edges = new Line2D[n];
        for (int i = 0; i < n; i++) {
            edges[i]=new Line2D.Double(xpoints[i], ypoints[i], xpoints[(i+1)%n], ypoints[(i+1)%n]);
        }
    }

}
