package asp4j.solver.object;

import asp4j.lang.Atom;
import java.util.Collection;

/**
 * Specification of output classes to filter for.
 *
 * @author hbeck
 * date May 26, 2013
 */
public interface FilterBinding {
    
    FilterBinding add(Class clazz) throws Exception;

    FilterBinding remove(Class clazz) throws Exception;

    Collection<String> getFilterPredicateNames();

    Object asObject(Atom atom) throws Exception;

}
