/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author nvcleemp
 */
public class DelaneySymbol {
    private int dimension;
    private Chamber[] chambers;
    private DelaneySymbol canonical = null;
    private DelaneySymbol minimal = null;
    private Boolean orientable = null;

    public DelaneySymbol(int dimension, int size) {
        this.dimension = dimension;
        chambers = new Chamber[size];
        createChambers();
    }

    DelaneySymbol(int dimension, Chamber[] chambers) {
        this.dimension = dimension;
        this.chambers = chambers;
    }

    public int getDimension() {
        return dimension;
    }
    
    public int getSize() {
        return chambers.length;
    }
    
    public Chamber getChamber(int number){
        return chambers[number];
    }
    
    private void createChambers(){
        for (int i = 0; i < chambers.length; i++)
            chambers[i] = new Chamber(this);
    }
    
    public void printSymbol(){
        printSymbol(System.out);
    }

    public void printSymbol(PrintStream out){
        out.print("|   |");
        for (int i = 0; i < dimension + 1; i++)
            out.printf("s%1$2d|", i);
        for (int i = 0; i < dimension; i++)
            out.printf("m%1$1d%2$1d|", i, i+1);
        out.println();
        
        for (int i = 0; i < 8*dimension + 9; i++) 
            out.print("=");
        out.println();

        for (int i = 0; i < chambers.length; i++) {
            out.printf("|%1$3d|", i);
            for (int j = 0; j < dimension + 1; j++) 
                out.printf("%1$3d|", getIndex(chambers[i].sigma(j)));
            for (int j = 0; j < dimension; j++)
                out.printf("%1$3d|", chambers[i].m(j));
            out.println();
        }
        out.println();
    }
    
    public int getIndex(Chamber chamber){
        int i = 0;
        while(i<chambers.length && chambers[i]!=chamber) i++;
        if(i==chambers.length)
            return -1;
        else
            return i;
    }

    public DelaneySymbol getCanonical() {
        if(canonical != null)
            return canonical;
        
        canonical = getBasicDelaney().makeCanonical().toDelaneySymbol();
        canonical.setCanonical(canonical);
        
        return canonical;
    }

    public DelaneySymbol getMinimal() {
        if(minimal != null)
            return minimal;
        
        minimal = getBasicDelaney().getMinimal().toDelaneySymbol();
        minimal.setMinimal(minimal);
        minimal.setCanonical(minimal);
        
        return minimal;
    }

    private void setCanonical(DelaneySymbol canonical) {
        this.canonical = canonical;
    }

    private void setMinimal(DelaneySymbol minimal) {
        this.minimal = minimal;
    }
    
    public BasicDelaney getBasicDelaney(){
        int[][] sigma = new int[chambers.length][dimension+1];
        int[][] m = new int[chambers.length][dimension];
        
        for (int j = 0; j < dimension+1; j++)
            for (int i = 0; i < chambers.length; i++)
                sigma[i][j] = getIndex(chambers[i].sigma(j));
        
        
        for (int j = 0; j < dimension; j++)
            for (int i = 0; i < chambers.length; i++)
                m[i][j] = chambers[i].m(j);
        
        return new BasicDelaney(dimension, sigma, m);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DelaneySymbol other = (DelaneySymbol) obj;
        if (this.dimension != other.dimension)
            return false;
        if (!this.getBasicDelaney().equals(other.getBasicDelaney()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.dimension;
        hash = 13 * hash + getBasicDelaney().hashCode();
        return hash;
    }
    
    public boolean tilingEquals(DelaneySymbol symbol){
        return getCanonical().equals(symbol.getCanonical());
    }
    
    public boolean minimalEquals(DelaneySymbol symbol){
        return getMinimal().equals(symbol.getMinimal());
    }
    
    public boolean isOrientable(){
        if(orientable!=null)
            return orientable;
        
        Map<Chamber, Boolean> orientation = new HashMap<Chamber, Boolean>();
        Stack<Chamber> stack = new Stack<Chamber>();
        
        orientation.put(chambers[0], true);
        stack.push(chambers[0]);
        
        while(!stack.empty()){
            Chamber chamber = stack.pop();
            for (int i = 0; i <= dimension; i++) {
                if(orientation.containsKey(chamber.sigma(i))){
                    if(orientation.get(chamber.sigma(i)).equals(orientation.get(chamber))){
                        orientable = Boolean.FALSE;
                        return orientable;
                    }
                } else {
                    orientation.put(chamber.sigma(i), !orientation.get(chamber));
                    stack.push(chamber.sigma(i));
                }
            }
        }
        
        orientable = Boolean.TRUE;
        return orientable;
    }
}
