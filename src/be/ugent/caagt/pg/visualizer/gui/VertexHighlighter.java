/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui;

import be.ugent.caagt.pg.graph.Vertex;
import java.awt.Color;

/**
 *
 * @author nvcleemp
 */
public interface VertexHighlighter {
    public Color getColorFor(Vertex v);
    public void setColor(Vertex v, Color c);
}
