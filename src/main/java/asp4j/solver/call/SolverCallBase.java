package asp4j.solver.call;

import asp4j.lang.Atom;
import asp4j.program.Program;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public abstract class SolverCallBase implements SolverCall {

    protected final Program<Atom> program;
    protected File tmpInputFile;

    public SolverCallBase(Program<Atom> program) {
        this.program = program;
    }

    @Override
    public String create() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(getSolverCommand());
        for (File file : getProgram().getFiles()) {
            sb.append(" ").append(file.getAbsolutePath());
        }
        sb.append(" ").append(tmpInputFile().getAbsolutePath());
        return sb.toString();    
    }

    /** 
     * @return string respresentation of additional input from java
     */
    protected String inputString() {
        StringBuilder sb = new StringBuilder();
        for (Atom atom : program.getInput()) {
            sb.append(atom.toString());
        }
        return sb.toString();
    }

    @Override
    public Program<Atom> getProgram() {
        return program;
    }

    protected File tmpInputFile() throws IOException {
        if (this.tmpInputFile == null) {
            this.tmpInputFile = File.createTempFile("asp4j-tmp-prog-", ".lp");
            this.tmpInputFile.deleteOnExit();
            FileUtils.writeStringToFile(tmpInputFile, inputString());
        }
        return this.tmpInputFile;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.program);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SolverCallBase other = (SolverCallBase) obj;
        if (!Objects.equals(this.program, other.program)) {
            return false;
        }
        return true;
    }
}
