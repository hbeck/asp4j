package asp4j.mapping.object.atom;

import asp4j.lang.Atom;
import asp4j.mapping.object.InputMapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface AtomInputMapping<T> extends AnyAtomMapping<T>, InputMapping<T,Atom> {
    
}
