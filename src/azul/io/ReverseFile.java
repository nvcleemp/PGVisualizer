/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.io;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author nvcleemp
 */
public class ReverseFile {
     public static void main(String[] args){

        FileWriter out = null;
        try {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) {
                System.exit(0);
            }
            ArrayList<String> list = new ArrayList<String>();
            if (chooser.getSelectedFile().exists()) {
                try {
                    Scanner fileScanner = new Scanner(chooser.getSelectedFile());
                    while (fileScanner.hasNextLine()) {
                        String line = fileScanner.nextLine().trim();
                        list.add(line);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ReverseFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (chooser.showSaveDialog(null) == JFileChooser.CANCEL_OPTION || chooser.getSelectedFile().exists()) {
                System.exit(0);
            }
            out = new FileWriter(chooser.getSelectedFile());
            for (int i = list.size() - 1; i >= 0; i--) {
                out.write(list.get(i));
                out.write("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(ReverseFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ReverseFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

}
