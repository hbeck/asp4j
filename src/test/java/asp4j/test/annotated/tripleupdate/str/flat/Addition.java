package asp4j.test.annotated.tripleupdate.str.flat;

import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck 
 * date May 14, 2013
 */
@DefAtom("add")
public class Addition extends TypedTriple {

    public Addition() {
    }

    public Addition(Statement statement) {
        super(statement);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Addition other  = (Addition) obj;
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.predicate, other.predicate)) {
            return false;
        }
        if (!Objects.equals(this.object, other.object)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode()*Objects.hash("add");
    }
}
