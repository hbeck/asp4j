package asp4j.mapping.object.atom;

import asp4j.lang.Atom;
import asp4j.mapping.object.OutputMapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface AtomOutputMapping<T> extends AnyAtomMapping<T>, OutputMapping<T,Atom> {
    
}
