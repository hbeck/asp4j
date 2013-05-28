package asp4j.solver;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author hbeck date April 14, 2013
 */
public class SolverDLV extends SolverBase {    

    @Override
    protected String solverCommand() {
        return "dlv -silent";
    }

    @Override
    protected List<String> getAnswerSetStrings(Process exec) throws IOException {
        InputStream inputStream = exec.getInputStream();
        return IOUtils.readLines(inputStream);
    }

    @Override
    protected String prepareAnswerSetString(String answerSetString) {
        if (answerSetString.startsWith("{")) {
            return answerSetString.substring(1, answerSetString.length() - 1);
        }
        return answerSetString;
    }

    @Override
    protected String atomDelimiter() {
        return ", ";
    }
}
