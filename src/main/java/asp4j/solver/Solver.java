package asp4j.solver;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.program.Program;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck 
 * date 2013-04-14
 */
public interface Solver {
    
    List<AnswerSet<Atom>> getAnswerSets(Program<Atom> program) throws Exception;
    
    Set<Atom> getConsequence(Program<Atom> program, ReasoningMode mode) throws Exception;
    
}
