package asp4j.test.direct.tripleupdate.term.str;

import asp4j.lang.Atom;
import asp4j.mapping.direct.CanInitFromAtom;
import java.util.Objects;

/**
 *
 * @author hbeck May 30, 2013
 */
public class SomeConflict implements CanInitFromAtom {

    @Override
    public int hashCode() {
        return Objects.hash("asp4j_test_tripleupdate_annotated_some_conflict");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==null) {
            return false;
        }
        if (!(obj instanceof SomeConflict)) {
            return false;
        }
        return true;
    }

    @Override
    public void init(Atom atom) {
        //nothing
    }

    @Override
    public String symbol() {
        return "some_conflict";
    }
    
}
