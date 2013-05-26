package asp4j.test.dlv.person.annotated;

import asp4j.mapping.annotations.Predicate;
import java.util.Objects;

/**
 *
 * @author hbeck 
 * date May 23, 2013
 */
@Predicate("female")
public class Female extends Person {

    public Female() {
    }

    public Female(String id) {
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
        final Female other = (Female) obj;
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
        return "Female{" + "id=" + id + '}';
    }
}
