/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui;

import be.ugent.twi.pg.graph.Face;
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
