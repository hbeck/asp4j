package asp4j.test.annotated.tripleupdate.flat.str;

import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck 
 * date May 15, 2013
 */
@DefAtom("db")
public class InDatabase extends TypedTriple {

    public InDatabase() {
    }

    public InDatabase(Statement statement) {
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
        final InDatabase other = (InDatabase) obj;
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
        return super.hashCode() * Objects.hash("db");
    }
}
