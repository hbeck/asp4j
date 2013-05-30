package asp4j.mapping.direct;

import asp4j.lang.Constant;

/**
 * Objects that can directly specify their translation as Constant
 *
 * @author hbeck
 * date May 14, 2013
 */
public interface CanAsConstant extends CanAsTerm {
    
    Constant asConstant();
    
}
