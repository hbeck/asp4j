package asp4j.mapping.annotations;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author hbeck 
 * date May 23, 2013
 */
public abstract class ReflectionUtils {

    public static Atom asAtom(final Object object) {
        try {
            Class<?> clazz = object.getClass();
            String predicateName = clazz.getAnnotation(Atomname.class).value();
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

    public static Object asObject(Class<?> clazz, final Atom atom) throws Exception {
        Object object = clazz.newInstance();
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
        return object;
    }

    private static String[] asArray(Map<Integer, String> map) {
        String[] arr = new String[map.size()];
        for (Entry<Integer, String> entry : map.entrySet()) {
            arr[entry.getKey()] = entry.getValue();
        }
        return arr;
    }
}
