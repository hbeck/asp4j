package asp4j.mapping.object.constant;

import asp4j.lang.Constant;
import asp4j.mapping.object.OutputMapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface ConstantOutputMapping<T> extends AnyConstantMapping<T>, OutputMapping<T,Constant> {
    
}
