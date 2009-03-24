/* JmolPanel.java
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

package be.ugent.caagt.pg.visualizer.structures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.MessageFormat;
import javax.swing.JPanel;

import org.jmol.api.JmolViewer;

public class JmolPanel extends JPanel {

    private JmolViewer viewer;
    private PGJmolAdapter adapter;

    public JmolPanel() {
        super();
        adapter = new PGJmolAdapter();
        //viewer = JmolSimpleViewer.allocateSimpleViewer(this, adapter);
        viewer = JmolViewer.allocateViewer(this, adapter);
    }

    public void setMolecule(Molecule molecule){
        adapter.setMolecule(molecule);
    }
    
    public JmolViewer getViewer() {
        return viewer;
    }
    
    final Dimension currentSize = new Dimension();
    final Rectangle rectClip = new Rectangle();
    final Dimension preferredSize = new Dimension(500, 300);

    @Override
    public void paint(Graphics g) {
        getSize(currentSize);
        g.getClipBounds(rectClip);
        viewer.renderScreenImage(g, currentSize, rectClip);
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public Molecule getMolecule() {
        return adapter.getMolecule();
    }
    
    public void setBackgroundColor(Color c){
        String red = MessageFormat.format("00{0}", Integer.toHexString(c.getRed()));
        String green = MessageFormat.format("00{0}", Integer.toHexString(c.getGreen()));
        String blue = MessageFormat.format("00{0}", Integer.toHexString(c.getBlue()));
        String colorString = MessageFormat.format("[x{0}{1}{2}]",
                red.substring(red.length()-2),
                green.substring(green.length()-2),
                blue.substring(blue.length()-2));
        viewer.setColorBackground(colorString);
    }
}
