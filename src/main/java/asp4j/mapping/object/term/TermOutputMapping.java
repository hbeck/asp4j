package asp4j.mapping.object.term;

import asp4j.lang.Term;
import asp4j.mapping.object.OutputMapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface TermOutputMapping<T> extends AnyTermMapping<T>, OutputMapping<T,Term> {
    
}
