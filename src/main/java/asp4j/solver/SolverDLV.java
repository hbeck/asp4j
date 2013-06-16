package asp4j.solver;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author hbeck Apr 14, 2013
 */
public class SolverDLV extends SolverBase {

    private static final String EMPTY_LINES = "dlv output error: empty lines";
    private static final String BAD_ANSWERSET_SYNTAX = "dlv output error: not an answer set: ";
    private static final String MULTIPLE_LINES = "dlv output error: expected single line ouput, got: ";

    @Override
    protected String answerSetsSolverCommand() {
        return "dlv -silent";
    }

    @Override
    protected String querySolverCommand(ReasoningMode reasoningMode) {
        StringBuilder sb = new StringBuilder();
        sb.append(answerSetsSolverCommand());
        switch (reasoningMode) {
            case BRAVE:
                sb.append(" -brave");
                break;
            case CAUTIOUS:
                sb.append(" -cautious");
                break;
        }
        return sb.toString();
    }

    @Override
    protected List<String> getAnswerSetStrings(Process exec) throws IOException {
        InputStream inputStream = exec.getInputStream();
        List<String> allLines = IOUtils.readLines(inputStream);
        if (allLines.isEmpty()) {
            throw new IOException(EMPTY_LINES);
        }
        List<String> answerSetLines = new ArrayList<>();
        for (String line : allLines) {
            if (!line.startsWith("{")) {
                throw new IOException(BAD_ANSWERSET_SYNTAX + line);
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

    @Override
    protected boolean getBooleanQueryResult(Process exec) throws IOException, ParseException {
        InputStream inputStream = exec.getInputStream();
        List<String> allLines = IOUtils.readLines(inputStream);
        if (allLines.isEmpty()) {
            throw new IOException(EMPTY_LINES);
        }
        if (allLines.size() > 1) {
            throw new IOException(MULTIPLE_LINES + allLines.size());
        }
        //... is bravely true.
        //... is cautiously false.
        String line = allLines.get(0).trim();
        int idx = line.lastIndexOf(" ");
        return Boolean.parseBoolean(line.substring(idx+1,line.length()-1));
    }
}
