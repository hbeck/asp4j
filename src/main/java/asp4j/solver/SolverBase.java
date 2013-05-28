package asp4j.solver;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.program.Program;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck date Apr 14, 2013
 */
public abstract class SolverBase implements Solver {

    protected int lastProgramHashCode;
    protected List<AnswerSet<Atom>> lastProgramAnswerSets;

    public SolverBase() {
        init();
    }

    private void init() {
        lastProgramAnswerSets = null;
    }

    protected void clear() {
        lastProgramAnswerSets = null;
    }

    /**
     *
     * @return part before list of files
     */
    protected abstract String solverCommandPrefix();

    protected String solverCommand(Program<Atom> program) {
        StringBuilder sb = new StringBuilder();
        sb.append(solverCommandPrefix());
        for (File file : program.getFiles()) {
            sb.append(" ").append(file.getAbsolutePath());
        }
        if (!program.getInput().isEmpty()) {
            sb.append(" ").append(tempInputFilename());
        }
        return sb.toString();
    }

    protected abstract String tempInputFilename();

    /**
     * @return list of filenames as used in command line
     */
    protected String generateInputFilenames(Program<Atom> program) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    protected void preSolver(Program<Atom> program) throws Exception {
        Collection<Atom> inputAtoms = program.getInput();
        if (inputAtoms.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Atom atom : inputAtoms) {
            sb.append(atom.toString()).append(" ");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempInputFilename()))) {
            writer.write(sb.toString());
        }
    }

    protected void postSolver(Program<Atom> program) throws Exception {
        if (program.getInput().isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("rm ").append(tempInputFilename());
        runVoidExec(sb.toString());
    }

    /**
     * @return list of strings, each representing an answer set. atoms
     * comma-separated. {optionally, surrounded, by, braces}
     */
    protected abstract List<String> getAnswerSetStrings(Process exec) throws IOException;

    /**
     *
     * @param answerSetString
     * @return string to tokenize based on standard configuration
     * @see anwerSetDelimiter
     */
    protected abstract String prepareAnswerSetString(String answerSetString);

    protected abstract String answerSetDelimiter();

    protected List<AnswerSet<Atom>> mapAnswerSetStrings(List<String> strings) {
        List<AnswerSet<Atom>> answerSets = new ArrayList();
        for (String answerSetString : strings) {
            answerSetString = prepareAnswerSetString(answerSetString);
            String[] atomStrings = answerSetString.split(answerSetDelimiter());
            Set<Atom> atoms = new HashSet();
            for (String atomString : atomStrings) {
                int idxParen = atomString.indexOf("(");
                if (idxParen == -1) {
                    atoms.add(new AtomImpl(atomString));
                } else {
                    String predicateName = atomString.substring(0, idxParen);
                    String[] args = atomString.substring(idxParen + 1, atomString.length() - 1).split(",");
                    atoms.add(new AtomImpl(predicateName, args));
                }
            }
            answerSets.add(new AnswerSetImpl(atoms));
        }
        return answerSets;
    }

    /**
     * run a command and throw an exception in case of errors
     *
     * @param cmd
     * @throws Exception
     */
    protected void runVoidExec(String cmd) throws Exception {
        if (cmd == null || cmd.isEmpty()) {
            return;
        }
        Process exec = Runtime.getRuntime().exec(cmd);
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
        String line;
        boolean hasError = false;
        StringBuilder sb = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            hasError = true;
            System.err.println(line);
            sb.append(line).append("\n");
        }
        exec.waitFor();
        int exitValue = exec.exitValue();
        if (exitValue != 0 || hasError) {
            throw new Exception(cmd + " exit: " + exitValue + "\n error msg: " + sb.toString());
        }
    }

    @Override
    public List<AnswerSet<Atom>> getAnswerSets(Program<Atom> program) throws Exception {
        if (lastProgramAnswerSets != null && program.hashCode() == lastProgramHashCode) {
            return lastProgramAnswerSets;
        }
        lastProgramHashCode = program.hashCode();
        preSolver(program);
        Process exec = Runtime.getRuntime().exec(solverCommand(program));
        List<String> answerSetStrings = getAnswerSetStrings(exec);
        postSolver(program);
        return lastProgramAnswerSets = Collections.unmodifiableList(mapAnswerSetStrings(answerSetStrings));
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
        Set<Atom> intersection = new HashSet();
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
        Set<Atom> set = new HashSet();
        for (AnswerSet<Atom> answerSet : answerSets) {
            set.addAll(answerSet.atoms());
        }
        return Collections.unmodifiableSet(set);
    }
}
