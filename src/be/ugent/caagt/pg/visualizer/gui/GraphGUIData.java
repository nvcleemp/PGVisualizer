/* GraphGUIData.java
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

import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Graph;
import be.ugent.caagt.pg.visualizer.groups.WallpaperGroup;

/**
 *
 * @author nvcleemp
 */
public class GraphGUIData {
    private VertexHighlighter highlighter = null;
    private FaceHighlighter faceHighlighter = null;
    private String symbol;
    private WallpaperGroup group;
    private String comment;

    public GraphGUIData() {
        comment = "";
    }

    public GraphGUIData(String comment) {
        this.comment = comment;
    }

    public VertexHighlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(VertexHighlighter highlighter) {
        this.highlighter = highlighter;
    }

    public FaceHighlighter getFaceHighlighter() {
        return faceHighlighter;
    }

    public void setFaceHighlighter(FaceHighlighter faceHighlighter) {
        this.faceHighlighter = faceHighlighter;
    }
    
    public String getComment(){
        return comment;
    }

    public String getSymbol() {
        return symbol;
    }

    public WallpaperGroup getGroup() {
        return group;
    }
    
    public String export(){
        StringBuffer buf = new StringBuffer();
        if(faceHighlighter!=null){
            buf.append("facehighlight ");
            buf.append(faceHighlighter.export());
            buf.append(" # ");
        }
        if(symbol!=null){
            buf.append("symbol ");
            buf.append(symbol);
            buf.append(" # ");
        }
        if(group!=null){
            buf.append("group ");
            buf.append(group);
            buf.append(" # ");
        }
        buf.append(comment);
        return buf.toString();
    }

    public static GraphGUIData createGraphGUIData(String s, Graph g){
        GraphGUIData data = new GraphGUIData();
        StringBuffer comment = new StringBuffer();
        String[] parts = s.split("#");
        for (String string : parts) {
            if(string.startsWith("facehighlight ")){
                DefaultFaceHightlighter faceHighlighter = new DefaultFaceHightlighter();
                if(g instanceof DefaultGraph)
                    faceHighlighter.importHighlighting((DefaultGraph)g, string.substring(14));
                data.faceHighlighter = faceHighlighter;
            } else if(string.trim().startsWith("symbol ")){
                data.symbol = string.substring(7).trim();
            } else if(string.trim().startsWith("group ")){
                try {
                    data.group = WallpaperGroup.valueOf(string.substring(6).trim());
                } catch (IllegalArgumentException ex) {
                    data.group = WallpaperGroup.UNKNOWN;
                }
            } else
                comment.append(string);
        }
        data.comment = comment.toString();
        if(data.symbol==null)
            System.out.println("ERROR: " + s);
        return data;
    }
}
