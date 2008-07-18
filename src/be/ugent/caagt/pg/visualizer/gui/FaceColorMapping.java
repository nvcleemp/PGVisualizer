/* FaceColorMapping.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008 Universiteit Gent
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
