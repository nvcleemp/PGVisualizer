/* JmolFrame.java
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

import de.jreality.geometry.IndexedFaceSetFactory;
import de.jreality.ui.viewerapp.ViewerApp;
import de.jreality.ui.viewerapp.ViewerAppMenu;
import de.jreality.ui.viewerapp.actions.view.ToggleNavigator;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *
 * @author nvcleemp
 */
public class JRealityFrame{
    
    private ViewerApp va;
    

    public JRealityFrame() {
        va = null;
    }

    public void display(Tiled3DStructure struc){
        IndexedFaceSetFactory ifsf = new IndexedFaceSetFactory();
    
        double[][] vertices = new double[struc.getSize()][3];
        
        for (int i = 0; i < vertices.length; i++) {
            vertices[i][0] = struc.getX(i);
            vertices[i][1] = struc.getY(i);
            vertices[i][2] = struc.getZ(i);
        }

    
        int[][] faceIndices = new int[struc.getFaceSize()][];
        
        for (int i = 0; i < faceIndices.length; i++) {
            faceIndices[i] = struc.getFaceAt(i);
        }
        
        Color[] colors = new Color[struc.getFaceSize()];
        
        for (int i = 0; i < colors.length; i++) {
            colors[i] = struc.getColorOfFace(i);
        }

    
        ifsf.setVertexCount(vertices.length);
        ifsf.setVertexCoordinates(vertices);
        ifsf.setFaceCount(faceIndices.length);
        ifsf.setFaceIndices(faceIndices);    
        ifsf.setFaceColors(colors);
        ifsf.setGenerateEdgesFromFaces(true);
        ifsf.setGenerateFaceNormals(true);
        ifsf.update();
        if(va!=null){
            va.dispose();
        }
        va = new ViewerApp(ifsf.getIndexedFaceSet());
        va.setAttachNavigator(false);
        va.setExternalNavigator(false);
        va.setAttachBeanShell(false);
        va.setExternalBeanShell(false);
        va.setShowMenu(false);
        va.setExternalNavigator(true);
        va.update();
        JMenuBar menuBar = new JMenuBar();
        JMenu optionMenu = new JMenu("Options");
        optionMenu.add(new JCheckBoxMenuItem(new ToggleNavigator(ViewerAppMenu.TOGGLE_NAVIGATOR, va)));
        menuBar.add(optionMenu);
        va.getFrame().setJMenuBar(menuBar);
        va.display();
	va.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //needed to override value set in ViewerApp
        va.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                va.dispose();
                va = null;
            }
        });
    }
    
}
