package asp4j.solver;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.answerset.AnswerSet;
import asp4j.lang.answerset.AnswerSetImpl;
import asp4j.program.Program;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hbeck date April 14, 2013
 */
public class SolverDLV extends SolverBase {

    private String temporaryInputFile = System.getProperty("user.dir")+"/tmp_dlv_input.lp";

    @Override
    protected String solverCommand(Program<Atom> program) {
        StringBuilder sb = new StringBuilder();
        sb.append("dlv -silent");
        for (File file : program.getFiles()) {
            sb.append(" ").append(file.getAbsolutePath());
        }
        if (!program.getInput().isEmpty()) {
            sb.append(" ").append(temporaryInputFile);
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    @Override
    protected List<String> parseAnswerSetStrings(BufferedReader input) throws IOException {
        //use apache commons
        List<String> strings = new LinkedList();
        String line;
        while ((line = input.readLine()) != null) {
            if (line.isEmpty()) {
                continue;
            }
            strings.add(line.substring(1, line.length() - 1));
        }
        return strings;
    }

    @Override
    protected List<AnswerSet<Atom>> mapAnswerSetStrings(List<String> strings) {
        //TODO make List<AnswerSet> final, no add methods. use builder pattern
        List<AnswerSet<Atom>> answerSets = new LinkedList();
        for (String answerSetString : strings) {
            //System.out.println("answer set string: "+answerSetString);
            String[] atomStrings = answerSetString.split(", ");
            Collection<Atom> atoms = new LinkedList();
            for (String atomString : atomStrings) {
                //System.out.println("atom: "+atomString);
                int idxParen = atomString.indexOf("(");
                String predicateName = atomString.substring(0, idxParen);
                String[] args = atomString.substring(idxParen + 1, atomString.length() - 1).split(",");
                atoms.add(new AtomImpl(predicateName, args));
            }
            answerSets.add(new AnswerSetImpl(atoms));
        }
        return answerSets;
    }

    @Override
    protected void preSolver(Program<Atom> program) throws Exception {
        Collection<Atom> inputAtoms = program.getInput();
        if (inputAtoms.isEmpty()) {
            return;
        }                
        StringBuilder sb = new StringBuilder();
        for (Atom atom : inputAtoms) {
            sb.append(atom.toString()).append(" ");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryInputFile))) {
            writer.write(sb.toString());
        }        
    }

    @Override
    protected void postSolver(Program<Atom> program) throws Exception {
        if (program.getInput().isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("rm ").append(temporaryInputFile);
        runVoidExec(sb.toString());
    }
}
