package asp4j.mapping.object.atom;

import asp4j.lang.Atom;
import asp4j.mapping.object.Mapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface AtomMapping<T> extends AtomInputMapping<T>, AtomOutputMapping<T>, Mapping<T,Atom> {
    
}
