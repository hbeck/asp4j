package asp4j.solver.object;

import asp4j.lang.Atom;
import asp4j.mapping.OutputAtom;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Collection;

/**
 *
 * @author hbeck date May 19, 2013
 */
public class OutputAtomBinding {

    BiMap<String, Class<? extends OutputAtom>> predicate2class;

    public OutputAtomBinding() {
        this.predicate2class = HashBiMap.create();
    }

    public OutputAtomBinding add(Class<? extends OutputAtom> binding) throws Exception {
        String predicateName = binding.newInstance().predicateName();
        predicate2class.put(predicateName, binding);
        return this;
    }

    public OutputAtomBinding remove(Class<? extends OutputAtom> binding) throws Exception {
        String predicateName = binding.newInstance().predicateName();
        predicate2class.remove(predicateName);
        return this;
    }

    public Collection<Class<? extends OutputAtom>> getBindings() {
        return predicate2class.values();
    }

    public Collection<String> getBoundPredicateNames() {
        return predicate2class.keySet();
    }

    public OutputAtom asObject(Atom atom) throws Exception {
        OutputAtom inst = predicate2class.get(atom.predicateName()).newInstance();        
        inst.init(atom);
        return inst;
    }
}
