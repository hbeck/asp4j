package asp4j.solver.object;

import asp4j.lang.answerset.AnswerSets;
import asp4j.mapping.direct.ObjectAtom;
import asp4j.mapping.direct.OutputAtom;
import asp4j.program.Program;

/**
 * TODO generalize
 *
 * @author hbeck
 * date May 19, 2013
 */
public interface ObjectSolver {
    
    AnswerSets<OutputAtom> getAnswerSets(Program<ObjectAtom> program, OutputAtomBinding binding) throws Exception;    
    
    AnswerSets<Object> getAnswerSets(Program<Object> program, ObjectBinding binding) throws Exception;    

}
