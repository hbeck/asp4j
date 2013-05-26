package asp4j.solver.object;

import asp4j.lang.Atom;
import java.util.Collection;

/**
 * Specification of output classes to filter for.
 *
 * @author hbeck
 * date May 26, 2013
 */
public interface FilterBinding<Out> {
    
    FilterBinding<Out> add(Class<? extends Out> clazz) throws Exception;

    FilterBinding<Out> remove(Class<? extends Out> clazz) throws Exception;

    Collection<String> getFilterPredicateNames();

    Out asObject(Atom atom) throws Exception;

}
