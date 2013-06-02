package asp4j.mapping.object;

import asp4j.lang.LangElem;
import asp4j.solver.object.Binding;

/**
 *
 * @author hbeck Jun 1, 2013
 */
public abstract class AnyMappingBase<T,E extends LangElem> implements AnyMapping<T,E> {
    
    protected Binding binding;

    @Override
    public Binding getBinding() {
        return binding;
    }

    @Override
    public void setBinding(Binding binding) {
        this.binding=binding;
    }

}
