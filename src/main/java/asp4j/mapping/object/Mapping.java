package asp4j.mapping.object;

import asp4j.lang.LangElem;

/**
 * Defines how objects of class T are mappend to ASP language elements and vice versa
 *
 * @author hbeck Jun 1, 2013
 */
public interface Mapping<T,E extends LangElem> extends InputMapping<T,E>, OutputMapping<T,E> {

}
