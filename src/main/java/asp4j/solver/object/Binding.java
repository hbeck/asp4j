package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.lang.Constant;
import asp4j.lang.Term;
import asp4j.mapping.direct.CanAsAtom;
import asp4j.mapping.direct.CanAsConstant;
import asp4j.mapping.direct.CanAsTerm;
import asp4j.mapping.direct.CanInitFromAtom;
import asp4j.mapping.direct.CanInitFromConstant;
import asp4j.mapping.direct.CanInitFromTerm;
import asp4j.mapping.object.InputMapping;
import asp4j.mapping.object.OutputMapping;
import asp4j.mapping.object.atom.AnyAtomMapping;
import asp4j.mapping.object.atom.AtomInputMapping;
import asp4j.mapping.object.atom.AtomInputMappingBase;
import asp4j.mapping.object.atom.AtomOutputMapping;
import asp4j.mapping.object.atom.AtomOutputMappingBase;
import asp4j.mapping.object.constant.AnyConstantMapping;
import asp4j.mapping.object.constant.ConstantInputMapping;
import asp4j.mapping.object.constant.ConstantInputMappingBase;
import asp4j.mapping.object.constant.ConstantOutputMapping;
import asp4j.mapping.object.constant.ConstantOutputMappingBase;
import asp4j.mapping.object.term.AnyTermMapping;
import asp4j.mapping.object.term.TermInputMapping;
import asp4j.mapping.object.term.TermInputMappingBase;
import asp4j.mapping.object.term.TermOutputMapping;
import asp4j.mapping.object.term.TermOutputMappingBase;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hbeck May 23, 2013
 */
public class Binding {

    private Registry registry;

    public Binding() {
        this.registry = new Registry(this);

    }

    @Deprecated
    public Binding addFilter(Class<?> cls) {
        return this;
    }

    public <T> Binding add(final T t) throws Exception {
        return add((Class<T>) t.getClass(), t);
    }

    public <T> Binding add(final Class<T> clazz) throws Exception {
        return add(clazz, clazz.newInstance());
    }

    private <T> Binding add(final Class<T> clazz, T inst) throws Exception {

        if (registry.isRegistered(clazz)) {
            return this;
        }

        if (inst instanceof CanAsAtom) {
            AtomInputMapping<?> mapping = createCanAsAtomMapping((Class<CanAsAtom>) clazz, (CanAsAtom) inst);
            registry.addAtomInputMapping(mapping);
        } else if (inst instanceof CanAsTerm) {
            TermInputMapping<?> mapping = createCanAsTermMapping((Class<CanAsTerm>) clazz, (CanAsTerm) inst);
            registry.addTermInputMapping(mapping);
        } else if (inst instanceof CanAsConstant) {
            ConstantInputMapping<?> mapping = createCanAsConstantMapping((Class<CanAsConstant>) clazz);
            registry.addConstantInputMapping(mapping);
        }
        //TODO annotations

        if (inst instanceof CanInitFromAtom) {
            AtomOutputMapping<?> mapping = createCanInitFromAtomMapping((Class<CanInitFromAtom>) clazz, (CanInitFromAtom) inst);
            registry.addAtomOutputMapping(mapping);
        } else if (inst instanceof CanInitFromTerm) {
            TermOutputMapping<?> mapping = createCanInitFromTermMapping((Class<CanInitFromTerm>) clazz, (CanInitFromTerm) inst);
            registry.addTermOutputMapping(mapping);
        } else if (inst instanceof CanInitFromConstant) {
            ConstantOutputMapping<?> mapping = createCanInitFromConstantMapping((Class<CanInitFromConstant>) clazz);
            registry.addConstantOutputMapping(mapping);
        }
        //TODO annotations

        return this;
    }

    private <T> void addOutputClass(final Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromAtom) {
            AtomOutputMapping<?> mapping = createCanInitFromAtomMapping((Class<CanInitFromAtom>) clazz, (CanInitFromAtom) inst);
            registry.addAtomOutputMapping(mapping);
            return;
        }
        //TODO annotations
        throw new Exception("cannot create atom output mapping for " + clazz);
    }

    private <T> AtomOutputMapping<T> createAtomOutputMapping(Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromAtom) {
            AtomOutputMapping<?> mapping = createCanInitFromAtomMapping((Class<CanInitFromAtom>) clazz, (CanInitFromAtom) inst);
            registry.addAtomOutputMapping(mapping);
            return (AtomOutputMapping<T>) mapping;
        }
        //TODO annotations
        throw new Exception("cannot create atom output mapping for" + clazz);
    }

    private <T> TermOutputMapping<T> createTermOutputMapping(Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromTerm) {
            TermOutputMapping<?> mapping = createCanInitFromTermMapping((Class<CanInitFromTerm>) clazz, (CanInitFromTerm) inst);
            registry.addTermOutputMapping(mapping);
            return (TermOutputMapping<T>) mapping;
        }
        //TODO annotations
        throw new Exception("cannot create term output mapping for " + clazz);
    }

    private <T> ConstantOutputMapping<T> createConstantOutputMapping(Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromConstant) {
            ConstantOutputMapping<?> mapping = createCanInitFromConstantMapping((Class<CanInitFromConstant>) clazz);
            registry.addConstantOutputMapping(mapping);
            return (ConstantOutputMapping<T>) mapping;
        }
        //TODO annotations
        throw new Exception("cannot create constant output mapping for " + clazz);
    }

    public <T> Binding add(AnyAtomMapping<T> mapping) throws Exception {
        if (mapping instanceof InputMapping) {
            registry.addAtomInputMapping((AtomInputMapping<T>) mapping);
        }
        if (mapping instanceof OutputMapping) {
            registry.addAtomOutputMapping((AtomOutputMapping<T>) mapping);
        }
        return this;
    }

    public <T> Binding add(AnyTermMapping<T> mapping) throws Exception {
        if (mapping instanceof InputMapping) {
            registry.addTermInputMapping((TermInputMapping<T>) mapping);
        }
        if (mapping instanceof OutputMapping) {
            registry.addTermOutputMapping((TermOutputMapping<T>) mapping);
        }
        return this;
    }

    public <T> Binding add(AnyConstantMapping<T> mapping) throws Exception {
        if (mapping instanceof InputMapping) {
            registry.addConstantInputMapping((ConstantInputMapping<T>) mapping);
        }
        if (mapping instanceof OutputMapping) {
            registry.addConstantOutputMapping((ConstantOutputMapping<T>) mapping);
        }
        return this;
    }

    public <T> Atom asAtom(T t) throws Exception {
        AtomInputMapping<T> mapping = (AtomInputMapping<T>) registry.getAtomInputMapping(t.getClass());
        return mapping.asLangElem(t);
    }

    public <T> Term asTerm(T t) throws Exception {
        TermInputMapping<T> mapping = (TermInputMapping<T>) registry.getTermInputMapping(t.getClass());
        return mapping.asLangElem(t);
    }

    public <T> Constant asConstant(T t) throws Exception {
        ConstantInputMapping<T> mapping = (ConstantInputMapping<T>) registry.getConstantInputMapping(t.getClass());
        return mapping.asLangElem(t);
    }

    public <T> T atomAsObject(Atom atom, Class<T> clazz) throws Exception {
        AtomOutputMapping<T> mapping = (AtomOutputMapping<T>) registry.getAtomOutputMapping(clazz);
        return (T) mapping.asObject(atom);
    }

    public <T> T termAsObject(Term term, Class<T> clazz) throws Exception {
        TermOutputMapping<T> mapping = (TermOutputMapping<T>) registry.getTermOutputMapping(clazz);
        return (T) mapping.asObject(term);
    }

    public <T> T constantAsObject(Constant constant, Class<T> clazz) throws Exception {
        ConstantOutputMapping<T> mapping = (ConstantOutputMapping<T>) registry.getConstantOutputMapping(clazz);
        return (T) mapping.asObject(constant);
    }

    protected AnswerSet<Object> map(AnswerSet<Atom> answerSet) throws Exception {
        Set<Object> objects = map(answerSet.atoms());
        return new AnswerSetImpl(objects);
    }

    protected Set<Object> map(Set<Atom> atoms) throws Exception {
        Filter filter = new Filter(registry.getAtomOutputClasses());
        return filterAndMap(atoms, filter);
    }

    /**
     * filters the atoms in the given low level answer set based on the filters
     * registered in this binding and returns according objects
     *
     * @param answerSet
     * @return answer set based on mapping filtered atoms
     */
    protected AnswerSet<Object> filterAndMap(AnswerSet<Atom> answerSet, Filter filter) throws Exception {
        if (filter == null) {
            throw new NullPointerException();
        }
        Set<Object> objects = filterAndMap(answerSet.atoms(), filter);
        return new AnswerSetImpl(objects);
    }

    /**
     * filters low level set of atoms based on the filters registered in this
     * binding and returns according objects
     *
     * @param atoms
     * @return unmodifiable set of filtered atoms, mapped to according objects
     */
    protected Set<Object> filterAndMap(Set<Atom> atoms, Filter filter) throws Exception {
        if (filter == null) {
            throw new NullPointerException();
        }
        for (Class<?> clazz : filter.getClasses()) {
            if (!registry.isRegisteredOutput(clazz)) {
                addOutputClass(clazz);
            }
        }
        Set<Object> set = new HashSet<>();
        for (Atom atom : atoms) {
            Class clazz = registry.getAtomClass(atom.symbol()); //TODO
            if (filter.accepts(clazz)) {
                set.add(atomAsObject(atom, clazz));
            }
        }
        return Collections.unmodifiableSet(set);
    }

    private <T extends CanAsAtom> AtomInputMapping<T> createCanAsAtomMapping(final Class<T> clazz, final CanAsAtom template) throws Exception {
        return new AtomInputMappingBase<T>() {
            private final String symbol = template.symbol();

            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public String symbol() {
                return symbol;
            }

            @Override
            public Atom asLangElem(T t) throws Exception {
                return ((CanAsAtom) t).asAtom();
            }
        };
    }

    private <T extends CanAsTerm> TermInputMapping<T> createCanAsTermMapping(final Class<T> clazz, final CanAsTerm template) throws Exception {
        return new TermInputMappingBase<T>() {
            private final String symbol = template.symbol();

            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public String symbol() {
                return symbol;
            }

            @Override
            public Term asLangElem(T t) throws Exception {
                return ((CanAsTerm) t).asTerm();
            }
        };
    }

    private <T extends CanAsConstant> ConstantInputMapping<T> createCanAsConstantMapping(final Class<T> clazz) throws Exception {
        return new ConstantInputMappingBase<T>() {
            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public Constant asLangElem(T t) throws Exception {
                return ((CanAsConstant) t).asConstant();
            }
        };
    }

    private <T extends CanInitFromAtom> AtomOutputMapping<T> createCanInitFromAtomMapping(final Class<T> clazz, final CanInitFromAtom template) throws Exception {
        return new AtomOutputMappingBase<T>() {
            private final String symbol = template.symbol();

            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public String symbol() {
                return symbol;
            }

            @Override
            public T asObject(Atom atom) throws Exception {
                T inst = clazz().newInstance();
                inst.init(atom);
                return inst;
            }
        };
    }

    private <T extends CanInitFromTerm> TermOutputMapping<T> createCanInitFromTermMapping(final Class<T> clazz, final CanInitFromTerm template) throws Exception {
        return new TermOutputMappingBase<T>() {
            private final String symbol = template.symbol();

            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public String symbol() {
                return symbol;
            }

            @Override
            public T asObject(Term term) throws Exception {
                T inst = clazz().newInstance();
                inst.init(term);
                return inst;
            }
        };
    }

    private <T extends CanInitFromConstant> ConstantOutputMapping<T> createCanInitFromConstantMapping(final Class<T> clazz) throws Exception {
        return new ConstantOutputMappingBase<T>() {
            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public T asObject(Constant constant) throws Exception {
                T inst = clazz().newInstance();
                inst.init(constant);
                return inst;
            }
        };
    }

    private class Registry {

        private final Binding binding;

        Registry(Binding binding) {
            this.binding = binding;
        }
        private Map<String, Class<?>> predicateSymbol2class = new HashMap<>();
        private Map<String, Class<?>> functionSymbol2class = new HashMap<>();
        //
        private Map<Class<?>, AtomInputMapping<?>> atomInputMappings = new HashMap<>();
        private Map<Class<?>, TermInputMapping<?>> termInputMappings = new HashMap<>();
        private Map<Class<?>, ConstantInputMapping<?>> constantInputMappings = new HashMap<>();
        //
        private Map<Class<?>, AtomOutputMapping<?>> atomOutputMappings = new HashMap<>();
        private Map<Class<?>, TermOutputMapping<?>> termOutputMappings = new HashMap<>();
        private Map<Class<?>, ConstantOutputMapping<?>> constantOutputMappings = new HashMap<>();

        private boolean isRegistered(Class<?> clazz) {
            return isRegisteredInput(clazz) || isRegisteredOutput(clazz);
        }

        private boolean isRegisteredInput(Class<?> clazz) {
            return atomInputMappings.containsKey(clazz)
                    || termInputMappings.containsKey(clazz)
                    || constantInputMappings.containsKey(clazz);
        }

        private boolean isRegisteredOutput(Class<?> clazz) {
            return atomOutputMappings.containsKey(clazz)
                    || termOutputMappings.containsKey(clazz)
                    || constantOutputMappings.containsKey(clazz);
        }

        private void addAtomInputMapping(AtomInputMapping<?> mapping) {
            atomInputMappings.put(mapping.clazz(), mapping);
            predicateSymbol2class.put(mapping.symbol(), mapping.clazz());
            mapping.setBinding(binding);
        }

        private void addAtomOutputMapping(AtomOutputMapping<?> mapping) {
            atomOutputMappings.put(mapping.clazz(), mapping);
            predicateSymbol2class.put(mapping.symbol(), mapping.clazz());
            mapping.setBinding(binding);
        }

        private void addTermInputMapping(TermInputMapping<?> mapping) {
            termInputMappings.put(mapping.clazz(), mapping);
            functionSymbol2class.put(mapping.symbol(), mapping.clazz());
            mapping.setBinding(binding);
        }

        private void addTermOutputMapping(TermOutputMapping<?> mapping) {
            termOutputMappings.put(mapping.clazz(), mapping);
            functionSymbol2class.put(mapping.symbol(), mapping.clazz());
            mapping.setBinding(binding);
        }

        private void addConstantInputMapping(ConstantInputMapping<?> mapping) {
            constantInputMappings.put(mapping.clazz(), mapping);
            mapping.setBinding(binding);
        }

        private void addConstantOutputMapping(ConstantOutputMapping<?> mapping) {
            constantOutputMappings.put(mapping.clazz(), mapping);
            mapping.setBinding(binding);
        }

        private <T> AtomInputMapping<T> getAtomInputMapping(Class<T> clazz) throws Exception {
            AtomInputMapping<?> mapping = (AtomInputMapping<?>) atomInputMappings.get(clazz);
            if (mapping != null) {
                return (AtomInputMapping<T>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = (AtomInputMapping<?>) atomInputMappings.get(candidateClass);
                if (mapping != null) {
                    return (AtomInputMapping<T>) mapping;
                }
            }
            throw new Exception("no atom input mapping found for class " + clazz + ". could not map instance. ");
        }

        private <T> TermInputMapping<T> getTermInputMapping(Class<T> clazz) throws Exception {
            TermInputMapping<?> mapping = (TermInputMapping<?>) termInputMappings.get(clazz);
            if (mapping != null) {
                return (TermInputMapping<T>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = (TermInputMapping<?>) termInputMappings.get(candidateClass);
                if (mapping != null) {
                    return (TermInputMapping<T>) mapping;
                }
            }
            throw new Exception("no term input mapping found for class " + clazz + ". could not map instance.");
        }

        private <T> ConstantInputMapping<T> getConstantInputMapping(Class<T> clazz) throws Exception {
            ConstantInputMapping<?> mapping = (ConstantInputMapping<?>) constantInputMappings.get(clazz);
            if (mapping != null) {
                return (ConstantInputMapping<T>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = (ConstantInputMapping<?>) constantInputMappings.get(candidateClass);
                if (mapping != null) {
                    return (ConstantInputMapping<T>) mapping;
                }
            }
            throw new Exception("no constant input mapping found for class " + clazz + ". could not map instance.");
        }

        private <T> AtomOutputMapping<T> getAtomOutputMapping(Class<T> clazz) throws Exception {
            AtomOutputMapping<?> mapping = atomOutputMappings.get(clazz);
            if (mapping == null) {
                return createAtomOutputMapping(clazz);
            }
            return (AtomOutputMapping<T>) mapping;
        }

        private <T> TermOutputMapping<T> getTermOutputMapping(Class<T> clazz) throws Exception {
            TermOutputMapping<?> mapping = termOutputMappings.get(clazz);
            if (mapping == null) {
                return createTermOutputMapping(clazz);
            }
            return (TermOutputMapping<T>) mapping;
        }

        private <T> ConstantOutputMapping<T> getConstantOutputMapping(Class<T> clazz) throws Exception {
            ConstantOutputMapping<?> mapping = constantOutputMappings.get(clazz);
            if (mapping == null) {
                return createConstantOutputMapping(clazz);
            }
            return (ConstantOutputMapping<T>) mapping;
        }

        private Set<Class<?>> getAtomOutputClasses() {
            return atomOutputMappings.keySet();
        }

        private Class<?> getAtomClass(String symbol) {
            return predicateSymbol2class.get(symbol);
        }
    }
}