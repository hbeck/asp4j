package asp4j.mapping.object;

import asp4j.lang.LangElem;
import asp4j.mapping.MappingException;

/**
 * Defines how ASP language elements are mapped to objects of class T
 *
 * @author hbeck Jun 1, 2013
 */
public interface OutputMapping<T,E extends LangElem> extends AnyMapping<T,E> {
    
    T asObject(E e) throws MappingException;

}
