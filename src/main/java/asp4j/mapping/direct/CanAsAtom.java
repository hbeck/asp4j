package asp4j.mapping.direct;

import asp4j.lang.Atom;
import asp4j.lang.HasSymbol;

/**
 * Objects that can directly specify their translation as Atom
 *
 * @author hbeck
 * date May 14, 2013
 */
public interface CanAsAtom extends HasSymbol {
    
    Atom asAtom();
    
}
