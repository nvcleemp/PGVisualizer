/* CommentStripper.java
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
