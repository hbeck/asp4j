package asp4j.test.dlv.person.wrapped;

import java.util.Objects;

/**
 *
 * @author hbeck date May 20, 2013
 */
public class Female extends Person {

    public Female() {
    }

    public Female(String id) {
        super(id);
    }

    @Override
    public String predicateName() {
        return "female";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }
}
