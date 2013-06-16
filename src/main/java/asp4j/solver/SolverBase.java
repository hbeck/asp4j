package asp4j.solver;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.program.Program;
import asp4j.solver.call.AnswerSetsSolverCall;
import asp4j.solver.call.QuerySolverCall;
import asp4j.solver.call.SolverCall;
import asp4j.solver.query.BooleanQuery;
import asp4j.solver.query.Query;
import asp4j.solver.query.TupleQuery;
import asp4j.solver.query.TupleQueryResult;
import asp4j.util.ParseUtils;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck Apr 14, 2013
 */
public abstract class SolverBase implements Solver {

    protected int lastProgramHashCode;
    protected List<AnswerSet<Atom>> lastProgramAnswerSets;
//    protected Boolean lastBooleanQuery;
//    protected int lastBooleanQueryHashCode;

    public SolverBase() {
        lastProgramAnswerSets = null;
    }

    @Override
    public List<AnswerSet<Atom>> getAnswerSets(Program<Atom> program) throws SolverException {
        try {
            if (lastProgramAnswerSets != null && program.hashCode() == lastProgramHashCode) {
                return lastProgramAnswerSets;
            }
            lastProgramHashCode = program.hashCode();
            //
            SolverCall solverCall = getAnswerSetsSolverCall(program);
            Process exec = Runtime.getRuntime().exec(solverCall.create());
            List<String> answerSetStrings = getAnswerSetStrings(exec);
            return lastProgramAnswerSets = Collections.unmodifiableList(mapAnswerSetStrings(answerSetStrings));
        } catch (IOException | ParseException e) {
            throw new SolverException(e);
        }
    }

    @Override
    public Set<Atom> getConsequence(Program<Atom> program, ReasoningMode mode) throws SolverException {
        List<AnswerSet<Atom>> as = getAnswerSets(program);
        switch (mode) {
            case BRAVE:
                return braveConsequence(as);
            case CAUTIOUS:
                return cautiousConsequence(as);
            default:
                return null;
        }
    }

    @Override
    public boolean booleanQuery(Program<Atom> program, BooleanQuery query, ReasoningMode reasoningMode) throws SolverException {
        try {
            SolverCall solverCall = getQuerySolverCall(program, query, reasoningMode);
            Process exec = Runtime.getRuntime().exec(solverCall.create());
            return getBooleanQueryResult(exec);
        } catch (IOException | ParseException e) {
            throw new SolverException(e);
        }
    }

    @Override
    public TupleQueryResult tupleQuery(Program<Atom> program, TupleQuery query, ReasoningMode reasoningMode) throws SolverException {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }
    
    private SolverCall getAnswerSetsSolverCall(Program<Atom> program) {
        return new AnswerSetsSolverCall(program) {
            @Override
            public String getSolverCommand() {
                return answerSetsSolverCommand();
            }
        };
    }

    private SolverCall getQuerySolverCall(Program<Atom> program, Query query, final ReasoningMode reasoningMode) {
        return new QuerySolverCall(program, query) {
            @Override
            public String getSolverCommand() {
                return querySolverCommand(reasoningMode);
            }
        };
    }

    /**
     * command to start the solver, including params (excluding input programs)
     *
     * @return part before list of files
     */
    protected abstract String answerSetsSolverCommand();

    protected abstract String querySolverCommand(ReasoningMode reasoningMode);

    /**
     * @return list of strings, each representing an answer set
     */
    protected abstract List<String> getAnswerSetStrings(Process exec) throws IOException;
    
    protected abstract boolean getBooleanQueryResult(Process exec) throws IOException, ParseException;

    /**
     * prepare answer set string for tokenization. e.g. surrounding braces may
     * be removed.
     *
     * @param answerSetString
     * @return string to tokenize based on standard configuration
     * @see anwerSetDelimiter
     */
    protected abstract String prepareAnswerSetString(String answerSetString);

    /**
     * @return separator of atoms withing an answer set
     */
    protected abstract String atomDelimiter();

    //
    //  std implementations
    //
    protected void clear() {
        lastProgramAnswerSets = null;
    }

    /**
     * maps a list of answer sets, represented as strings, to a list of (low
     * level) AnswerSet objects
     */
    protected List<AnswerSet<Atom>> mapAnswerSetStrings(List<String> answerSetStrings) throws ParseException {
        List<AnswerSet<Atom>> answerSets = new ArrayList<>();
        for (String answerSetString : answerSetStrings) {
            answerSetString = prepareAnswerSetString(answerSetString);
            String[] atomStrings = answerSetString.split(atomDelimiter());
            Set<Atom> atoms = new HashSet<>();
            for (String atomString : atomStrings) {
                atoms.add(ParseUtils.parseAtom(atomString));
            }
            answerSets.add(new AnswerSetImpl<>(atoms));
        }
        return answerSets;
    }

    protected Set<Atom> cautiousConsequence(List<AnswerSet<Atom>> answerSets) {
        Set<Atom> intersection = new HashSet<>();
        Iterator<AnswerSet<Atom>> it = answerSets.iterator();
        if (it.hasNext()) {
            intersection.addAll(it.next().atoms());
            while (it.hasNext()) {
                intersection.retainAll(it.next().atoms());
            }
        }
        return Collections.unmodifiableSet(intersection);
    }

    protected Set<Atom> braveConsequence(List<AnswerSet<Atom>> answerSets) {
        Set<Atom> set = new HashSet<>();
        for (AnswerSet<Atom> answerSet : answerSets) {
            set.addAll(answerSet.atoms());
        }
        return Collections.unmodifiableSet(set);
    }
}
