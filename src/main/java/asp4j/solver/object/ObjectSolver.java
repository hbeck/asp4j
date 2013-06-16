package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.solver.ReasoningMode;
import asp4j.solver.SolverException;
import asp4j.solver.object.query.ObjectBooleanQuery;
import asp4j.solver.object.query.ObjectTupleQuery;
import asp4j.solver.object.query.ObjectTupleQueryResult;
import asp4j.solver.query.BooleanQuery;
import asp4j.solver.query.TupleQuery;
import asp4j.solver.query.TupleQueryResult;
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
    
    boolean booleanQuery(Program<Object> program, BooleanQuery query, ReasoningMode reasoningMode);
    
    boolean booleanQuery(Program<Object> program, ObjectBooleanQuery query, ReasoningMode reasoningMode);

    TupleQueryResult tupleQuery(Program<Object> program, TupleQuery query, ReasoningMode reasoningMode);
    
    ObjectTupleQueryResult tupleQuery(Program<Object> program, ObjectTupleQuery query, ReasoningMode reasoningMode);
    
}
