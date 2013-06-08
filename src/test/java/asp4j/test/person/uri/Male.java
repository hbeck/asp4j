package asp4j.test.person.uri;

import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;
import org.openrdf.model.URI;

/**
 *
 * @author hbeck June 2, 2013
 */
@DefAtom("male")
public class Male extends Person {

    public Male() {
    }

    public Male(URI id) {
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
