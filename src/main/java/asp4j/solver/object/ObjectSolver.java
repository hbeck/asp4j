package asp4j.solver.object;

import asp4j.lang.answerset.AnswerSets;
import asp4j.program.Program;

/**
 * 
 * @author hbeck
 * date May 19, 2013
 */
public interface ObjectSolver {
    
    <In,Out> AnswerSets<Out> getAnswerSets(Program<In> program, FilterBinding<Out> binding) throws Exception;

}
