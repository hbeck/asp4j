package asp4j.mapping.object;

import asp4j.lang.LangElem;

/**
 * Defines how objects of class T are mappend to ASP language elements
 *
 * @author hbeck Jun 1, 2013
 */
public interface InputMapping<T,E extends LangElem> extends AnyMapping<T,E> {    
    
    E asLangElem(T t) throws Exception;

}
