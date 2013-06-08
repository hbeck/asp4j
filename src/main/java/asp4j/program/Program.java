package asp4j.program;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author hbeck
 *  May 19, 2013
 */
public interface Program<T> {
    
    Collection<T> getInput();

    Collection<File> getFiles();

}
