/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.visualizer.molecule;

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
