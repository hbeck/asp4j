package asp4j.mapping;

import asp4j.lang.Atom;
import asp4j.lang.HasPredicateName;

/**
 *
 * @author hbeck
 * date 2013-05-14
 */
public interface OutputAtom extends HasPredicateName {
    
    void init(Atom atom);
    
}
