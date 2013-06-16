package asp4j.solver.object.query;

import asp4j.solver.query.TupleQueryResult;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public interface ObjectTupleQueryResult {
    
    int size();
    boolean isEmpty();
    
    TupleQueryResult getTupleQueryResult();
    
    List<ObjectTupleBinding> asList();
    Iterator<ObjectTupleBinding> iterator();

}
