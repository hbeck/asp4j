package asp4j.mapping;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.ConstantImpl;
import asp4j.lang.Term;
import asp4j.lang.TermImpl;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hbeck date May 30, 2013
 */
public abstract class ParseUtils {

    /**
     *
     * @param termString potentially nested term, e.g., t1(t2(c2,c3),c4)
     * @return
     * @throws ParseException
     */
    public static Term parseTerm(String termString) throws ParseException {
        int parenIdx = termString.indexOf("(");
        if (parenIdx == -1) {
            return new ConstantImpl(termString);
        }
        String functionSymbol = termString.substring(0, parenIdx);
        String inner = termString.substring(parenIdx + 1, termString.length() - 1);
        List<String> termTokens = tokenizeTerms(inner);
        Term[] terms = new Term[termTokens.size()];
        for (int i = 0; i < terms.length; i++) {
            terms[i] = parseTerm(termTokens.get(i));
        }
        return new TermImpl(functionSymbol, terms);
    }

    //ps(c1,t1(t2(c2,c3),c4))
    public static Atom parseAtom(String atomString) throws ParseException {
        String s = atomString.trim();
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        int parenIdx = s.indexOf("(");
        if (parenIdx == -1) {
            return new AtomImpl(s);
        }
        String predicateSymbol = s.substring(0, parenIdx);
        String inner = s.substring(parenIdx + 1, s.length() - 1);
        List<String> termTokens = tokenizeTerms(inner);
        Term[] terms = new Term[termTokens.size()];
        for (int i = 0; i < terms.length; i++) {
            terms[i] = parseTerm(termTokens.get(i));
        }
        return new AtomImpl(predicateSymbol, terms);
    }

    /**
     *
     * @param innerStr "t1,...tn", if 'surrounding' atom or term is of form
     * s(t1,...,tn)
     * @return String array {"t1",...,"tn"}
     */
    protected static List<String> tokenizeTerms(String innerStr) throws ParseException {
        char[] chars = innerStr.toCharArray();
        List<String> tokens = new ArrayList<>();
        parseStep(chars, 0, tokens, new StringBuilder(), 0);
        return tokens;
    }

    protected static void parseStep(char[] chars, int charIdx, List<String> terms, StringBuilder token, int parenDepth) throws ParseException {
        if (charIdx == chars.length) {
            if (token.length() > 0) {
                terms.add(token.toString());
            }
            return;
        }
        char c = chars[charIdx];
        if (c == '(') {
            parseStep(chars, charIdx + 1, terms, token.append(c), parenDepth + 1);
        } else if (c == ')') {
            if (parenDepth < 1) {
                throw new ParseException("too many closing parentheses", charIdx);
            } else if (parenDepth == 1) {
                terms.add(token.append(c).toString());
                parseStep(chars, charIdx + 1, terms, new StringBuilder(), 0);
            } else {
                parseStep(chars, charIdx + 1, terms, token.append(c), parenDepth - 1);
            }
        } else if (c == ',') {
            if (parenDepth == 0) {
                if (token.length() > 0) {
                    terms.add(token.toString());
                }
                parseStep(chars, charIdx + 1, terms, new StringBuilder(), parenDepth);
            } else {
                parseStep(chars, charIdx + 1, terms, token.append(c), parenDepth);
            }
        } else {
            parseStep(chars, charIdx + 1, terms, token.append(c), parenDepth);
        }
    }
}
