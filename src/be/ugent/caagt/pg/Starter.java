/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg;

import be.ugent.caagt.pg.visualizer.gui.EmbedderRunner;
import be.ugent.caagt.pg.visualizer.gui.PGVisualizer;
import be.ugent.caagt.pg.preferences.PGPreferences;
import be.ugent.twijug.jclops.CLManager;
import be.ugent.twijug.jclops.annotations.Option;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Class to start the different section of the PG project.
 * 
 * @author nvcleemp
 */
public class Starter {
    
    private Programs program = Programs.VISUALIZER;
    
    public enum Programs{
        VISUALIZER{
            public void run(File f){
                JFrame frame = new JFrame("PGVisualizer");
                PGVisualizer visualizer = new PGVisualizer(f);
                frame.add(visualizer);
                frame.setJMenuBar(visualizer.getMenuBar(frame));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setVisible(true);
            }
        },
        EMBEDDER{
            public void run(File f){
                JFrame frame = new JFrame("PGEmbedder");
                EmbedderRunner embedder = new EmbedderRunner(f, frame);
                frame.add(embedder);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        };
        
        public abstract void run(File f);
    }
    
    @Option(shortName='p', description="The program to run")
    public void setProgram(Programs program){
        this.program = program;
    }
    
    private void run(String[] remainingArguments){
        File f = null;
        if(remainingArguments.length>0)
            f = new File(remainingArguments[0]);
        if(f==null || !f.exists()){
            JFileChooser chooser;
            String dir = PGPreferences.getInstance().getStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY);
            if(dir==null)
                chooser = new JFileChooser();
            else
                chooser = new JFileChooser(new File(dir));
            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile().exists()){
                f = chooser.getSelectedFile();
                PGPreferences.getInstance().setStringPreference(PGPreferences.Preference.CURRENT_DIRECTORY, f.getParent());
            }else
                f=null;
        }
        if(f!=null){
            program.run(f);
        } else {
            System.err.println("Invalid or no file specified.");
        }
    }

    public static void main(String[] args) {
        Starter starter = new Starter();
        CLManager clm = new CLManager(starter);
        clm.parse(args);
        starter.run(clm.getRemainingArguments());
    }

}
