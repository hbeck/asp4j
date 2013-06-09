package asp4j.solver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author hbeck  Apr 14, 2013
 */
public class SolverDLV extends SolverBase {

    @Override
    protected String solverCommand() {
        return "dlv -silent";
    }

    @Override
    protected List<String> getAnswerSetStrings(Process exec) throws IOException {
        InputStream inputStream = exec.getInputStream();
        List<String> allLines = IOUtils.readLines(inputStream);
        if (allLines.isEmpty()) {
            throw new IOException("dlv output error: lines empty");
        }
        List<String> answerSetLines = new ArrayList<>();
        for (String line : allLines) {
            if (!line.startsWith("{")) {
                throw new IOException("dlv output error: not an answer set: "+line);
            }
            answerSetLines.add(line);
        }
        return answerSetLines;
    }

    /**
     * 
     * @param answerSetString "{atom_1,...,atom_n}"
     * @return "atom_1,...,atom_n"
     */
    @Override
    protected String prepareAnswerSetString(String answerSetString) {
        return answerSetString.substring(1, answerSetString.length() - 1);
    }

    @Override
    protected String atomDelimiter() {
        return ", ";
    }
}
