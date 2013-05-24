package asp4j.solver;

import asp4j.lang.Atom;
import asp4j.lang.answerset.AnswerSets;
import asp4j.program.Program;

/**
 *
 * @author hbeck 
 * date 2013-04-14
 */
public interface Solver {
    
    AnswerSets<Atom> getAnswerSets(Program<Atom> program) throws Exception;
    
}
