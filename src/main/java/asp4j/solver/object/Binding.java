package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.lang.HasArgs;
import asp4j.lang.LangElem;
import asp4j.lang.Term;
import asp4j.lang.TermImpl;
import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import asp4j.mapping.annotations.DefConstant;
import asp4j.mapping.annotations.DefTerm;
import asp4j.mapping.object.AnyMapping;
import asp4j.mapping.object.atom.AtomInputMapping;
import asp4j.mapping.object.atom.AtomMapping;
import asp4j.mapping.object.atom.AtomOutputMapping;
import asp4j.mapping.object.constant.ConstantInputMapping;
import asp4j.mapping.object.constant.ConstantMapping;
import asp4j.mapping.object.constant.ConstantOutputMapping;
import asp4j.mapping.object.term.TermInputMapping;
import asp4j.mapping.object.term.TermMapping;
import asp4j.mapping.object.term.TermOutputMapping;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hbeck May 23, 2013
 */
public class Binding {

    private Registry registry;

    public Binding() {
        this.registry = new Registry();
    }

    public Binding addAll(Collection<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            add(clazz);
        }
        return this;
    }

    public <T> Binding add(final Class<T> clazz) throws Exception {
        registry.add(clazz);
        return this;
    }

    public <T, E extends LangElem> Binding add(final Class<T> clazz, AnyMapping<T, E> mapping) throws Exception {
        registry.add(clazz, mapping);
        return this;
    }

//    private <T> AtomOutputMapping<T> createAtomOutputMapping(Class<T> clazz) throws Exception {
//        AtomMapping<?> mapping = createAtomMapping(clazz);
//        registry.addAtomInputMapping(clazz, mapping);
//        registry.addAtomOutputMapping(clazz, mapping);
//        return (AtomOutputMapping<T>) mapping;
//    }
//
//    private <T> TermOutputMapping<T> createTermOutputMapping(Class<T> clazz) throws Exception {
//        TermMapping<?> mapping = createTermMapping(clazz);
//        registry.addTermInputMapping(clazz, mapping);
//        registry.addTermOutputMapping(clazz, mapping);
//        return (TermOutputMapping<T>) mapping;
//    }
//
//    private <T> ConstantOutputMapping<T> createConstantOutputMapping(Class<T> clazz) throws Exception {
//        ConstantMapping<?> mapping = createConstantMapping(clazz);
//        registry.addConstantInputMapping(clazz, mapping);
//        registry.addConstantOutputMapping(clazz, mapping);
//        return (ConstantOutputMapping<T>) mapping;
//    }
//    private <T, E extends LangElem> Class<?> getMappedClass(AnyMapping<T, E> mapping) {
//        return mapping.getClass().getGenericInterfaces()[0].getClass(); //TODO        
//    }

//    public <T> Binding add(AnyAtomMapping<T> mapping) throws Exception {
//        Class<?> clazz = getMappedClass(mapping);
//        System.out.println("clazz: " + clazz);
//        if (mapping instanceof InputMapping) {
//            registry.addAtomInputMapping(clazz, (AtomInputMapping<T>) mapping);
//        }
//        if (mapping instanceof OutputMapping) {
//            registry.addAtomOutputMapping(clazz, (AtomOutputMapping<T>) mapping);
//        }
//        return this;
//    }
//
//    public <T> Binding add(AnyTermMapping<T> mapping) throws Exception {
//        Class<?> clazz = getMappedClass(mapping);
//        if (mapping instanceof InputMapping) {
//            registry.addTermInputMapping(clazz, (TermInputMapping<T>) mapping);
//        }
//        if (mapping instanceof OutputMapping) {
//            registry.addTermOutputMapping(clazz, (TermOutputMapping<T>) mapping);
//        }
//        return this;
//    }
//
//    public <T> Binding add(AnyConstantMapping<T> mapping) throws Exception {
//        Class<?> clazz = getMappedClass(mapping);
//        if (mapping instanceof InputMapping) {
//            registry.addConstantInputMapping(clazz, (ConstantInputMapping<T>) mapping);
//        }
//        if (mapping instanceof OutputMapping) {
//            registry.addConstantOutputMapping(clazz, (ConstantOutputMapping<T>) mapping);
//        }
//        return this;
//    }
    protected <T> Atom mapObjectAsAtom(T t) throws Exception {
        AtomInputMapping<T> mapping = (AtomInputMapping<T>) registry.getAtomInputMapping(t.getClass());
        return mapping.asLangElem(t);
    }

    protected <T> Term mapObjectAsTerm(T t) throws Exception {
        TermInputMapping<T> mapping = (TermInputMapping<T>) registry.getTermInputMapping(t.getClass());
        return mapping.asLangElem(t);
    }

    protected <T> Constant mapObjectAsConstant(T t) throws Exception {
        ConstantInputMapping<T> mapping = (ConstantInputMapping<T>) registry.getConstantInputMapping(t.getClass());
        return mapping.asLangElem(t);
    }

    protected <T> T mapAtomAsObject(Atom atom, Class<T> clazz) throws Exception {
        AtomOutputMapping<T> mapping = (AtomOutputMapping<T>) registry.getAtomOutputMapping(clazz);
        return (T) mapping.asObject(atom);
    }

    protected <T> T mapTermAsObject(Term term, Class<T> clazz) throws Exception {
        TermOutputMapping<T> mapping = (TermOutputMapping<T>) registry.getTermOutputMapping(clazz);
        return (T) mapping.asObject(term);
    }

    protected <T> T mapConstantAsObject(Constant constant, Class<T> clazz) throws Exception {
        ConstantOutputMapping<T> mapping = (ConstantOutputMapping<T>) registry.getConstantOutputMapping(clazz);
        return (T) mapping.asObject(constant);
    }

    /**
     * filters low level set of atoms based on the filters registered in this
     * binding and returns according objects
     *
     * @param atoms
     * @return unmodifiable set of filtered atoms, mapped to according objects
     */
    protected Set<Object> filterAndMap(Set<Atom> atoms, Filter filter) throws Exception {
        Set<Object> set = new HashSet<>();
        for (Atom atom : atoms) {
            Class clazz = registry.getAtomClass(atom.symbol()); //TODO
            if (filter.accepts(clazz)) {
                set.add(mapAtomAsObject(atom, clazz));
            }
        }
        return Collections.unmodifiableSet(set);
    }

    protected List<AnswerSet<Object>> filterAndMap(List<AnswerSet<Atom>> answerSets, Filter filter) throws Exception {
        List<AnswerSet<Object>> list = new ArrayList<>();
        for (AnswerSet<Atom> answerSet : answerSets) {
            list.add(new AnswerSetImpl(filterAndMap(answerSet.atoms(), filter)));
        }
        return Collections.unmodifiableList(list);
    }

    //
    //
    private class Registry {        

        Registry() {
            init();
        }
        //
        private Map<Class<?>, Class<?>> object2LangElem = new HashMap<>();
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
//            AtomMapping<String> am = new AtomMapping<String>() {
//                @Override
//                public Atom asLangElem(String s) throws Exception {
//                    return new AtomImpl(s);
//                }
//
//                @Override
//                public String asObject(Atom atom) throws Exception {
//                    return atom.symbol();
//                }
//
//                @Override
//                public String symbol() {
//                    return null; //TODO
//                }
//            };
//            addAtomInputMapping(String.class, am);
//            addAtomOutputMapping(String.class, am);
//            //
//            TermMapping<String> tm = new TermMapping<String>() {
//                @Override
//                public Term asLangElem(String s) throws Exception {
//                    return new TermImpl(s);
//                }
//
//                @Override
//                public String asObject(Term term) throws Exception {
//                    return term.symbol();
//                }
//
//                @Override
//                public String symbol() {
//                    return null; //TODO
//                }
//            };
//            addTermInputMapping(String.class, tm);
//            addTermOutputMapping(String.class, tm);
//            //
            ConstantMapping<String> cm = new ConstantMapping<String>() {
                @Override
                public Constant asLangElem(String s) throws Exception {
                    return new ConstantImpl(s);
                }

                @Override
                public String asObject(Constant constant) throws Exception {
                    return constant.symbol();
                }
            };
            addConstantInputMapping(String.class, cm);
            addConstantOutputMapping(String.class, cm);
            object2LangElem.put(String.class, Constant.class);
        }

        private <T> void add(final Class<T> clazz) throws Exception {

            if (isRegistered(clazz)) {
                return;
            }

            if (clazz.isAnnotationPresent(DefAtom.class)) {
                AtomMapping<?> mapping = createAtomMapping(clazz);
                registry.addAtomInputMapping(clazz, mapping);
                registry.addAtomOutputMapping(clazz, mapping);
            } else if (clazz.isAnnotationPresent(DefTerm.class)) {
                TermMapping<?> mapping = createTermMapping(clazz);
                registry.addTermInputMapping(clazz, mapping);
                registry.addTermOutputMapping(clazz, mapping);
            } else if (clazz.isAnnotationPresent(DefConstant.class)) {
                ConstantMapping<?> mapping = createConstantMapping(clazz);
                registry.addConstantInputMapping(clazz, mapping);
                registry.addConstantOutputMapping(clazz, mapping);
            } else {
                throw new Exception("cannot bind " + clazz);
            }
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

        private <T, E extends LangElem> void add(Class<?> clazz, AnyMapping<T, E> mapping) {
            if (mapping instanceof AtomMapping) {
                if (mapping instanceof AtomInputMapping) {
                    addAtomInputMapping(clazz, (AtomInputMapping<T>) mapping);
                }
                if (mapping instanceof AtomOutputMapping) {
                    addAtomOutputMapping(clazz, (AtomOutputMapping<T>) mapping);
                }
            } else if (mapping instanceof TermMapping) {
                if (mapping instanceof TermInputMapping) {
                    addTermInputMapping(clazz, (TermInputMapping<T>) mapping);
                }
                if (mapping instanceof TermOutputMapping) {
                    addTermOutputMapping(clazz, (TermOutputMapping<T>) mapping);
                }
            } else if (mapping instanceof ConstantMapping) {
                if (mapping instanceof ConstantInputMapping) {
                    addConstantInputMapping(clazz, (ConstantInputMapping<T>) mapping);
                }
                if (mapping instanceof ConstantOutputMapping) {
                    addConstantOutputMapping(clazz, (ConstantOutputMapping<T>) mapping);
                }
            }
        }

        private void addAtomInputMapping(Class<?> clazz, AtomInputMapping<?> mapping) {
            atomInputMappings.put(clazz, mapping);
            predicateSymbol2class.put(mapping.symbol(), clazz);
            object2LangElem.put(clazz, Atom.class);
            //mapping.setBinding(binding);
        }

        private void addAtomOutputMapping(Class<?> clazz, AtomOutputMapping<?> mapping) {
            atomOutputMappings.put(clazz, mapping);
            predicateSymbol2class.put(mapping.symbol(), clazz);
            object2LangElem.put(clazz, Atom.class);
            //mapping.setBinding(binding);
        }

        private void addTermInputMapping(Class<?> clazz, TermInputMapping<?> mapping) {
            termInputMappings.put(clazz, mapping);
            functionSymbol2class.put(mapping.symbol(), clazz);
            object2LangElem.put(clazz, Term.class);
            //mapping.setBinding(binding);
        }

        private void addTermOutputMapping(Class<?> clazz, TermOutputMapping<?> mapping) {
            termOutputMappings.put(clazz, mapping);
            functionSymbol2class.put(mapping.symbol(), clazz);
            object2LangElem.put(clazz, Term.class);
            //mapping.setBinding(binding);
        }

        private void addConstantInputMapping(Class<?> clazz, ConstantInputMapping<?> mapping) {
            constantInputMappings.put(clazz, mapping);
            object2LangElem.put(clazz, Constant.class);
            //mapping.setBinding(binding);
        }

        private void addConstantOutputMapping(Class<?> clazz, ConstantOutputMapping<?> mapping) {
            constantOutputMappings.put(clazz, mapping);
            object2LangElem.put(clazz, Constant.class);
            //mapping.setBinding(binding);
        }

        private <T> AtomInputMapping<T> getAtomInputMapping(Class<T> clazz) throws Exception {
//            return (AtomInputMapping<T>) atomInputMappings.get(clazz);
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
//            return (TermInputMapping<T>) termInputMappings.get(clazz);
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
//            return (ConstantInputMapping<T>) constantInputMappings.get(clazz);
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
            return null; //TODO
        }

        private <T> AtomOutputMapping<T> getAtomOutputMapping(Class<T> clazz) throws Exception {
//            return (AtomOutputMapping<T>) atomOutputMappings.get(clazz);
            AtomOutputMapping<?> mapping = atomOutputMappings.get(clazz);
            if (mapping != null) {
                return (AtomOutputMapping<T>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = (AtomOutputMapping<?>) atomOutputMappings.get(candidateClass);
                if (mapping != null) {
                    return (AtomOutputMapping<T>) mapping;
                }
            }
            return null; //TODO            
        }

        private <T> TermOutputMapping<T> getTermOutputMapping(Class<T> clazz) throws Exception {
  //          return (TermOutputMapping<T>) termOutputMappings.get(clazz);
            TermOutputMapping<?> mapping = termOutputMappings.get(clazz);
            if (mapping != null) {
                return (TermOutputMapping<T>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = (TermOutputMapping<?>) termOutputMappings.get(candidateClass);
                if (mapping != null) {
                    return (TermOutputMapping<T>) mapping;
                }
            }
            return null; //TODO
        }

        private <T> ConstantOutputMapping<T> getConstantOutputMapping(Class<T> clazz) throws Exception {
//            return (ConstantOutputMapping<T>) constantOutputMappings.get(clazz);
            ConstantOutputMapping<?> mapping = constantOutputMappings.get(clazz);
            if (mapping != null) {
                return (ConstantOutputMapping<T>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = (ConstantOutputMapping<?>) constantOutputMappings.get(candidateClass);
                if (mapping != null) {
                    return (ConstantOutputMapping<T>) mapping;
                }
            }
            return null; //TODO
        }

        private Class<?> getAtomClass(String symbol) {
            return predicateSymbol2class.get(symbol);
        }

        private <T> AtomMapping<T> createAtomMapping(final Class<T> clazz) throws Exception {
            DefAtom atomAnn = clazz.getAnnotation(DefAtom.class);
            final String predicateSymbol = atomAnn.value();
            return new AtomMapping<T>() {
//            @Override
//            public Class<T> clazz() {
//                return clazz;
//            }
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
                        termMap.put(Integer.valueOf(arg), mapObjectAsTerm(returnedObject));
                    }
                    return new AtomImpl(predicateSymbol, asArray(termMap));
                }

                @Override
                public T asObject(Atom atom) throws Exception {
                    Object object = clazz.newInstance();
                    invokeSetters(object, clazz, atom);
                    return (T) object;
                }
            };
        }

        private <T> TermMapping<T> createTermMapping(final Class<T> clazz) throws Exception {
            return new TermMapping<T>() {
//            @Override
//            public Class<T> clazz() {
//                return clazz;
//            }
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
                        termMap.put(Integer.valueOf(arg), mapObjectAsTerm(returnedObject));
                    }
                    return new TermImpl(symbol(), asArray(termMap));
                }

                @Override
                public T asObject(Term term) throws Exception {
                    Object object = clazz.newInstance();
                    invokeSetters(object, clazz, term);
                    return (T) object;
                }
            };
        }

        private <T> ConstantMapping<T> createConstantMapping(final Class<T> clazz) throws Exception {
            return new ConstantMapping<T>() {
//            @Override
//            public Class<T> clazz() {
//                return clazz;
//            }
                @Override
                public Constant asLangElem(T t) throws Exception {
                    DefConstant constAnn = clazz.getAnnotation(DefConstant.class);
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
                    setterMethod.invoke(object, mapConstantAsObject((Constant) term, type));
                } else {
                    setterMethod.invoke(object, mapTermAsObject(term, type));
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
    }
}