package asp4j.mapping.object.term;

import asp4j.lang.Term;
import asp4j.mapping.object.InputMapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface TermInputMapping<T> extends AnyTermMapping<T>, InputMapping<T,Term> {
    
}
