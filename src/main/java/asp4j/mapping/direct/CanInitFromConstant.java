package asp4j.mapping.direct;

import asp4j.lang.Constant;
import asp4j.lang.HasSymbol;

/**
 * Objects that can be directly initialized from a Constant
 *
 * @author hbeck
 * date May 30, 2013
 */
public interface CanInitFromConstant extends HasSymbol {
    
    void init(Constant constant);
    
}
