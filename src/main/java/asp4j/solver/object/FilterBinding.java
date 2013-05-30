package asp4j.solver.object;

import asp4j.lang.Atom;
import asp4j.lang.HasSymbol;
import asp4j.mapping.MappingUtils;
import asp4j.mapping.annotations.DefAtom;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * specifies target instantiation objects to filter ObjectSolver output for
 * 
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
        Class clazz = predicate2class.get(atom.symbol());
        return MappingUtils.asObject(clazz, atom);
    }

    private String getPredicateName(Class clazz) throws Exception {
        DefAtom predicateAnn = (DefAtom)clazz.getAnnotation(DefAtom.class);
        if (predicateAnn != null) {
            return predicateAnn.value();
        }        
        //assume direct
        Object inst = clazz.newInstance();        
        return ((HasSymbol) inst).symbol();
    }
}
