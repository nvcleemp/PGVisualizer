/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney;

/**
 *
 * @author nvcleemp
 */
public class Chamber {
    
    private final DelaneySymbol symbol;
    private Chamber[] sigma;
    private int m[];

    public Chamber(DelaneySymbol symbol) {
        this.symbol = symbol;
        sigma = new Chamber[symbol.getDimension()+1];
        m = new int[symbol.getDimension()];
    }
    
    public Chamber sigma(int i){
        return sigma[i];
    }
    
    public void setSigma(int i, Chamber chamber){
        sigma[i] = chamber;
    }
    
    public int m(int i){
        return m[i];
    }

    public void setM(int i, int mValue){
        m[i] = mValue;
    }
    
    public DelaneySymbol getSymbol(){
        return symbol;
    }
    
    Chamber copy(DelaneySymbol symbol){
        Chamber chamber = new Chamber(symbol);
        
        return chamber;
    }

}
