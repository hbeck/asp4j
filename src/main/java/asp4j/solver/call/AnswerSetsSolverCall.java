package asp4j.solver.call;

import asp4j.lang.Atom;
import asp4j.program.Program;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public abstract class AnswerSetsSolverCall extends SolverCallBase {
    
    public AnswerSetsSolverCall(Program<Atom> program) {
        super(program);
    }   
    
}
