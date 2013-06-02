package asp4j.mapping.object.constant;

import asp4j.lang.Constant;
import asp4j.mapping.object.InputMapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface ConstantInputMapping<T> extends AnyConstantMapping<T>, InputMapping<T,Constant> {
    
}
