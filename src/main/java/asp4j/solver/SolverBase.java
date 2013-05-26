package asp4j.solver;

import asp4j.lang.Atom;
import asp4j.lang.answerset.AnswerSet;
import asp4j.lang.answerset.AnswerSets;
import asp4j.lang.answerset.AnswerSetsImpl;
import asp4j.program.Program;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @author hbeck
 * date Apr 14, 2013
 */
public abstract class SolverBase implements Solver {

    public SolverBase() {
        init();
    }

    private void init() {
    }
    
    protected void preSolver(Program<Atom> program) throws Exception {
    }

    protected abstract String solverCommand(Program<Atom> program);

    protected void postSolver(Program<Atom> program) throws Exception {
    }

    /**
     * @return list of strings, each representing an answer set. atoms comma-separated. {optionally, surrounded, by, braces}
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
    public AnswerSets<Atom> getAnswerSets(Program<Atom> program) throws Exception {
        preSolver(program);
        Process exec = Runtime.getRuntime().exec(solverCommand(program));
        List<String> answerSetStrings = getAnswerSetStrings(exec);
        postSolver(program);
        return new AnswerSetsImpl(mapAnswerSetStrings(answerSetStrings));
    }

}
