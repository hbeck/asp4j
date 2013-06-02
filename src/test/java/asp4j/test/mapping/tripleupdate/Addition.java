package asp4j.test.mapping.tripleupdate;

import java.util.Objects;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck May 30, 2013
 */
public class Addition {
    
    private Statement statement;

    public Addition() {
    }

    public Addition(Statement statement) {
        this.statement=statement;
    }
    
    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.statement);
        hash = 97 * hash + Objects.hashCode("add");        
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
        final Addition other = (Addition) obj;
        if (!Objects.equals(this.statement, other.statement)) {
            return false;
        }
        return true;
    }    

    @Override
    public String toString() {
        return "Addition{" + "statement=" + statement + '}';
    }    

}
