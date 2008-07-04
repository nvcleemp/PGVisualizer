/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.ugent.caagt.pg.io;

/**
 *
 * @author nvcleemp
 */
public class FileFormatException extends Exception {

    public FileFormatException(String message) {
        super(message);
    }

    public FileFormatException(Throwable cause) {
        super(cause);
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
    
