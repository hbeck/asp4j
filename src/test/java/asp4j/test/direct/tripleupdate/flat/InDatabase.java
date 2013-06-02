package asp4j.test.direct.tripleupdate.flat;

import asp4j.mapping.direct.CanInitFromAtom;
import java.util.Objects;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck 
 * date May 15, 2013
 */
public class InDatabase extends TypedTriple implements CanInitFromAtom {

    public InDatabase() {
        super();
    }

    public InDatabase(Statement statement) {
        super(statement);
    }

    @Override
    public String symbol() {
        return "db";
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
        if (!Objects.equals(this.getStatement(), other.getStatement())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 59 * Objects.hash(symbol()) * super.hashCode();
    }
}
