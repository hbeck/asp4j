package asp4j.solver.object;

import asp4j.lang.Atom;
import asp4j.lang.HasPredicateName;
import asp4j.mapping.MappingUtils;
import asp4j.mapping.annotations.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Collection;

/**
 * @author hbeck date May 23, 2013
 */
public class FilterBindingImpl<T> implements FilterBinding<T> {

    BiMap<String, Class<? extends T>> predicate2class;

    public FilterBindingImpl() {
        this.predicate2class = HashBiMap.create();
    }

    @Override
    public FilterBindingImpl<T> add(Class<? extends T> clazz) throws Exception {
        predicate2class.put(getPredicateName(clazz), clazz);
        return this;
    }

    @Override
    public FilterBindingImpl<T> remove(Class<? extends T> clazz) throws Exception {        
        predicate2class.remove(getPredicateName(clazz));
        return this;
    }

    @Override
    public Collection<String> getFilterPredicateNames() {
        return predicate2class.keySet();
    }

    @Override
    public T asObject(final Atom atom) throws Exception {
        Class<? extends T> clazz = predicate2class.get(atom.predicateName());
        return MappingUtils.asObject(clazz, atom);
    }

    private String getPredicateName(Class<? extends T> clazz) throws Exception {
        Predicate annotation = clazz.getAnnotation(Predicate.class);
        if (annotation != null) {
            return annotation.value();
        }
        //assume direct
        T tmp = clazz.newInstance();
        return ((HasPredicateName) tmp).predicateName();
    }
}
