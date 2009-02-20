/* Starter.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
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

package be.ugent.caagt.pg;

import be.ugent.caagt.pg.embedder.gui.EmbedderRunner;
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
