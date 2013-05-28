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
    
    /**
     * list of answer sets in order as reported by the solver
     * 
     * @param program
     * @return answer sets
     * @throws Exception 
     */
    List<AnswerSet<Atom>> getAnswerSets(Program<Atom> program) throws Exception;

    /**
     * derive logical consequence of a program
     * 
     * @param program
     * @param mode BRAVE reports atoms found in any answer set, CAUTIOUS those found in all answer sets
     * @return logical consequence by means of the answer set semantics
     * @throws Exception 
     */
    Set<Atom> getConsequence(Program<Atom> program, ReasoningMode mode) throws Exception;
    
}
