/* PGVisualizer.java
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

import be.ugent.caagt.pg.visualizer.gui.util.ListSelectionNavigator;
import be.ugent.caagt.pg.visualizer.gui.action.ClearFaceSelectionAction;
import be.ugent.caagt.pg.visualizer.gui.action.FilterAction;
import be.ugent.caagt.pg.visualizer.gui.action.GrowGraphAction;
import be.ugent.caagt.pg.visualizer.gui.action.FiniteStructureAction;
import be.ugent.caagt.pg.visualizer.gui.action.ShowWindowAction;
import be.ugent.caagt.pg.visualizer.gui.toggler.ClipViewToggler;
import be.ugent.caagt.pg.visualizer.gui.toggler.FillFacesToggler;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author nvcleemp
 */
public class PGVisualizer extends JPanel{
    
    private GraphListModel model;
    private TorusView view = new TorusView();
    private JMenuBar menuBar = null;

    public PGVisualizer(File file) {
        this(new DefaultGraphListModel(file));
    }
    
    public PGVisualizer(GraphListModel graphListModel) {
        model = graphListModel;
        setLayout(new BorderLayout());
        ListSelectionNavigator nav = new ListSelectionNavigator(model.getSelectionModel(), model);
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "previous");
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "next");
        getActionMap().put("previous", nav.getPreviousAction());
        getActionMap().put("next", nav.getNextAction());
        add(nav, BorderLayout.NORTH);
        view = new TorusView(model);
        view.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TorusView.ViewVertex vv = view.getVertexAt(e.getX(), e.getY());
                TorusView.ViewFace vf = view.getFaceAt(e.getX(), e.getY());
                if(e.isShiftDown()){
                    if(vv!=null){
                        view.getGraphSelectionModel().toggleVertex(vv.vertex);
                    } else if (vf!=null){
                        view.getFaceSelectionModel().toggleFace(vf.face);
                    }
                } else {
                    view.getGraphSelectionModel().clearSelection();
                    view.getFaceSelectionModel().clearSelection();
                    if(vv!=null){
                        view.getGraphSelectionModel().addVertex(vv.vertex);
                    } else if (vf!=null){
                        view.getFaceSelectionModel().toggleFace(vf.face);
                    }
                }
                //view.selectedFace = view.getFaceAt(e.getX(), e.getY());
            }            
        });
        add(view, BorderLayout.CENTER);
    }
    
    public JMenuBar getMenuBar(JFrame target){
        if(menuBar==null){
            menuBar = new JMenuBar();
            menuBar.add(ExportControl.getMenu(view, model));
            JMenu editMenu = new JMenu("Edit");
            editMenu.add(new JMenuItem(new ShowWindowAction("Editor", new ToolWindow(new PeriodicGraphEditor(model), target, true))));
            editMenu.addSeparator();
            editMenu.add(new JMenuItem(new ShowWindowAction("Operations", new ToolWindow(new GraphOperations(model), target))));
            editMenu.add(new JMenuItem(new ShowWindowAction("Domain operations", new ToolWindow(new DomainOperations(model), target))));
            editMenu.addSeparator();
            editMenu.add(new JMenuItem(new ClearFaceSelectionAction(view.getFaceSelectionModel())));
            editMenu.add(new JMenuItem(new ShowWindowAction("Selected faces info", new ToolWindow(new SelectedFacesInfo(view.getFaceSelectionModel(), view), target))));
            editMenu.addSeparator();
            editMenu.add(new JMenuItem(new GrowGraphAction(model)));
            menuBar.add(editMenu);
            JMenu viewMenu = new JMenu("View");
            viewMenu.add(new FillFacesToggler(view).getJCheckBoxMenuItem());
            viewMenu.add(new JMenuItem(new ShowWindowAction("Face option", new ToolWindow(new FaceControl(view, model), target))));
            viewMenu.add(new JMenuItem(new ShowWindowAction("Default color table", new ToolWindow(FaceColorMapping.getDefaultColorTable(), target))));
            viewMenu.addSeparator();
            viewMenu.add(new ClipViewToggler(view).getJCheckBoxMenuItem());
            viewMenu.add(new JMenuItem(new ShowWindowAction("View option", new ToolWindow(new ViewController(view), target))));
            viewMenu.addSeparator();
            viewMenu.add(new JMenuItem(new FiniteStructureAction(model)));
            viewMenu.addSeparator();
            viewMenu.add(new JMenuItem(new ShowWindowAction("Info", new ToolWindow(new GraphInfo(model), target))));
            viewMenu.add(new JMenuItem(new ShowWindowAction("Table", new ListOverviewWindow(model))));
            menuBar.add(viewMenu);
            JMenu embedderMenu = new JMenu("Embedder");
            embedderMenu.add(new JMenuItem(new ShowWindowAction("Embedder runner", new ToolWindow(new EmbedderControl(model), target))));
            menuBar.add(embedderMenu);
            JMenu filterMenu = new JMenu("Filter");
            filterMenu.add(new JMenuItem(new FilterAction(model)));
            menuBar.add(filterMenu);
        }
        return menuBar;
    }
    
    public static void main(String[] args) {
        File f = null;
        if(args.length != 0)
            f = new File(args[0]);
        if(f==null || !f.exists()){
            JFileChooser chooser = new JFileChooser();
            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                f = chooser.getSelectedFile();
        }
        
        if(f.exists()){
            showPGVisualizer(f);
        }
    }
    
    private static void showPGVisualizer(File f){
        JFrame frame = new JFrame("PGVisualizer");
        PGVisualizer visualizer = new PGVisualizer(f);
        frame.add(visualizer);
        frame.setJMenuBar(visualizer.getMenuBar(frame));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

}
