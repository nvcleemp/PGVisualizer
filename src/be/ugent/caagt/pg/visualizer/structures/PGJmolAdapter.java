/* PGJmolAdapter.java
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

package be.ugent.caagt.pg.visualizer.structures;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.Hashtable;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolFileReaderInterface;

/**
 *
 * @author nvcleemp
 */
public class PGJmolAdapter extends JmolAdapter{
    
    private Molecule clientFile;

    public PGJmolAdapter() {
        super("PGJmolAdapter");
    }

    @Override
    public Object openBufferedReader(String name, String type, BufferedReader bufferedReader, Hashtable htParams) {
        return clientFile;
    }

    @Override
    public Object openBufferedReader(String name, BufferedReader bufferedReader) {
        return clientFile;
    }

    @Override
    public Object openBufferedReader(String name, BufferedReader bufferedReader, Hashtable htParams) {
        return clientFile;
    }

    @Override
    public Object openBufferedReader(String name, String type, BufferedReader bufferedReader) {
        return clientFile;
    }

    @Override
    public Object openBufferedReaders(JmolFileReaderInterface fileReader, String[] names, String[] types, Hashtable[] htParams) {
        return clientFile;
    }

    @Override
    public Object openDOMReader(Object DOMNode) {
        return clientFile;
    }

    @Override
    public Object openZipFiles(InputStream is, String fileName, String[] zipDirectory, Hashtable htParams, boolean asBufferedReader) {
        return clientFile;
    }

    public int getEstimatedAtomCount(Object clientFile) {
        if(!(clientFile instanceof Molecule))
            throw new RuntimeException("PGJmolAdpater used with wrong clientFile.");
        Molecule molecule = (Molecule)clientFile;
        return molecule.getSize();
    }

    public JmolAdapter.AtomIterator getAtomIterator(Object clientFile) {
        if(!(clientFile instanceof Molecule))
            throw new RuntimeException("PGJmolAdpater used with wrong clientFile.");
        Molecule molecule = (Molecule)clientFile;
        JmolAdapter.AtomIterator it = new MyAtomIterator(molecule);
        return it;
    }

    @Override
    public JmolAdapter.BondIterator getBondIterator(Object clientFile) {
        if(!(clientFile instanceof Molecule))
            throw new RuntimeException("PGJmolAdpater used with wrong clientFile.");
        Molecule molecule = (Molecule)clientFile;
        JmolAdapter.BondIterator it = new MyBondIterator(molecule);
        return it;
    }

    public Molecule getMolecule() {
        return clientFile;
    }

    void setMolecule(Molecule molecule) {
        clientFile = molecule;
    }
    
    private class MyAtomIterator extends JmolAdapter.AtomIterator {
        
        private Molecule molecule;
        private int position = -1;

        public MyAtomIterator(Molecule molecule) {
            this.molecule = molecule;
        }

        @Override
        public boolean hasNext() {
            position++;
            return (position < molecule.getSize());
        }

        @Override
        public Object getUniqueID() {
            return position;
        }

        @Override
        public float getX() {
            return molecule.getX(position)*10;
        }

        @Override
        public float getY() {
            return molecule.getY(position)*10;
        }

        @Override
        public float getZ() {
            return molecule.getZ(position)*10;
        }

        @Override
        public String getElementSymbol() {
            return "C";
        }        
        
    }
    
    private class MyBondIterator extends JmolAdapter.BondIterator {
        
        private Molecule molecule;
        private int position = -1;

        public MyBondIterator(Molecule molecule) {
            this.molecule = molecule;
        }

        @Override
        public boolean hasNext() {
            position++;
            return position < molecule.getEdgeSize();
        }

        @Override
        public Object getAtomUniqueID1() {
            return molecule.edgeFrom(position);
        }

        @Override
        public Object getAtomUniqueID2() {
            return molecule.edgeTo(position);
        }

        @Override
        public int getEncodedOrder() {
            return 1;
        }
        
    }

}
