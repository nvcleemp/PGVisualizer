/* ReverseFile.java
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

package be.ugent.caagt.pg.io;

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
