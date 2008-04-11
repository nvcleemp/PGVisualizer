/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.toroidalembedder.gui;

import azul.toroidalembedder.graph.Face;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class DefaultFaceHightlighter implements FaceHighlighter {
    
    Map<Face, Color> map = new HashMap<Face, Color>();

    public Color getColorFor(Face f) {
        return map.get(f);
    }

    public void setColor(Face f, Color c) {
        if(c==null)
            map.remove(f);
        else
            map.put(f, c);
    }
    
}
