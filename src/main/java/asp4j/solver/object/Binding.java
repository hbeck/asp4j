package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.lang.HasArgs;
import asp4j.lang.Term;
import asp4j.lang.TermImpl;
import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import asp4j.mapping.annotations.DefConstant;
import asp4j.mapping.annotations.DefTerm;
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
import asp4j.mapping.object.atom.AtomMapping;
import asp4j.mapping.object.atom.AtomMappingBase;
import asp4j.mapping.object.atom.AtomOutputMapping;
import asp4j.mapping.object.atom.AtomOutputMappingBase;
import asp4j.mapping.object.constant.AnyConstantMapping;
import asp4j.mapping.object.constant.ConstantInputMapping;
import asp4j.mapping.object.constant.ConstantInputMappingBase;
import asp4j.mapping.object.constant.ConstantMapping;
import asp4j.mapping.object.constant.ConstantMappingBase;
import asp4j.mapping.object.constant.ConstantOutputMapping;
import asp4j.mapping.object.constant.ConstantOutputMappingBase;
import asp4j.mapping.object.term.AnyTermMapping;
import asp4j.mapping.object.term.TermInputMapping;
import asp4j.mapping.object.term.TermInputMappingBase;
import asp4j.mapping.object.term.TermMapping;
import asp4j.mapping.object.term.TermMappingBase;
import asp4j.mapping.object.term.TermOutputMapping;
import asp4j.mapping.object.term.TermOutputMappingBase;
import java.lang.reflect.Method;
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

        if (clazz.isAnnotationPresent(DefAtom.class)) {
            AtomMapping<?> mapping = createAtomMappingByAnnotation(clazz);
            registry.addAtomInputMapping(mapping);
            registry.addAtomOutputMapping(mapping);
        } else if (clazz.isAnnotationPresent(DefTerm.class)) {
            TermMapping<?> mapping = createTermMappingByAnnotation(clazz);
            registry.addTermInputMapping(mapping);
            registry.addTermOutputMapping(mapping);
        } else if (clazz.isAnnotationPresent(DefConstant.class)) {
            ConstantMapping<?> mapping = createConstantMappingByAnnotation(clazz);
            registry.addConstantInputMapping(mapping);
            registry.addConstantOutputMapping(mapping);
        } else {
            return tryAddByInterfaces(clazz, inst);
        }

        return this;
    }

    private <T> Binding tryAddByInterfaces(final Class<T> clazz, T inst) throws Exception {

        if (inst instanceof CanAsAtom) {
            AtomInputMapping<?> mapping = createAtomInputMappingByInterface((Class<CanAsAtom>) clazz, (CanAsAtom) inst);
            registry.addAtomInputMapping(mapping);
        } else if (inst instanceof CanAsTerm) {
            TermInputMapping<?> mapping = createTermInputMappingByInterface((Class<CanAsTerm>) clazz, (CanAsTerm) inst);
            registry.addTermInputMapping(mapping);
        } else if (inst instanceof CanAsConstant) {
            ConstantInputMapping<?> mapping = createConstantInputMappingByInterface((Class<CanAsConstant>) clazz);
            registry.addConstantInputMapping(mapping);
        }

        if (inst instanceof CanInitFromAtom) {
            AtomOutputMapping<?> mapping = createAtomOutputMappingByInterface((Class<CanInitFromAtom>) clazz, (CanInitFromAtom) inst);
            registry.addAtomOutputMapping(mapping);
        } else if (inst instanceof CanInitFromTerm) {
            TermOutputMapping<?> mapping = createTermOutputMappingByInterface((Class<CanInitFromTerm>) clazz, (CanInitFromTerm) inst);
            registry.addTermOutputMapping(mapping);
        } else if (inst instanceof CanInitFromConstant) {
            ConstantOutputMapping<?> mapping = createConstantOutputMappingByInterface((Class<CanInitFromConstant>) clazz);
            registry.addConstantOutputMapping(mapping);
        }

        return this;
    }

    private <T> void addOutputClass(final Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromAtom) {
            AtomOutputMapping<?> mapping = createAtomOutputMappingByInterface((Class<CanInitFromAtom>) clazz, (CanInitFromAtom) inst);
            registry.addAtomOutputMapping(mapping);
            return;
        }
        AtomMapping<?> mapping = createAtomMappingByAnnotation(clazz);
        registry.addAtomInputMapping(mapping);
        registry.addAtomOutputMapping(mapping);
    }

    private <T> AtomOutputMapping<T> createAtomOutputMapping(Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromAtom) {
            AtomOutputMapping<?> mapping = createAtomOutputMappingByInterface((Class<CanInitFromAtom>) clazz, (CanInitFromAtom) inst);
            registry.addAtomOutputMapping(mapping);
            return (AtomOutputMapping<T>) mapping;
        }
        AtomMapping<?> mapping = createAtomMappingByAnnotation(clazz);
        registry.addAtomInputMapping(mapping);
        registry.addAtomOutputMapping(mapping);
        return (AtomOutputMapping<T>) mapping;
    }

    private <T> TermOutputMapping<T> createTermOutputMapping(Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromTerm) {
            TermOutputMapping<?> mapping = createTermOutputMappingByInterface((Class<CanInitFromTerm>) clazz, (CanInitFromTerm) inst);
            registry.addTermOutputMapping(mapping);
            return (TermOutputMapping<T>) mapping;
        }
        TermMapping<?> mapping = createTermMappingByAnnotation(clazz);
        registry.addTermInputMapping(mapping);
        registry.addTermOutputMapping(mapping);
        return (TermOutputMapping<T>) mapping;
    }

    private <T> ConstantOutputMapping<T> createConstantOutputMapping(Class<T> clazz) throws Exception {
        T inst = clazz.newInstance();
        if (inst instanceof CanInitFromConstant) {
            ConstantOutputMapping<?> mapping = createConstantOutputMappingByInterface((Class<CanInitFromConstant>) clazz);
            registry.addConstantOutputMapping(mapping);
            return (ConstantOutputMapping<T>) mapping;
        }
        ConstantMapping<?> mapping = createConstantMappingByAnnotation(clazz);
        registry.addConstantInputMapping(mapping);
        registry.addConstantOutputMapping(mapping);
        return (ConstantOutputMapping<T>) mapping;
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
        TermInputMapping<? extends Object> termInputMapping = registry.getTermInputMapping(t.getClass());
        if (termInputMapping != null) {
            TermInputMapping<T> mapping = (TermInputMapping<T>) termInputMapping;
            return mapping.asLangElem(t);
        }
        try {
            add((Class<T>)t.getClass(), t);
            TermInputMapping<T> mapping = (TermInputMapping<T>)registry.getTermInputMapping(t.getClass());
            return mapping.asLangElem(t);
        } catch (Exception e) {
            //TODO
        }
        return asConstant(t);
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

    //
    private <T> AtomMapping<T> createAtomMappingByAnnotation(final Class<T> clazz) throws Exception {
        DefAtom atomAnn = clazz.getAnnotation(DefAtom.class);
        final String predicateSymbol = atomAnn.value();
        return new AtomMappingBase<T>() {
            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public String symbol() {
                return predicateSymbol;
            }

            @Override
            public Atom asLangElem(T t) throws Exception {
                Map<Integer, Term> termMap = new HashMap<>();
                for (Method method : clazz.getMethods()) {
                    Arg argAnnotation = method.getAnnotation(Arg.class);
                    if (argAnnotation == null) {
                        continue;
                    }
                    int arg = argAnnotation.value();
                    Object returnedObject = method.invoke(t);
                    termMap.put(Integer.valueOf(arg), asTerm(returnedObject));
                }
                return new AtomImpl(predicateSymbol, asArray(termMap));
            }

            @Override
            public T asObject(Atom atom) throws Exception {
                Object object = clazz().newInstance();
                invokeSetters(object, clazz, atom);
                return (T) object;
            }
        };
    }

    private <T> TermMapping<T> createTermMappingByAnnotation(final Class<T> clazz) throws Exception {
        return new TermMappingBase<T>() {
            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public String symbol() {
                DefTerm termAnn = clazz.getAnnotation(DefTerm.class);
                return termAnn.value();
            }

            @Override
            public Term asLangElem(T t) throws Exception {
                Map<Integer, Term> termMap = new HashMap<>();
                for (Method method : clazz.getMethods()) {
                    Arg argAnnotation = method.getAnnotation(Arg.class);
                    if (argAnnotation == null) {
                        continue;
                    }
                    int arg = argAnnotation.value();
                    Object returnedObject = method.invoke(t);
                    termMap.put(Integer.valueOf(arg), asTerm(returnedObject));
                }
                return new TermImpl(symbol(), asArray(termMap));
            }

            @Override
            public T asObject(Term term) throws Exception {
                Object object = clazz().newInstance();
                invokeSetters(object, clazz, term);
                return (T) object;
            }
        };
    }

    private <T> ConstantMapping<T> createConstantMappingByAnnotation(final Class<T> clazz) throws Exception {
        return new ConstantMappingBase<T>() {
            @Override
            public Class<T> clazz() {
                return clazz;
            }

            @Override
            public Constant asLangElem(T t) throws Exception {
                DefConstant constAnn = clazz().getAnnotation(DefConstant.class);
                String symbol = constAnn.value();
                return new ConstantImpl(symbol);
            }

            @Override
            public T asObject(Constant constant) throws Exception {
                return clazz.newInstance();
            }
        };
    }

    private <T> void invokeSetters(Object object, Class<T> clazz, HasArgs input) throws Exception {
        for (Method method : clazz.getMethods()) {
            Arg argAnnotation = method.getAnnotation(Arg.class);
            if (argAnnotation == null) {
                continue;
            }
            String getterName = method.getName();
            Class<?> type = method.getReturnType();
            int pos = getterName.startsWith("get") ? 3 : 2; //get or is
            String setterName = "set" + getterName.substring(pos);
            int argIdx = argAnnotation.value();
            Method setterMethod = clazz.getMethod(setterName, type);
            Term term = input.getArg(argIdx);
            if (term instanceof Constant) {
                setterMethod.invoke(object, constantAsObject((Constant) term, type));
            } else {
                setterMethod.invoke(object, termAsObject(term, type));
            }
        }
    }

    private Term[] asArray(Map<Integer, Term> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Term[] arr = new Term[map.size()];
        for (Map.Entry<Integer, Term> entry : map.entrySet()) {
            arr[entry.getKey()] = entry.getValue();
        }
        return arr;
    }

    //
    private <T extends CanAsAtom> AtomInputMapping<T> createAtomInputMappingByInterface(final Class<T> clazz, final CanAsAtom template) throws Exception {
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

    private <T extends CanAsTerm> TermInputMapping<T> createTermInputMappingByInterface(final Class<T> clazz, final CanAsTerm template) throws Exception {
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

    private <T extends CanAsConstant> ConstantInputMapping<T> createConstantInputMappingByInterface(final Class<T> clazz) throws Exception {
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

    private <T extends CanInitFromAtom> AtomOutputMapping<T> createAtomOutputMappingByInterface(final Class<T> clazz, final CanInitFromAtom template) throws Exception {
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

    private <T extends CanInitFromTerm> TermOutputMapping<T> createTermOutputMappingByInterface(final Class<T> clazz, final CanInitFromTerm template) throws Exception {
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

    private <T extends CanInitFromConstant> ConstantOutputMapping<T> createConstantOutputMappingByInterface(final Class<T> clazz) throws Exception {
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

    //
    private class Registry {

        private final Binding binding;

        Registry(Binding binding) {
            this.binding = binding;
            init();
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

        private void init() {
            ConstantMapping<String> cm = new ConstantMappingBase<String>() {
                @Override
                public Class<String> clazz() {
                    return String.class;
                }

                @Override
                public Constant asLangElem(String s) throws Exception {
                    return new ConstantImpl(s);
                }

                @Override
                public String asObject(Constant constant) throws Exception {
                    return constant.symbol();
                }
            };
            addConstantInputMapping(cm);
            addConstantOutputMapping(cm);
        }

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
            return null; //try load/constant input mapping above. TODO
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
            return null; //try load TODO
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