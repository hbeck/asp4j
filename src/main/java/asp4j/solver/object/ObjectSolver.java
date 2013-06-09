package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.solver.ReasoningMode;
import asp4j.solver.SolverException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck May 19, 2013
 */
public interface ObjectSolver {
    
    List<AnswerSet<Object>> getAnswerSets(Program<Object> program) throws SolverException;

    List<AnswerSet<Object>> getAnswerSets(Program<Object> program, Binding binding) throws SolverException;
    
    List<AnswerSet<Object>> getAnswerSets(Program<Object> program, Filter filter) throws SolverException;
    
    List<AnswerSet<Object>> getAnswerSets(Program<Object> program, Binding binding, Filter filter) throws SolverException;
    
    Set<Object> getConsequence(Program<Object> program, ReasoningMode mode) throws SolverException;
    
    Set<Object> getConsequence(Program<Object> program, ReasoningMode mode, Binding binding) throws SolverException;
    
    Set<Object> getConsequence(Program<Object> program, ReasoningMode mode, Filter filter) throws SolverException;
    
    Set<Object> getConsequence(Program<Object> program, ReasoningMode mode, Binding binding, Filter filter) throws SolverException;
    
}
