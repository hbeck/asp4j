package asp4j.solver;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.answerset.AnswerSet;
import asp4j.lang.answerset.AnswerSetImpl;
import asp4j.program.Program;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author hbeck
 * date April 14, 2013
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
        return sb.toString();
    }
    
    @Override
    protected List<String> getAnswerSetStrings(Process exec) throws IOException {
        InputStream inputStream = exec.getInputStream();
        return IOUtils.readLines(inputStream);
    }
    
    @Override
    protected List<AnswerSet<Atom>> mapAnswerSetStrings(List<String> strings) {        
        List<AnswerSet<Atom>> answerSets = new ArrayList();
        for (String answerSetString : strings) {
            if (answerSetString.startsWith("{")) {
                answerSetString=answerSetString.substring(1,answerSetString.length()-1);
            }
            String[] atomStrings = answerSetString.split(", ");
            Collection<Atom> atoms = new ArrayList();
            for (String atomString : atomStrings) {
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
