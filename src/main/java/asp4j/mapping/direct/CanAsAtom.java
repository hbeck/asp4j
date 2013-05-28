package asp4j.mapping.direct;

import asp4j.lang.Atom;

/**
 * Objects that can directly specify their translation to Atom
 *
 * @author hbeck
 * date May 14, 2013
 */
public interface CanAsAtom {
    
    Atom asAtom();
    
}
