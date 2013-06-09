package asp4j.mapping.object.constant;

import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.mapping.object.EnumMappingBase;

/**
 *
 * @author hbeck Jun 8, 2013
 */
public final class ConstantEnumMapping<T extends Enum<T>> extends EnumMappingBase<T,Constant> implements ConstantMapping<T> {

    public ConstantEnumMapping(Class<T> enumType) {
        super(enumType);
    }

    @Override
    public Constant asLangElem(T t) {
        return new ConstantImpl(getTargetName(t));
    }

}
