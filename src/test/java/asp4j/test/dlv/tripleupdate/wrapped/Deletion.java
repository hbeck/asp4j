package asp4j.test.dlv.tripleupdate.wrapped;

import asp4j.mapping.OutputAtom;
import java.util.Objects;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck date May 14, 2013
 */
public class Deletion extends TypedTriple implements OutputAtom {

    public Deletion() {
    }

    public Deletion(Statement statement) {
        super(statement);
    }

    @Override
    public String predicateName() {
        return "del";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Deletion other = (Deletion) obj;
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
