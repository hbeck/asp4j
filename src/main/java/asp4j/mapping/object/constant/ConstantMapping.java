package asp4j.mapping.object.constant;

import asp4j.lang.Constant;
import asp4j.mapping.object.Mapping;

/**
 *
 * @author hbeck May 31, 2013
 */
public interface ConstantMapping<T> extends ConstantInputMapping<T>, ConstantOutputMapping<T>, Mapping<T,Constant> {

}
