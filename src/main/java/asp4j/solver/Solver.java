package asp4j.solver;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.program.Program;
import asp4j.solver.query.BooleanQuery;
import asp4j.solver.query.TupleQuery;
import asp4j.solver.query.TupleQueryResult;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck Apr 14, 2013
 *
 */
public interface Solver {
    
    /**
     * list of answer sets in order as reported by the solver
     * 
     * @param program
     * @return answer sets
     *  
     */
    List<AnswerSet<Atom>> getAnswerSets(Program<Atom> program) throws SolverException;

    /**
     * derive logical consequence of a program
     * 
     * @param program
     * @param mode BRAVE reports atoms found in some answer set, CAUTIOUS those found in all answer sets
     * @return logical consequence by means of the answer set semantics
     *  
     */
    Set<Atom> getConsequence(Program<Atom> program, ReasoningMode mode) throws SolverException;    

    boolean booleanQuery(Program<Atom> program, BooleanQuery query, ReasoningMode reasoningMode) throws SolverException;

    TupleQueryResult tupleQuery(Program<Atom> program, TupleQuery query, ReasoningMode reasoningMode) throws SolverException;

}
