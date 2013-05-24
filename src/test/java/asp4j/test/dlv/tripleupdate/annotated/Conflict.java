package asp4j.test.dlv.tripleupdate.annotated;

import asp4j.annotations.Arg;
import asp4j.annotations.Atomname;
import java.util.Objects;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck date May 24, 2013
 */
@Atomname("confl")
public class Conflict {

    private String type;
    private String subject;
    private String predicate;
    private String object1;
    private String object2;

    public Statement[] getConflictPair() {
        Statement[] arr = new Statement[2];
        arr[0] = new StatementImpl(new URIImpl("urn:" + subject),
                new URIImpl("urn:" + predicate),
                new URIImpl("urn:" + object1));
        arr[2] = new StatementImpl(new URIImpl("urn:" + subject),
                new URIImpl("urn:" + predicate),
                new URIImpl("urn:" + object2));
        return arr;
    }

    @Arg(0)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Arg(1)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Arg(2)
    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    @Arg(3)
    public String getObject1() {
        return object1;
    }

    public void setObject1(String object1) {
        this.object1 = object1;
    }

    @Arg(4)
    public String getObject2() {
        return object2;
    }

    public void setObject2(String object2) {
        this.object2 = object2;
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
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.predicate, other.predicate)) {
            return false;
        }
        if (!Objects.equals(this.object1, other.object1)) {
            return false;
        }
        if (!Objects.equals(this.object2, other.object2)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.type);
        hash = 31 * hash + Objects.hashCode(this.subject);
        hash = 31 * hash + Objects.hashCode(this.predicate);
        hash = 31 * hash + Objects.hashCode(this.object1);
        hash = 31 * hash + Objects.hashCode(this.object2);
        return hash;
    }
}
