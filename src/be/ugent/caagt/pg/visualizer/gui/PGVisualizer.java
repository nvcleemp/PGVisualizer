/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.gui;

import be.ugent.caagt.pg.visualizer.gui.action.ClearFaceSelectionAction;
import be.ugent.caagt.pg.visualizer.gui.action.FilterAction;
import be.ugent.caagt.pg.visualizer.gui.action.GrowGraphAction;
import be.ugent.caagt.pg.visualizer.gui.action.Show3DAction;
import be.ugent.caagt.pg.visualizer.gui.action.ShowWindowAction;
import be.ugent.caagt.pg.visualizer.gui.toggler.ClipViewToggler;
import be.ugent.caagt.pg.visualizer.gui.toggler.FillFacesToggler;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author nvcleemp
 */
public class PGVisualizer extends JPanel{
    
    private GraphListModel model;
    private TorusView view = new TorusView();
    private JMenuBar menuBar = null;

    public PGVisualizer(File file) {
        model = new DefaultGraphListModel(file);
        setLayout(new BorderLayout());
        add(new ListSelectionNavigator(model.getSelectionModel(), model), BorderLayout.NORTH);
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
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        //controls.add(ToolFactory.createEmbeddedTool(new AzulenoidInfo(model)));
        //controls.add(ToolFactory.createEmbeddedTool(new FaceControl(view, model)));
        //controls.add(ToolFactory.createEmbeddedTool(new ViewController(view)));
        //controls.add(ToolFactory.createEmbeddedTool(new GraphOperations(model)));
        //controls.add(ToolFactory.createEmbeddedTool(new DomainOperations(model)));
        //controls.add(ToolFactory.createEmbeddedTool(new EmbedderControl(model)));
        //controls.add(ExportControl.getPanel(view, model));
        add(controls, BorderLayout.EAST);
    }
    
    public PGVisualizer(GraphListModel graphListModel) {
        model = graphListModel;
        setLayout(new BorderLayout());
        add(new ListSelectionNavigator(model.getSelectionModel(), model), BorderLayout.NORTH);
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
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        //controls.add(new AzulenoidInfo(model));
        //controls.add(new FaceControl(view, model));
        //controls.add(ToolFactory.createEmbeddedTool(new ViewController(view)));
        //controls.add(new GraphOperations(model));
        //controls.add(new DomainOperations(model));
        //controls.add(new EmbedderControl(model));
        //controls.add(ExportControl.getPanel(view, model));
        add(controls, BorderLayout.EAST);
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
            viewMenu.add(new JMenuItem(new ShowWindowAction("Show face option", new ToolWindow(new FaceControl(view, model), target))));
            viewMenu.addSeparator();
            viewMenu.add(new ClipViewToggler(view).getJCheckBoxMenuItem());
            viewMenu.add(new JMenuItem(new ShowWindowAction("Show view option", new ToolWindow(new ViewController(view), target))));
            viewMenu.addSeparator();
            viewMenu.add(new JMenuItem(new Show3DAction(model)));
            viewMenu.addSeparator();
            viewMenu.add(new JMenuItem(new ShowWindowAction("Show info", new ToolWindow(new AzulenoidInfo(model), target))));
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
