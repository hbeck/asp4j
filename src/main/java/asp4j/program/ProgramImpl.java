package asp4j.program;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author hbeck
 * date May 19, 2013
 */
public class ProgramImpl<T> implements Program<T> {
    
    private Collection<T> input;
    private Collection<File> files;
    
    public ProgramImpl() {
    }

    public ProgramImpl(Collection<T> input, Collection<File> files) {
        this.input = input;
        this.files = files;
    }

    @Override
    public Collection<T> getInput() {
        return input;
    }

    @Override
    public Collection<File> getFiles() {
        return files;
    }

    @Override
    public void setInput(Collection<T> input) {
        this.input = input;
    }

    @Override
    public void setFiles(Collection<File> files) {
        this.files = files;
    }
    
}
