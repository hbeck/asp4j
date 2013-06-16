package asp4j.solver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author hbeck May 27, 2013
 */
public class SolverClingo extends SolverBase {

    @Override
    protected String answerSetsSolverCommand() {
        return "clingo 0 --verbose=0";
    }

    @Override
    protected String querySolverCommand(ReasoningMode reasoningMode) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    protected List<String> getAnswerSetStrings(Process exec) throws IOException {
        InputStream inputStream = exec.getInputStream();
        List<String> allLines = IOUtils.readLines(inputStream);
        List<String> answerSetLines = new ArrayList<>();
        for (String line : allLines) {
            if (line.startsWith("%") || line.startsWith("SATISFIABLE")) {
                continue;
            }
            answerSetLines.add(line);
        }
        return answerSetLines;
    }

    @Override
    protected String prepareAnswerSetString(String answerSetString) {
        return answerSetString;
    }

    @Override
    protected String atomDelimiter() {
        return " ";
    }

    @Override
    protected boolean getBooleanQueryResult(Process exec) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }
    
}
