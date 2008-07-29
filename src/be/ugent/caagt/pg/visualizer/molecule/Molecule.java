/* Molecule.java
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

package be.ugent.caagt.pg.visualizer.molecule;

import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.graph.Edge;
import be.ugent.caagt.pg.graph.Graph;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nvcleemp
 */
public class Molecule extends Tiled3DStructure {
    
    protected String[] atomType;
    
    public Molecule(Graph graph){
        this(graph, null, null, Embedding.PLANAR);             
    }
    
    public Molecule(Graph graph, Embedding embedding) {
        this(graph, null, null, embedding);
    }
    
    public Molecule(Graph graph, List<Face> resultFaces, Embedding embedding) {
        this(graph, null, null, embedding);
    }
    
    public Molecule(Graph graph, List<Face> resultFaces, Map<Face, Color> colors, Embedding embedding) {
        super(graph, resultFaces, colors, embedding);
        atomType = new String[size];
        for (int i = 0; i < atomType.length; i++) {
            atomType[i] = "C";
        }
    }
    
    public String writeCML(){
        StringBuffer buffer = new StringBuffer();
        String sep;
        int i, k, n = size;
        buffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
        buffer.append("<!DOCTYPE molecule SYSTEM \"cml.dtd\" []>\n");
        buffer.append("<molecule convention=\"MathGraph\">\n");
        buffer.append("  <atomArray>\n");
        buffer.append("    <stringArray builtin=\"id\">");
        sep = "";
        for (i = 0; i < n; ++i)
        {
          buffer.append(sep + "a" + i);
          sep = " ";
        }
        buffer.append("</stringArray>\n");
        buffer.append("    <stringArray builtin=\"elementType\">");
        sep = "";
        for (i = 0; i < n; ++i)
        {
          buffer.append(sep + atomType[i]);
          sep = " ";
        }
        buffer.append("</stringArray>\n");
        for (k = 0; k < 3; ++k)
        {
          buffer.append("    <floatArray builtin=\"" + "xyz".substring(k, k+1) + 3 + "\">");
          sep = "";
          for (i = 0; i < n; ++i)
          {
            buffer.append(sep + (coordinate[i][k]*10));
            sep = " ";
          }
          buffer.append("</floatArray>\n");
        }
        buffer.append("  </atomArray>\n");
        buffer.append("  <bondArray>\n");
        StringBuffer from = new StringBuffer();
        StringBuffer to = new StringBuffer();
        sep = "";
        for (i = 0; i < n; ++i)
        {
            for (int j = 0; j < adjacencyList[i].length; j++)
            {
                //if(i < adjacencyList[i][j]){
                    from.append(sep + "a" + i);
                    to.append(sep + "a" + adjacencyList[i][j]);
                    sep = " ";
                //}
            }
        }
        buffer.append("    <stringArray builtin=\"atomRef\">" + from + "</stringArray>\n");
        buffer.append("    <stringArray builtin=\"atomRef\">" + to + "</stringArray>\n");
        buffer.append("  </bondArray>\n");
        buffer.append("</molecule>\n");
        return buffer.toString();
    }
}
