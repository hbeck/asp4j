package asp4j.test.person.annotated;

import asp4j.mapping.annotations.Predicate;
import java.util.Objects;

/**
 *
 * @author hbeck
 * date May 23, 2013
 */
@Predicate("male")
public class Male extends Person {

    public Male() {
    }

    public Male(String id) {
        super(id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Male other = (Male) obj;
        if (this.id==null) {
            if (other.id!=null) {
                return false;
            }
            return true;
        }
        if (other.id==null) {
            return false;
        }
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Male{" + "id=" + id + '}';
    }
}
