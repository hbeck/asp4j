package asp4j.solver;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.mapping.ParseUtils;
import asp4j.program.Program;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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

    protected File inputFile;
    protected int lastProgramHashCode;
    protected List<AnswerSet<Atom>> lastProgramAnswerSets;

    public SolverBase() {
        inputFile = null;
        lastProgramAnswerSets = null;
    }

    /**
     * command to start the solver, including params (excluding input programs)
     *
     * @return part before list of files
     */
    protected abstract String solverCommand();

    /**
     * @return list of strings, each representing an answer set
     */
    protected abstract List<String> getAnswerSetStrings(Process exec) throws IOException;

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

    @Override
    public List<AnswerSet<Atom>> getAnswerSets(Program<Atom> program) throws Exception {
        if (lastProgramAnswerSets != null && program.hashCode() == lastProgramHashCode) {
            return lastProgramAnswerSets;
        }
        lastProgramHashCode = program.hashCode();
        preSolverExec(program);
        Process exec = Runtime.getRuntime().exec(solverCallString(program));
        List<String> answerSetStrings = getAnswerSetStrings(exec);
        postSolverExec(program);
        return lastProgramAnswerSets = Collections.unmodifiableList(mapAnswerSetStrings(answerSetStrings));
    }

    /**
     * maps a list of answer sets, represented as strings, to a list of (low
     * level) AnswerSet objects
     */
    protected List<AnswerSet<Atom>> mapAnswerSetStrings(List<String> answerSetStrings) throws ParseException {
        List<AnswerSet<Atom>> answerSets = new ArrayList();
        for (String answerSetString : answerSetStrings) {
            answerSetString = prepareAnswerSetString(answerSetString);
            String[] atomStrings = answerSetString.split(atomDelimiter());
            Set<Atom> atoms = new HashSet();
            for (String atomString : atomStrings) {
                atoms.add(ParseUtils.parseAtom(atomString));
            }
            answerSets.add(new AnswerSetImpl(atoms));
        }
        return answerSets;
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

    /**
     *
     * @param program
     * @return full call to solver, i.e., solver command plus programs
     * @throws IOException
     */
    protected String solverCallString(Program<Atom> program) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(solverCommand());
        for (File file : program.getFiles()) {
            sb.append(" ").append(file.getAbsolutePath());
        }
        if (!program.getInput().isEmpty()) {
            sb.append(" ").append(inputFile.getAbsolutePath());
        }
        return sb.toString();
    }

    protected File tempInputFile() throws IOException {
        if (inputFile == null) {
            inputFile = File.createTempFile("tmp-program", ".lp");
            inputFile.deleteOnExit();
        }
        return inputFile;
    }

    /**
     * executed before call to solver
     */
    protected void preSolverExec(Program<Atom> program) throws Exception {
        Collection<Atom> inputAtoms = program.getInput();
        if (inputAtoms.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Atom atom : inputAtoms) {
            sb.append(atom.toString()).append(" ");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempInputFile()))) {
            writer.write(sb.toString());
        }
    }

    /**
     * executed after call to solver
     */
    protected void postSolverExec(Program<Atom> program) throws Exception {
    }
}
