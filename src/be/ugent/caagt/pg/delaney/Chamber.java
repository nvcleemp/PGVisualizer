/* Chamber.java
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

package be.ugent.caagt.pg.delaney;

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
