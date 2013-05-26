package asp4j.mapping.direct;

import asp4j.lang.Atom;
import asp4j.lang.HasPredicateName;

/**
 *
 * @author hbeck
 * date May 14, 2013
 */
public interface CanInitFromAtom extends HasPredicateName {
    
    void init(Atom atom);
    
}
