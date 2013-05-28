package asp4j.mapping;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.Constant;
import asp4j.mapping.annotations.Predicate;
import asp4j.mapping.direct.CanAsAtom;
import asp4j.mapping.direct.CanInitFromAtom;
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
            Predicate predicateAnn = clazz.getAnnotation(Predicate.class);
            if (predicateAnn == null) {
                String predicateName = clazz.getAnnotation(Constant.class).value();
                return new AtomImpl(predicateName);
            }
            String predicateName = predicateAnn.value();
            Map<Integer, String> argsMap = new HashMap();
            for (Method method : clazz.getMethods()) {
                Arg argAnnotation = method.getAnnotation(Arg.class);
                if (argAnnotation == null) {
                    continue;
                }
                int arg = argAnnotation.value();
                String argValue = (String) method.invoke(object);
                argsMap.put(Integer.valueOf(arg), argValue);
            }
            return new AtomImpl(predicateName, asArray(argsMap));
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.err.println(e);
            return null;
        }
    }

    public static <T> T asObject(Class<T> clazz, final Atom atom) throws Exception {
        Object object = clazz.newInstance();
        //direct
        if (object instanceof CanInitFromAtom) {
            ((CanInitFromAtom) object).init(atom);
            return (T) object;
        }
        invokeSetters(object,clazz,atom); //will do nothing for constants
        return (T) object;
    }

    private static <T> void invokeSetters(Object object, Class<T> clazz, Atom atom) throws Exception {
        for (Method method : clazz.getMethods()) {
            Arg argAnnotation = method.getAnnotation(Arg.class);
            if (argAnnotation == null) {
                continue;
            }
            String getterName = method.getName();
            int pos = getterName.startsWith("get") ? 3 : 2; //get or is
            String setterName = "set" + getterName.substring(pos);
            int argIdx = argAnnotation.value();
            Method setterMethod = clazz.getMethod(setterName, String.class);
            setterMethod.invoke(object, atom.getArg(argIdx));
        }
    }

    private static String[] asArray(Map<Integer, String> map) {
        String[] arr = new String[map.size()];
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            arr[entry.getKey()] = entry.getValue();
        }
        return arr;
    }
}
