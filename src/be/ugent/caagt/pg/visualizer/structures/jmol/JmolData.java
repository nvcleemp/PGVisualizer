/* JmolData.java
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

package be.ugent.caagt.pg.visualizer.structures.jmol;

import org.jmol.api.JmolViewer;

/**
 * Object that stores some data about the Jmol display. Currently it saves
 * the translucent state of atoms and bonds, because these need to be reset
 * each time the color changes.
 * 
 * @author nvcleemp
 */
public class JmolData {

    private JmolViewer viewer;
    private boolean atomsTranslucent = false;
    private boolean bondsTranslucent = false;

    public JmolData(JmolViewer viewer) {
        this.viewer = viewer;
    }

    public boolean areAtomsTranslucent() {
        return atomsTranslucent;
    }

    public void setAtomsTranslucent(boolean atomsTranslucent) {
        this.atomsTranslucent = atomsTranslucent;
    }

    public boolean areBondsTranslucent() {
        return bondsTranslucent;
    }

    public void setBondsTranslucent(boolean bondsTranslucent) {
        this.bondsTranslucent = bondsTranslucent;
    }

    public JmolViewer getViewer() {
        return viewer;
    }

}
