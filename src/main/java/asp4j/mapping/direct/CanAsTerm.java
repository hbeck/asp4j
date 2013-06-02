package asp4j.mapping.direct;

import asp4j.lang.HasSymbol;
import asp4j.lang.Term;

/**
 * Objects that can directly specify their translation as Term
 *
 * @author hbeck May 30, 2013
 */
public interface CanAsTerm extends HasSymbol {
    
    Term asTerm();
    
}
