package asp4j.test.mapping.tripleupdate;

import java.util.Objects;

/**
 *
 * @author hbeck May 30, 2013
 */
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
