package asp4j.program;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author hbeck
 * date May 19, 2013
 */
public interface Program<T> {
    
    Collection<T> getInput();

    Collection<File> getFiles();

    void setFiles(Collection<File> files);

    void setInput(Collection<T> input);
}
