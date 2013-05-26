package asp4j.solver.object;

import asp4j.annotations.Atomname;
import asp4j.annotations.ReflectionUtils;
import asp4j.lang.Atom;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Collection;

/**
 *
 * @author hbeck
 * date May 23, 2013
 */
public class ObjectBinding {

    BiMap<String, Class<?>> predicate2class;

    public ObjectBinding() {
        this.predicate2class = HashBiMap.create();
    }

    public ObjectBinding add(Class<?> binding) throws Exception {
        String predicateName = binding.getAnnotation(Atomname.class).value();
        predicate2class.put(predicateName, binding);
        return this;
    }

    public ObjectBinding remove(Class<?> binding) throws Exception {
        String predicateName = binding.getAnnotation(Atomname.class).value();
        predicate2class.remove(predicateName);
        return this;
    }

    public Collection<Class<?>> getBindings() {
        return predicate2class.values();
    }

    public Collection<String> getBoundPredicateNames() {
        return predicate2class.keySet();
    }

    public Object asObject(final Atom atom) throws Exception {
        Class<?> clazz = predicate2class.get(atom.predicateName());
        return ReflectionUtils.asObject(clazz,atom);
    }
}
