package asp4j.mapping.object;

import asp4j.lang.HasSymbol;
import asp4j.lang.LangElem;

/**
 * Defines how ASP language elements are mapped to objects of class T
 *
 * @author hbeck Jun 1, 2013
 */
public interface OutputMapping<T,E extends LangElem> extends AnyMapping<T,E>, HasSymbol {
    
    T asObject(E e) throws Exception;

}
