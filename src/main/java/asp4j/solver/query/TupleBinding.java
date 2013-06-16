package asp4j.solver.query;

import asp4j.lang.Term;
import java.util.Map;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public interface TupleBinding {

    boolean isEmpty();

    int size();

    Map<String, Term> asMap();

    Term getBinding(String variable);
}
