package asp4j.mapping.direct;

import asp4j.lang.HasSymbol;
import asp4j.lang.Term;

/**
 * Objects that can be directly initialized from a Term
 *
 * @author hbeck
 * date May 30, 2013
 */
public interface CanInitFromTerm extends HasSymbol {

    void init(Term term);
    
}
