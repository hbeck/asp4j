package asp4j.solver.call;

import asp4j.lang.Atom;
import asp4j.program.Program;
import java.io.IOException;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public interface SolverCall {
    
    Program<Atom> getProgram();
    
    String getSolverCommand();
    
    String create() throws IOException;

}
