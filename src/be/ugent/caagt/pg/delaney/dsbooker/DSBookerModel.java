/* DSBookerModel.java
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
import be.ugent.caagt.pg.delaney.dsbooker.DelaneySymbolLibrary.LibraryType;
import be.ugent.caagt.pg.io.FileFormatException;
import be.ugent.caagt.pg.io.IOManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nvcleemp
 */
public class DSBookerModel {
    
    private List<DSBookerModelListener> listeners = new ArrayList<DSBookerModelListener>();
    private List<DelaneySymbolLibrary> libraryList = new ArrayList<DelaneySymbolLibrary>();
    private DelaneySymbolLibrary mainLibrary;
    private DelaneySymbolLibrary.LibraryType libraryType = DelaneySymbolLibrary.LibraryType.AS_IS;
    private boolean singleLibrary = false;
    private int symbolsOfferedToMain = 0;
    private Scanner mainScanner;
    
    public void addListener(DSBookerModelListener listener){
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(DSBookerModelListener listener){
        listeners.remove(listener);
    }
    
    private void fireConfigured(){
        for (DSBookerModelListener listener : listeners)
            listener.configured();
    }
    
    private void fireNewGraphInLibrary(DelaneySymbolLibrary library, DelaneySymbol originalSymbol){
        for (DSBookerModelListener listener : listeners)
            listener.newGraphInLibrary(library, originalSymbol);
    }
    
    private void fireFinish(){
        for (DSBookerModelListener listener : listeners)
            listener.finish();
    }
    
    public void initialize(List<File> libraries){
        //load library files
        DelaneySymbolLibrary library = new DelaneySymbolLibrary(libraryType);
        for (File file : libraries) {
            for (DelaneySymbol symbol : IOManager.readDS(file))
                library.add(symbol);
            
            if(!singleLibrary){
                libraryList.add(library);
                library = new DelaneySymbolLibrary(libraryType);
            }
        }
        if(singleLibrary)
            libraryList.add(library);
        
        //create main library
        mainLibrary = new DelaneySymbolLibrary(libraryType);
        
        fireConfigured(); //method returns when all listeners are ready
        read();
    }
    
    public void read(){
        
        if(mainScanner==null)//read all symbols from stdin
            mainScanner = new Scanner(System.in);
        while(mainScanner.hasNextLine()){
            try {
                String line = mainScanner.nextLine().trim();
                if (!line.startsWith("#") && line.length()!=0)
                    addToMain(IOManager.readDS(line));
            } catch (FileFormatException ex) {
                Logger.getLogger(DSBookerModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fireFinish();
    }
    
    public void setMainInput(File inputFile) throws FileNotFoundException{
        mainScanner = new Scanner(inputFile);
    }
    
    private void addToMain(DelaneySymbol symbol){
        symbolsOfferedToMain++;
        if(mainLibrary.add(symbol))
            fireNewGraphInLibrary(mainLibrary, symbol);
    }
    
    public DelaneySymbolLibrary getMainLibrary(){
        return mainLibrary;
    }
    
    /**
     * 
     * @return a list of Delaney symbols that are only present in the main library and not in any of the library files.
     */
    public List<DelaneySymbol> mainLibraryPlus(){
        if(libraryList.size()==0)
            return mainLibrary.getList();
        List<DelaneySymbol> list = mainLibrary.extras(libraryList.get(0));
        for (int i = 1; i < libraryList.size(); i++)
            list.retainAll(mainLibrary.extras(libraryList.get(i)));
        return list;
    }
    
    /**
     * 
     * @return a list of Delaney symbols that are only present in the library files and not in the main library.
     */
    public List<DelaneySymbol> mainLibraryMinus(){
        List<DelaneySymbol> list = new ArrayList<DelaneySymbol>();
        for (DelaneySymbolLibrary lib : libraryList)
            list.addAll(lib.getList());
        
        list.removeAll(mainLibrary.getList());
        return list;
    }
    
    public List<DelaneySymbol> getUnion(){
        List<DelaneySymbol> list = mainLibrary.getList();
        for (DelaneySymbolLibrary lib : libraryList)
            for (DelaneySymbol symbol : lib.getList())
                if(!list.contains(symbol))
                    list.add(symbol);

        return list;
    }

    public void setLibraryType(LibraryType libraryType) {
        this.libraryType = libraryType;
    }
}
