/* DefaultFaceHightlighter.java
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

import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.DefaultGraph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

    public String export() {
        StringBuffer buf = new StringBuffer();
        for (Face face : map.keySet()) {
            buf.append(face.getIndex());
            buf.append(" ");
            buf.append(map.get(face).getRGB());
            buf.append(" ");
        }
        return buf.toString();
    }
    
    public void importHighlighting(DefaultGraph g, String s){
        Scanner scanner = new Scanner(s);
        List<Face> faces = g.getFaces();
        while(scanner.hasNextInt()){
            map.put(faces.get(scanner.nextInt()), Color.decode(scanner.next()));
        }
    }
    
    public List<Face> getFaces(){
        return new ArrayList<Face>(map.keySet());
    }
    
    public Map<Face,Color> getMap(){
        return new HashMap<Face, Color>(map);
    }
}
