package asp4j.mapping;

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
import asp4j.mapping.direct.CanInitFromTerm;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hbeck date May 26, 2013
 */
public abstract class MappingUtils {

    public static Atom asAtom(final Object object) {
        try {
            //direct
            if (object instanceof CanAsAtom) {
                return ((CanAsAtom) object).asAtom();
            }
            //annotation
            Class<?> clazz = object.getClass();
            DefAtom atomAnn = clazz.getAnnotation(DefAtom.class);
            String predicateSymbol = atomAnn.value();
            Map<Integer, Term> termMap = new HashMap<>();
            for (Method method : clazz.getMethods()) {
                Arg argAnnotation = method.getAnnotation(Arg.class);
                if (argAnnotation == null) {
                    continue;
                }
                int arg = argAnnotation.value();
                Object returnedObject = method.invoke(object);
                termMap.put(Integer.valueOf(arg), asTerm(returnedObject));
            }
            return new AtomImpl(predicateSymbol, asArray(termMap));
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.err.println(e);
            return null;
        }
    }

    public static Term asTerm(final Object object) {
        try {
            //direct
            if (object instanceof CanAsTerm) {
                return ((CanAsTerm) object).asTerm();
            }
            if (object instanceof String) {
                return new ConstantImpl((String) object);
            }
            //annotation
            Class<?> clazz = object.getClass();
            DefTerm termAnn = clazz.getAnnotation(DefTerm.class);
            //TODO CURR
            //mappings are not seen here
            //solution: get rid of this, incorporate in binding
            String functionSymbol = termAnn.value();
            Map<Integer, Term> termMap = new HashMap<>();
            for (Method method : clazz.getMethods()) {
                Arg argAnnotation = method.getAnnotation(Arg.class);
                if (argAnnotation == null) {
                    continue;
                }
                int arg = argAnnotation.value();
                Object returnedObject = method.invoke(object);
                termMap.put(Integer.valueOf(arg), asTerm(returnedObject));
            }
            return new TermImpl(functionSymbol, asArray(termMap));
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.err.println(e);
            return null;
        }
    }

    public static Constant asConstant(final Object object) {
        try {
            //direct
            if (object instanceof CanAsConstant) {
                return ((CanAsConstant) object).asConstant();
            }
            if (object instanceof String) {
                return new ConstantImpl((String) object);
            }
            //annotation
            Class<?> clazz = object.getClass();
            DefConstant constAnn = clazz.getAnnotation(DefConstant.class);
            String constantSymbol = constAnn.value();
            return new ConstantImpl(constantSymbol);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public static <T> T asObject(final Atom atom, Class<T> clazz) throws Exception {
        Object object = clazz.newInstance();
        //direct
        if (object instanceof CanInitFromAtom) {
            ((CanInitFromAtom) object).init(atom);
            return (T) object;
        }
        //annotations
        invokeSetters(object, clazz, atom);
        return (T) object;
    }

    public static <T> T asObject(final Term term, Class<T> clazz) throws Exception {
        Object object = clazz.newInstance();
        //direct
        if (object instanceof CanInitFromTerm) {
            ((CanInitFromTerm) object).init(term);
            return (T) object;
        }
        //annotations
        invokeSetters(object, clazz, term);
        return (T) object;
    }

    public static String asObject(final Constant constant) throws Exception {
        return constant.symbol();
    }

    private static <T> void invokeSetters(Object object, Class<T> clazz, HasArgs input) throws Exception {
        for (Method method : clazz.getMethods()) {
            Arg argAnnotation = method.getAnnotation(Arg.class);
            if (argAnnotation == null) {
                continue;
            }
            String getterName = method.getName();
            Class<?> getterReturnType = method.getReturnType();
            int pos = getterName.startsWith("get") ? 3 : 2; //get or is
            String setterName = "set" + getterName.substring(pos);
            int argIdx = argAnnotation.value();
            Method setterMethod = clazz.getMethod(setterName, getterReturnType);
            Term term = input.getArg(argIdx);
            if (term instanceof Constant) {
                setterMethod.invoke(object, asObject((Constant) term));
            } else {
                setterMethod.invoke(object, asObject(term, getterReturnType));
            }
        }
    }

    private static Term[] asArray(Map<Integer, Term> map) {
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
