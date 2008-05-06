/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Face;
import java.awt.Color;

/**
 *
 * @author nvcleemp
 */
public interface FaceHighlighter {
    public Color getColorFor(Face f);
    public void setColor(Face f, Color c);
    public String export();
}
