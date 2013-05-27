package asp4j.test.tripleupdate.direct;

import asp4j.lang.Atom;
import asp4j.mapping.direct.CanInitFromAtom;
import java.util.Objects;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck 
 * date May 21, 2013
 */
public class Conflict implements CanInitFromAtom {

    private String type;
    private Statement statement1;
    private Statement statement2;

    public Conflict() {
    }

    @Override
    public void init(Atom atom) {
        //confl(single_violation,car,hasColor,blue,red)
        this.type = atom.getArg(0);
        this.statement1 = new StatementImpl(new URIImpl("urn:"+atom.getArg(1)),
                                            new URIImpl("urn:"+atom.getArg(2)),
                                            new URIImpl("urn:"+atom.getArg(3)));
        this.statement2 = new StatementImpl(new URIImpl("urn:"+atom.getArg(1)),
                                            new URIImpl("urn:"+atom.getArg(2)),
                                            new URIImpl("urn:"+atom.getArg(4)));
    }

    @Override
    public String predicateName() {
        return "confl";
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
    public String toString() {
        return "Conflict{" + "type=" + type + ", statement1=" + statement1 + ", statement2=" + statement2 + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.type);
        hash = 73 * hash + Objects.hashCode(this.statement1);
        hash = 73 * hash + Objects.hashCode(this.statement2);
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
    
    
}
