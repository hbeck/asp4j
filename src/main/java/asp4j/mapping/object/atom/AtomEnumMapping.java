package asp4j.mapping.object.atom;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.mapping.object.EnumMappingBase;

/**
 *
 * @author hbeck Jun 8, 2013
 */
public final class AtomEnumMapping<T extends Enum<T>> extends EnumMappingBase<T,Atom> implements AtomMapping<T> {

    public AtomEnumMapping(Class<T> enumType) {
        super(enumType);
    }

    @Override
    public String symbol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Atom asLangElem(T t) {
        return new AtomImpl(getTargetName(t));
    }

}
