package asp4j.test.mapping.tripleupdate;

import java.util.Objects;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck May 30, 2013
 */
public class Conflict {

    private String type;
    private Statement statement1;
    private Statement statement2;

    public Statement[] getConflictPair() {
        return new Statement[]{statement1,statement2};
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Statement getStatement1() {
        return statement1;
    }

    public void setStatement1(Statement statement1) {
        this.statement1 = statement1;
    }

    public Statement getStatement2() {
        return statement2;
    }

    public void setStatement2(Statement statement2) {
        this.statement2 = statement2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.type);
        hash = 79 * hash + Objects.hashCode(this.statement1);
        hash = 79 * hash + Objects.hashCode(this.statement2);
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
        final Conflict other = (Conflict) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.statement1, other.statement1)) {
            return false;
        }
        if (!Objects.equals(this.statement2, other.statement2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Conflict{" + "type=" + type + ", st1=" + statement1 + ", st2=" + statement2 + '}';
    }    

}
