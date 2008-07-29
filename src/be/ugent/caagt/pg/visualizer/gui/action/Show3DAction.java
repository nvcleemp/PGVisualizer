/* Show3DAction.java
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

package be.ugent.caagt.pg.visualizer.gui.action;

import be.ugent.caagt.pg.graph.DefaultGraph;
import be.ugent.caagt.pg.graph.Face;
import be.ugent.caagt.pg.visualizer.gui.Tiled3DStructureDialog;
import be.ugent.caagt.pg.visualizer.gui.GraphListModelListener;
import be.ugent.caagt.pg.visualizer.gui.GraphListModel;
import be.ugent.caagt.pg.visualizer.gui.DefaultFaceHightlighter;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.event.ListDataEvent;

/**
 *
 * @author nvcleemp
 */
public class Show3DAction extends AbstractAction implements GraphListModelListener{

    public Show3DAction(GraphListModel graphListModel) {
        super("Show 3D model");
        this.graphListModel = graphListModel;
        graphListModel.addGraphModelListener(this);
        
    }
    
    private GraphListModel graphListModel;
    private Tiled3DStructureDialog dialog = null;

    public void actionPerformed(ActionEvent e) {
            if(dialog==null) {
                dialog = new Tiled3DStructureDialog();
                DefaultFaceHightlighter dfh = ((DefaultFaceHightlighter)graphListModel.getSelectedGraphGUIData().getFaceHighlighter());
                Map<Face,Color> colors;
                if(dfh==null){
                    colors = new HashMap<Face, Color>();
                } else {
                    colors = dfh.getMap();
                }
                dialog.showDialog(graphListModel.getSelectedGraph(),
                        ((DefaultGraph)graphListModel.getSelectedGraph()).getFaces(),
                        colors);
            } else {
                dialog.setVisible(true);
            }
    }

    public void selectedGraphChanged() {
        dialog = null;
    }

    public void intervalAdded(ListDataEvent e) {
        //
    }

    public void intervalRemoved(ListDataEvent e) {
        //
    }

    public void contentsChanged(ListDataEvent e) {
        //
    }

}
