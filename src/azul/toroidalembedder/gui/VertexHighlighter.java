/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.general.Vertex;
import java.awt.Color;

/**
 *
 * @author nvcleemp
 */
public interface VertexHighlighter {
    public Color getColorFor(Vertex v);
    public void setColor(Vertex v, Color c);
}
