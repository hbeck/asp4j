package asp4j.mapping.object;

import asp4j.lang.LangElem;
import asp4j.mapping.annotations.MapWith;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hbeck Jun 8, 2013
 */
public abstract class EnumMappingBase<T extends Enum<T>, E extends LangElem> implements Mapping<T, E>, HasTargetNames {

    private final Class<T> enumType;
    private final Map<String, String> constantName2targetName;
    private final Map<String, String> targetName2constantName;
    private Set<String> targetNames;

    public EnumMappingBase(Class<T> enumType) {
        this.enumType = enumType;
        this.constantName2targetName = new HashMap<>();
        this.targetName2constantName = new HashMap<>();
        this.targetNames = new HashSet<>();
        init();
    }

    private void init() {
        for (Field field : enumType.getDeclaredFields()) {
            if (field.isEnumConstant()) {
                if (field.isAnnotationPresent(MapWith.class)) {
                    String targetName = field.getAnnotation(MapWith.class).value();
                    constantName2targetName.put(field.getName(), targetName);
                    targetName2constantName.put(targetName, field.getName());
                    targetNames.add(targetName);
                } else {
                    targetNames.add(field.getName());
                }
            }
        }
    }

    protected final String getTargetName(T t) {
        return constantName2targetName.containsKey(t.name())
               ? constantName2targetName.get(t.name()) : t.name();
    }

    @Override
    public final T asObject(E a) throws Exception {
        String constantName = targetName2constantName.containsKey(a.symbol())
                              ? targetName2constantName.get(a.symbol()) : a.symbol();
        return Enum.valueOf(enumType, constantName);
    }

    @Override
    public final Set<String> getTargetNames() {
        return Collections.unmodifiableSet(targetNames);
    }
}
