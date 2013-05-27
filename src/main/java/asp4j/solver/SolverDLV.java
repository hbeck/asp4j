package asp4j.solver;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.program.Program;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author hbeck date April 14, 2013
 */
public class SolverDLV extends SolverBase {

    protected String tempInputFilename = System.getProperty("user.dir") + "/tmp_dlv_input.lp";

    @Override
    protected String solverCommandPrefix() {
        return "dlv -silent";
    }

    @Override
    protected List<String> getAnswerSetStrings(Process exec) throws IOException {
        InputStream inputStream = exec.getInputStream();
        return IOUtils.readLines(inputStream);
    }

    @Override
    protected String tempInputFilename() {
        return tempInputFilename;
    }

    @Override
    protected String prepareAnswerSetString(String answerSetString) {
        if (answerSetString.startsWith("{")) {
            return answerSetString.substring(1, answerSetString.length() - 1);
        }
        return answerSetString;
    }

    @Override
    protected String answerSetDelimiter() {
        return ", ";
    }
}
