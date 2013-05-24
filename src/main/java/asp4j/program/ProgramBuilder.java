package asp4j.program;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hbeck date May 20, 2013
 */
public class ProgramBuilder<T> {

    private Set<T> input;
    private Set<File> files;

    public ProgramBuilder() {
        this.input = new HashSet();
        this.files = new HashSet();
    }

    public Program<T> build() {
        return new ProgramImpl(input, files);
    }

    public ProgramBuilder<T> add(T t) {
        this.input.add(t);
        return this;
    }

    public ProgramBuilder<T> add(File file) {
        this.files.add(file);
        return this;
    }

    public ProgramBuilder<T> addInputs(Collection<T> input) {
        this.input.addAll(input);
        return this;
    }

    public ProgramBuilder<T> addFiles(Collection<File> files) {
        this.files.addAll(files);
        return this;
    }

    public ProgramBuilder<T> remove(T t) {
        input.remove(t);
        return this;
    }

    public ProgramBuilder<T> remove(File file) {
        files.remove(file);
        return this;
    }
}
