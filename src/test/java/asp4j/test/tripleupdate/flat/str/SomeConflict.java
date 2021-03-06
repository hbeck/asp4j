package asp4j.test.tripleupdate.flat.str;

import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;

/**
 *
 * @author hbeck
 * date May 28, 2013
 */
@DefAtom("conflict")
public class SomeConflict {

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
    
}
