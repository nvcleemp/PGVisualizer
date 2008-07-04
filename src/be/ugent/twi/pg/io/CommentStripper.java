/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.twi.pg.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nvcleemp
 */
public class CommentStripper {
    
    public static void stripComment(File in, File out) throws FileNotFoundException, IOException{
        Scanner s = new Scanner(in);
        Writer w = new FileWriter(out);
        while(s.hasNextLine()){
            String line = s.nextLine().trim();
            if(line.length()>0 && !line.startsWith("#"))
                w.write(line + "\n");
        }
        w.close();
        s.close();
    }
    
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.exit(1);
            }
            stripComment(new File(args[0]), new File(args[1]));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CommentStripper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CommentStripper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
