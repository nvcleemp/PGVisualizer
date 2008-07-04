/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.delaney.dsbooker;

import be.ugent.caagt.pg.delaney.DelaneySymbol;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nvcleemp
 */
public class DelaneySymbolLibrary {
    
    public enum LibraryType {
        MINIMAL{DelaneySymbol getSymbol(DelaneySymbol symbol){return symbol.getMinimal();}},
        CANONICAL{DelaneySymbol getSymbol(DelaneySymbol symbol){return symbol.getCanonical();}},
        AS_IS{DelaneySymbol getSymbol(DelaneySymbol symbol){return symbol;}};
        
        abstract DelaneySymbol getSymbol(DelaneySymbol symbol);
    }
    
    private LibraryType type;
    private List<DelaneySymbol> library = new ArrayList<DelaneySymbol>();

    public DelaneySymbolLibrary(LibraryType type) {
        this.type = type;
    }

    public boolean containsActualSymbol(DelaneySymbol symbol) {
        return library.contains(symbol);
    }

    public boolean contains(DelaneySymbol symbol) {
        return library.contains(type.getSymbol(symbol));
    }

    public boolean add(DelaneySymbol symbol) {
        if(!library.contains(type.getSymbol(symbol)))
            return library.add(type.getSymbol(symbol));
        else
            return false;
    }
    
    public DelaneySymbolLibrary getLibraryForType(LibraryType type){
        if(type==this.type)
            return this;
        else {
            DelaneySymbolLibrary newLibrary = new DelaneySymbolLibrary(type);
            for (DelaneySymbol symbol : library)
                newLibrary.add(symbol);
            return newLibrary;
        }
    }
    
    public List<DelaneySymbol> extras(DelaneySymbolLibrary dLibrary){
        List<DelaneySymbol> list = new ArrayList<DelaneySymbol>();
        for (DelaneySymbol symbol : library)
            if(!dLibrary.contains(symbol))
                list.add(symbol);
        return list;
    }
    
    public List<DelaneySymbol> getList(){
        return new ArrayList<DelaneySymbol>(library);
    }
}
