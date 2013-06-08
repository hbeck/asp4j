package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.AnswerSetImpl;
import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.lang.HasArgs;
import asp4j.lang.HasSymbol;
import asp4j.lang.LangElem;
import asp4j.lang.Term;
import asp4j.lang.TermImpl;
import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import asp4j.mapping.annotations.DefConstant;
import asp4j.mapping.annotations.DefEnumAtoms;
import asp4j.mapping.annotations.DefEnumConstants;
import asp4j.mapping.annotations.DefTerm;
import asp4j.mapping.object.AnyMapping;
import asp4j.mapping.object.InputMapping;
import asp4j.mapping.object.Mapping;
import asp4j.mapping.object.OutputMapping;
import asp4j.mapping.object.atom.AtomMapping;
import asp4j.mapping.object.constant.ConstantMapping;
import asp4j.mapping.object.term.TermMapping;
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

    /**
     * add explicit mapping for given class.
     *
     * @param <T>
     * @param <E>
     * @param clazz
     * @param mapping
     * @return
     * @throws Exception
     */
    public <T, E extends LangElem> Binding add(final Class<T> clazz, AnyMapping<T, E> mapping) throws Exception {
        registry.add(clazz, mapping);
        return this;
    }

    /**
     * add implicit mapping for given class. mapping is derived by annotations
     * of this class.
     *
     * @param <T>
     * @param clazz
     * @return
     * @throws Exception
     */
    public <T> Binding add(final Class<T> clazz) throws Exception {
        registry.add(clazz);
        return this;
    }

    /**
     * @see add(Class<?> class)
     * @param classes
     * @return
     * @throws Exception
     */
    public Binding addAll(Collection<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            registry.add(clazz);
        }
        return this;
    }

    //
    // solver methods
    //
    protected <T, E extends LangElem> E mapAsLangElem(T t) throws Exception {
        InputMapping<T, E> mapping = (InputMapping<T, E>) registry.getInputMapping(t.getClass());
        return mapping.asLangElem(t);
    }

    protected <T, E extends LangElem> T mapAsObject(E e) throws Exception {
        OutputMapping<T, E> mapping = (OutputMapping<T, E>) registry.getOutputMapping(e);
        return mapping.asObject(e);
    }

    protected <T, E extends LangElem> T mapAsObject(E e, Class<? extends T> clazz) throws Exception {
        OutputMapping<T, E> mapping = (OutputMapping<T, E>) registry.getOutputMapping(clazz);
        return mapping.asObject(e);
    }

    /**
     * filters low level set of atoms based on the filters registered in this
     * binding and returns according objects
     *
     * @param atoms
     * @return unmodifiable set of filtered atoms, mapped to according objects
     */
    protected Set<Object> filterAndMap(Set<Atom> atoms, Filter filter) throws Exception {
        Set<Object> result = new HashSet<>();
        for (Atom atom : atoms) {
            Class clazz = registry.getClassForSymbol(atom.symbol());
            if (filter.accepts(clazz)) {
                result.add(mapAsObject(atom));
            }
        }
        return Collections.unmodifiableSet(result);
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
        private Map<Class<?>, InputMapping<?, ?>> inputMappings = new HashMap<>();
        private Map<Class<?>, OutputMapping<?, ?>> outputMappings = new HashMap<>();
        private Map<String, Class<?>> symbol2class = new HashMap<>();

        private void init() {
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
            addInputMapping(String.class, cm);
            addOutputMapping(String.class, cm);
        }

        private boolean isRegistered(Class<?> clazz) {
            return inputMappings.containsKey(clazz) || outputMappings.containsKey(clazz);
        }

        private <T, E extends LangElem> void addInputMapping(Class<? extends T> clazz, InputMapping<T, E> mapping) {
            inputMappings.put(clazz, mapping);
        }

        private <T, E extends LangElem> void addOutputMapping(Class<? extends T> clazz, OutputMapping<T, E> mapping) {
            outputMappings.put(clazz, mapping);
            if (clazz.isEnum()) {
                for (T enumValue : clazz.getEnumConstants()) {
                    symbol2class.put(enumValue.toString(),clazz);
                }
            } else if (mapping instanceof HasSymbol) {
                symbol2class.put(((HasSymbol) mapping).symbol(), clazz);
            }
        }

        /**
         * add explicit mapping
         *
         * @param <T>
         * @param <E>
         * @param clazz
         * @param mapping
         */
        private <T, E extends LangElem> void add(Class<? extends T> clazz, AnyMapping<T, E> mapping) {
            if (isRegistered(clazz)) {
                return;
            }
            if (mapping instanceof InputMapping) {
                addInputMapping(clazz, (InputMapping<T, E>) mapping);
            }
            if (mapping instanceof OutputMapping) {
                addOutputMapping(clazz, (OutputMapping<T, E>) mapping);
            }
        }

        /**
         * add implicit mapping to be generated from annotations
         *
         * @param <T>
         * @param <E>
         * @param clazz
         * @throws Exception
         */
        private void add(final Class<?> clazz) throws Exception {
            if (isRegistered(clazz)) {
                return;
            }
            if (clazz.isEnum()) {
                addEnum(clazz);
            } else {
                addAnnotatedClass(clazz);
            }
        }

        private void addEnum(final Class clazz) throws Exception {
            Mapping<?, ?> mapping;
            if (clazz.isAnnotationPresent(DefEnumAtoms.class)) {
                mapping = createAtomEnumMapping(clazz);
            } else if (clazz.isAnnotationPresent(DefEnumConstants.class)) {
                mapping = createConstantEnumMapping(clazz);
            } else {
                return;
            }
            addInputMapping(clazz, (InputMapping<?, ?>) mapping);
            addOutputMapping(clazz, (OutputMapping<?, ?>) mapping);
        }

        private <T, E extends LangElem> void addAnnotatedClass(final Class<T> clazz) throws Exception {
            AnyMapping<T, ?> mapping;
            if (clazz.isAnnotationPresent(DefAtom.class)) {
                mapping = createAtomMapping(clazz);
            } else if (clazz.isAnnotationPresent(DefTerm.class)) {
                mapping = createTermMapping(clazz);
            } else if (clazz.isAnnotationPresent(DefConstant.class)) {
                mapping = createConstantMapping(clazz);
            } else {
                return; //no inner resolving. assume explicit mapping given
            }

            if (mapping instanceof InputMapping) {
                addInputMapping(clazz, (InputMapping<T, E>) mapping);
            }
            if (mapping instanceof OutputMapping) {
                addOutputMapping(clazz, (OutputMapping<T, E>) mapping);
            }

            Collection<Class<?>> innerClasses = getAnnotatedInnerClasses(clazz);
            for (Class<?> innerClass : innerClasses) {
                this.add(innerClass);
            }
        }

        private <T extends Enum<T>> AtomMapping<T> createAtomEnumMapping(final Class<T> enumType) {
            return new AtomMapping<T>() {
                @Override
                public Atom asLangElem(T t) throws Exception {
                    return new AtomImpl(t.name());
                }

                @Override
                public T asObject(Atom a) throws Exception {
                    return Enum.valueOf(enumType, a.symbol());
                }

                @Override
                public String symbol() {
                    throw new UnsupportedOperationException("symbol cannot be resolved for mapped enum");
                }
            };
        }

        private <T extends Enum<T>> ConstantMapping<T> createConstantEnumMapping(final Class<T> enumType) {
            return new ConstantMapping<T>() {
                @Override
                public Constant asLangElem(T t) throws Exception {
                    return new ConstantImpl(t.name());
                }

                @Override
                public T asObject(Constant c) throws Exception {
                    return Enum.valueOf(enumType, c.symbol());
                }
            };
        }

        private Collection<Class<?>> getAnnotatedInnerClasses(Class<?> clazz) {
            Collection<Class<?>> classes = new HashSet<>();
            for (Method method : clazz.getMethods()) {
                Arg argAnnotation = method.getAnnotation(Arg.class);
                if (argAnnotation == null) {
                    continue;
                }
                Class<?> type = method.getReturnType();
                classes.add(type);
            }
            return classes;
        }

        private <T, E extends LangElem> InputMapping<T, E> getInputMapping(Class<? extends T> clazz) throws Exception {
            InputMapping<?, ?> mapping = inputMappings.get(clazz);
            if (mapping != null) {
                return (InputMapping<T, E>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = inputMappings.get(candidateClass);
                if (mapping != null) {
                    return (InputMapping<T, E>) mapping;
                }
            }
            throw new Exception("no input mapping found for class " + clazz);
        }

        private <T, E extends LangElem> OutputMapping<T, E> getOutputMapping(E e) throws Exception {
            Class<? extends T> clazz = (Class<? extends T>) symbol2class.get(e.symbol());
            return getOutputMapping(clazz);
        }

        private <T, E extends LangElem> OutputMapping<T, E> getOutputMapping(Class<? extends T> clazz) throws Exception {
            OutputMapping<?, ?> mapping = outputMappings.get(clazz);
            if (mapping != null) {
                return (OutputMapping<T, E>) mapping;
            }
            for (Class<?> candidateClass : clazz.getInterfaces()) {
                mapping = outputMappings.get(candidateClass);
                if (mapping != null) {
                    return (OutputMapping<T, E>) mapping;
                }
            }
            throw new Exception("no output mapping found for class " + clazz);
        }

        private <T> AtomMapping<T> createAtomMapping(final Class<T> clazz) throws Exception {
            DefAtom atomAnn = clazz.getAnnotation(DefAtom.class);
            final String predicateSymbol = atomAnn.value();
            return new AtomMapping<T>() {
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
                        termMap.put(Integer.valueOf(arg), (Term) mapAsLangElem(returnedObject));
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
                        termMap.put(Integer.valueOf(arg), (Term) mapAsLangElem(returnedObject));
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
                @Override
                public Constant asLangElem(T t) throws Exception {
                    DefConstant constAnn = clazz.getAnnotation(DefConstant.class);
                    return new ConstantImpl(constAnn.value());
                }

                @Override
                public T asObject(Constant constant) throws Exception {
                    return clazz.newInstance();
                }
            };
        }

        private <T, E extends LangElem> void invokeSetters(Object object, Class<T> clazz, HasArgs input) throws Exception {
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
                setterMethod.invoke(object, mapAsObject(term, type));
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

        private Class getClassForSymbol(String symbol) {
            return symbol2class.get(symbol);
        }
    }
}