/* DelaneySymbolLibrary.java
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
