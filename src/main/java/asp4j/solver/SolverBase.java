package asp4j.solver;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.program.Program;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck 
 * date Apr 14, 2013
 */
public abstract class SolverBase implements Solver {
    
    protected int lastProgramHashCode;
    protected List<AnswerSet<Atom>> lastProgramAnswerSets;

    public SolverBase() {
        init();
    }

    private void init() {
        lastProgramAnswerSets=null;
    }
    
    protected void clear() {
        lastProgramAnswerSets=null;
    }

    protected void preSolver(Program<Atom> program) throws Exception {        
    }

    protected abstract String solverCommand(Program<Atom> program);

    protected void postSolver(Program<Atom> program) throws Exception {
    }

    /**
     * @return list of strings, each representing an answer set. atoms
     * comma-separated. {optionally, surrounded, by, braces}
     */
    protected abstract List<String> getAnswerSetStrings(Process exec) throws IOException;

    protected abstract List<AnswerSet<Atom>> mapAnswerSetStrings(List<String> strings);

    protected void runVoidExec(String cmd) throws Exception {
        if (cmd == null || cmd.isEmpty()) {
            return;
        }
        Process exec = Runtime.getRuntime().exec(cmd);
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.err.println(line);
        }
        exec.waitFor();
        int exitValue = exec.exitValue();
        if (exitValue != 0) {
            throw new Exception(cmd + " exit: " + exitValue);
        }
    }

    @Override
    public List<AnswerSet<Atom>> getAnswerSets(Program<Atom> program) throws Exception {
        if (lastProgramAnswerSets!=null && program.hashCode()==lastProgramHashCode) {
            return lastProgramAnswerSets;
        }
        lastProgramHashCode=program.hashCode();
        preSolver(program);
        Process exec = Runtime.getRuntime().exec(solverCommand(program));
        List<String> answerSetStrings = getAnswerSetStrings(exec);
        postSolver(program);        
        return lastProgramAnswerSets=Collections.unmodifiableList(mapAnswerSetStrings(answerSetStrings));

    }

    @Override
    public Set<Atom> getConsequence(Program<Atom> program, ReasoningMode mode) throws Exception {
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

    protected Set<Atom> cautiousConsequence(List<AnswerSet<Atom>> answerSets) {
        Iterator<AnswerSet<Atom>> it = answerSets.iterator();
        Set<Atom> intersection = new HashSet();
        if (it.hasNext()) {
            intersection.addAll(it.next().atoms());
            while (it.hasNext()) {
                intersection.retainAll(it.next().atoms());
            }
        }
        return Collections.unmodifiableSet(intersection);
    }

    protected Set<Atom> braveConsequence(List<AnswerSet<Atom>> answerSets) {
        Set<Atom> set = new HashSet();
        for (AnswerSet<Atom> answerSet : answerSets) {
            set.addAll(answerSet.atoms());
        }
        return Collections.unmodifiableSet(set);
    }
}
