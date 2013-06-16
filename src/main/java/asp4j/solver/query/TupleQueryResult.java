package asp4j.solver.query;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public interface TupleQueryResult {

    boolean isEmpty();

    int size();

    List<TupleBinding> asList();

    Set<String> getVariables();

    Iterator<TupleBinding> iterator();
}
