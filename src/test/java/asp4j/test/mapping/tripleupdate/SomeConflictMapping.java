package asp4j.test.mapping.tripleupdate;

import asp4j.lang.Atom;
import asp4j.mapping.object.atom.AtomOutputMappingBase;

/**
 * TODO non-parameterized conflicts better modelled with enum!
 *
 * @author hbeck Jun 1, 2013
 */
public class SomeConflictMapping extends AtomOutputMappingBase<SomeConflict>{

//    @Override
//    public Atom asLangElem(SomeConflict t) throws Exception {
//        return new AtomImpl(symbol());
//    }

    @Override
    public SomeConflict asObject(Atom atom) throws Exception {
        return new SomeConflict();
    }

    @Override
    public Class<SomeConflict> clazz() {
        return SomeConflict.class;
    }

    @Override
    public String symbol() {
        return "some_conflict";
    }

}
