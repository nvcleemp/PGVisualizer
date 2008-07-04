/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui;

import be.ugent.twi.pg.graph.Vertex;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class DefaultVertexHightlighter implements VertexHighlighter {
    
    Map<Vertex, Color> map = new HashMap<Vertex, Color>();

    public Color getColorFor(Vertex v) {
        return map.get(v);
    }

    public void setColor(Vertex v, Color c) {
        map.put(v, c);
    }
    
}
