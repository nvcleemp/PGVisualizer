/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.ugent.caagt.pg.delaney.dsbooker;

import be.ugent.twijug.jclops.ArgumentParser;
import be.ugent.twijug.jclops.CLArgumentException;
import java.io.File;

/**
 *
 * @author nvcleemp
 */
public class FileParser implements ArgumentParser<File> {

    public File parseObject(String arg0) throws CLArgumentException {
        File file = new File(arg0);
        return file;
    }

}
