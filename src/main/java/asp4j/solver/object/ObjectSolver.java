package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.solver.ReasoningMode;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck May 19, 2013
 */
public interface ObjectSolver {
    
    List<AnswerSet<Object>> getAnswerSets(Program<?> program) throws Exception;

    List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding) throws Exception;
    
    List<AnswerSet<Object>> getAnswerSets(Program<?> program, Filter filter) throws Exception;
    
    List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding, Filter filter) throws Exception;
    
    Set<Object> getConsequence(Program<?> program, ReasoningMode mode) throws Exception;

    Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding) throws Exception;
    
    Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Filter filter) throws Exception;
    
    Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding, Filter filter) throws Exception;
    
}
