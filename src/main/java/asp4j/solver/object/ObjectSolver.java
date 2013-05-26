package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.solver.ReasoningMode;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author hbeck
 * date May 19, 2013
 */
public interface ObjectSolver {
    
    List<AnswerSet<Object>> getAnswerSets(Program<?> program, FilterBinding binding) throws Exception;
    
    Set<Object> getConsequence(Program<?> program, FilterBinding binding, ReasoningMode mode) throws Exception;

}
