package asp4j.solver.object;

import asp4j.lang.Atom;
import asp4j.lang.HasPredicateName;
import asp4j.mapping.MappingUtils;
import asp4j.mapping.annotations.Predicate;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hbeck date May 23, 2013
 */
public class FilterBinding {

    Map<String, Class> predicate2class;

    public FilterBinding() {
        this.predicate2class = new HashMap();
    }

    public FilterBinding add(Class clazz) throws Exception {
        predicate2class.put(getPredicateName(clazz), clazz);
        return this;
    }

    public FilterBinding remove(Class clazz) throws Exception {        
        predicate2class.remove(getPredicateName(clazz));
        return this;
    }

    public Collection<String> getFilterPredicateNames() {
        return predicate2class.keySet();
    }

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
