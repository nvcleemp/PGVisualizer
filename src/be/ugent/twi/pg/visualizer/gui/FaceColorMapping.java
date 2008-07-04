/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.visualizer.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class FaceColorMapping {
    private static Map<Integer, Color> faceColorMap = new HashMap<Integer, Color>();
    
    static {
        faceColorMap.put(4, Color.WHITE);
        faceColorMap.put(5, Color.GRAY);
        faceColorMap.put(6, Color.YELLOW);
        faceColorMap.put(7, new Color(0.6f, 0.7f, 1.0f));
        faceColorMap.put(8, new Color(0f, 0.4f, 0f));
        faceColorMap.put(9, new Color(0.4f, 0f, 0f));
        faceColorMap.put(10, Color.PINK);
    }
    
    public static Color getColorFor(int i){
        return faceColorMap.get(i);
    }
}
