package asp4j.mapping.direct;

import asp4j.lang.Atom;
import asp4j.lang.HasSymbol;

/**
 * Objects that can be directly initialized from an Atom
 *
 * @author hbeck
 * date May 14, 2013
 */
public interface CanInitFromAtom extends HasSymbol {
    
    void init(Atom atom);
    
}
