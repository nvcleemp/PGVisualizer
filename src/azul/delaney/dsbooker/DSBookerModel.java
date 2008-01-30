/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package azul.delaney.dsbooker;

import azul.delaney.DelaneySymbol;
import azul.delaney.dsbooker.DelaneySymbolLibrary.LibraryType;
import azul.io.FileFormatException;
import azul.io.IOManager;
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
