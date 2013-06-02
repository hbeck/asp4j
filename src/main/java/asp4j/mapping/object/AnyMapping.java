package asp4j.mapping.object;

import asp4j.lang.LangElem;
import asp4j.solver.object.Binding;

/**
 *
 * @author hbeck May 30, 2013
 */
public interface AnyMapping<T, E extends LangElem> {

    Class<T> clazz();

    Binding getBinding();

    void setBinding(Binding binding);
}
