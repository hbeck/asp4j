package asp4j.test.dlv.tripleupdate.wrapped;

import asp4j.mapping.direct.OutputAtom;
import asp4j.test.dlv.person.wrapped.Person;
import java.util.Objects;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck date May 14, 2013
 */
public class Addition extends TypedTriple implements OutputAtom {

    public Addition() {
    }

    public Addition(Statement statement) {
        super(statement);
    }

    @Override
    public String predicateName() {
        return "add";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Addition other = (Addition) obj;
        if (!Objects.equals(this.getStatement(), other.getStatement())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 59 * Objects.hash(predicateName()) * super.hashCode();
    }
}
