package asp4j.solver.object.query;

import java.util.List;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public interface ObjectTupleBinding {

    boolean isEmpty();

    int size();

    List<Object> asList();
}
