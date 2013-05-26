package asp4j.solver.object;

import asp4j.lang.Atom;
import asp4j.lang.HasPredicateName;
import asp4j.mapping.MappingUtils;
import asp4j.mapping.annotations.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author hbeck date May 23, 2013
 */
public class FilterBindingImpl implements FilterBinding {

    BiMap<String, Class> predicate2class;

    public FilterBindingImpl() {
        this.predicate2class = HashBiMap.create();
    }

    @Override
    public FilterBindingImpl add(Class clazz) throws Exception {
        predicate2class.put(getPredicateName(clazz), clazz);
        return this;
    }

    @Override
    public FilterBindingImpl remove(Class clazz) throws Exception {        
        predicate2class.remove(getPredicateName(clazz));
        return this;
    }

    @Override
    public Collection<String> getFilterPredicateNames() {
        return predicate2class.keySet();
    }

    @Override
    public Object asObject(final Atom atom) throws Exception {
        Class clazz = predicate2class.get(atom.predicateName());
        return MappingUtils.asObject(clazz, atom);
    }

    private String getPredicateName(Class clazz) throws Exception {
        Annotation annotation = clazz.getAnnotation(Predicate.class);
        if (annotation != null) {
            return ((Predicate)annotation).value();
        }
        //assume direct
        Object inst = clazz.newInstance();
        return ((HasPredicateName) inst).predicateName();
    }
}
