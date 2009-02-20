/* TorusViewTest.java
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

package be.ugent.caagt.pg.visualizer.gui;

import be.ugent.caagt.pg.embedder.DomainAngleEmbedder;
import be.ugent.caagt.pg.embedder.Embedder;
import be.ugent.caagt.pg.embedder.FastDomainAngleEmbedder;
import be.ugent.caagt.pg.embedder.SpringEmbedder;
import be.ugent.caagt.pg.embedder.SpringEmbedderEqualEdges;
import be.ugent.caagt.pg.embedder.energy.AngleEnergyCalculator;
import be.ugent.caagt.pg.embedder.energy.MeanEdgeLengthEnergyCalculator;
import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.DefaultVertex;
import be.ugent.caagt.pg.io.FileFormatException;
import be.ugent.caagt.pg.io.IOManager;
import be.ugent.caagt.pg.graph.FundamentalDomain;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author nvcleemp
 */
public class TorusViewTest {
    
    public static void main(String[] args){
        final JFrame frame = new JFrame();
        final DefaultGraph graph = new DefaultGraph();
        final DefaultVertex v1 = new DefaultVertex(0, 0);
        graph.addVertex(v1);
        final DefaultVertex v2 = new DefaultVertex(0.5, 0);
        graph.addVertex(v2);
        final DefaultVertex v3 = new DefaultVertex(0, 0.5);
        graph.addVertex(v3);
        graph.addEdge(v1, v2, 0, 0);
        graph.addEdge(v1, v3, 0, 0);
        graph.addEdge(v2, v3, 0, 0);
        graph.addEdge(v3, v1, 1, -1);
        graph.addEdge(v3, v2, 0, 1);
        //graph.addEdge(v3, v2, 1, 0);
        DefaultGraph graph1 = null;
        try {
            graph1 = IOManager.readPG("3|2 2|0 0;0.5 0;0 0.5|0 1 0 0;0 2 0 0;1 2 0 0;2 0 1 -1;2 1 0 1");
            graph1 = IOManager.readPG("3|2 2|0 0;0.5 0;0 0.5|0 1 0 0;0 2 0 0;1 2 0 0;2 0 1 -1;2 1 0 1;2 1 1 0");
            graph1 = IOManager.readPG("1|2 2|0 0|0 0 1 0;0 0 0 1");
        } catch (FileFormatException fileFormatException) {
            fileFormatException.printStackTrace();
        }
        final Embedder embedder1 = new SpringEmbedder(graph);
        final Embedder embedder2 = new SpringEmbedderEqualEdges(graph);
        final Embedder embedder3 = new DomainAngleEmbedder(graph, 40, 5, Math.PI/2, new MeanEdgeLengthEnergyCalculator());
        final Embedder embedder4 = new FastDomainAngleEmbedder(graph, 0.1, 1, new MeanEdgeLengthEnergyCalculator());
        final Embedder embedder5 = new FastDomainAngleEmbedder(graph, 0.1, 1, new AngleEnergyCalculator());
        embedder3.initialize();
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if(e.isShiftDown()){
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        graph.translate(0.1, 0);
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        graph.translate(- 0.1, 0);
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        graph.translate(0, -0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        graph.translate(0, 0.1);
                } else if(e.isControlDown()){
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        v1.translate(0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        v1.translate(- 0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        v1.translate(0, -0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        v1.translate(0, 0.1, graph.getFundamentalDomain());
                } else if(e.isAltDown()){
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        v3.translate(0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        v3.translate(- 0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        v3.translate(0, -0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        v3.translate(0, 0.1, graph.getFundamentalDomain());
                } else {
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                        v2.translate(0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_LEFT)
                        v2.translate(- 0.1, 0, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_UP)
                        v2.translate(0, -0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_DOWN)
                        v2.translate(0, 0.1, graph.getFundamentalDomain());
                    else if(e.getKeyCode() == KeyEvent.VK_SPACE)
                        embedder1.embed();
                    else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                        embedder2.embed();
                    else if(e.getKeyCode() == KeyEvent.VK_D)
                        embedder3.embed();
                    else if(e.getKeyCode() == KeyEvent.VK_F)
                        embedder4.embed();
                    else if(e.getKeyCode() == KeyEvent.VK_E)
                        embedder5.embed();
                    else if(e.getKeyCode() == KeyEvent.VK_A)
                        graph.getFundamentalDomain().addToAngle(0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_V)
                        graph.getFundamentalDomain().addToAngle(-0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD6)
                        graph.getFundamentalDomain().addToHorizontalSide(0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD4)
                        graph.getFundamentalDomain().addToHorizontalSide(-0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD8)
                        graph.getFundamentalDomain().addToVerticalSide(0.1);
                    else if(e.getKeyCode() == KeyEvent.VK_NUMPAD2)
                        graph.getFundamentalDomain().addToVerticalSide(-0.1);
                }
            }
            public void keyReleased(KeyEvent e) {
            }
            public void keyTyped(KeyEvent e) {
            }
        });
        frame.setContentPane(new TorusView(graph, -2, -2, 2, 2));
        frame.pack();
        final JFrame frame2 = new JFrame("Raw view");
        frame2.setContentPane(new TorusView(new FundamentalDomain(), graph, -2, -2, 2, 2));
        frame2.pack();
        frame2.setLocation(frame.getX() + frame.getWidth(), frame.getY());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame2.dispose();
            }
        });
        frame2.setVisible(true);
        frame.setVisible(true);
    }    
}
