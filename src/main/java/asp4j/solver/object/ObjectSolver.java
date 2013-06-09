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
    
    List<AnswerSet<Object>> getAnswerSets(Program<?> program) throws SolverException;

    List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding) throws SolverException;
    
    List<AnswerSet<Object>> getAnswerSets(Program<?> program, Filter filter) throws SolverException;
    
    List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding, Filter filter) throws SolverException;
    
    Set<Object> getConsequence(Program<?> program, ReasoningMode mode) throws SolverException;

    Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding) throws SolverException;
    
    Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Filter filter) throws SolverException;
    
    Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding, Filter filter) throws SolverException;
    
}
