package asp4j.test.annotated.tripleupdate.term.str;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefTerm;
import java.util.Objects;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck May 30, 2013
 */
@DefTerm("t")
public class Triple {

    protected String subject;
    protected String predicate;
    protected String object;
    
    private Statement statement;

    public Triple() {
    }

    public Triple(Statement statement) {
        this.statement = statement;
        this.subject = statement.getSubject().stringValue().substring(4);
        this.predicate = statement.getPredicate().stringValue().substring(4);
        this.object = statement.getObject().stringValue().substring(4);
    }

    @Arg(0)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        this.statement = null;
    }

    @Arg(1)
    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
        this.statement = null;
    }

    @Arg(2)
    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
        this.statement = null;
    }

    public Statement getStatement() {
        if (statement == null) {
            URI subjectUri = new URIImpl("urn:" + subject);
            URI predicateUri = new URIImpl("urn:" + predicate);
            URI objectUri = new URIImpl("urn:" + object);
            statement = new StatementImpl(subjectUri, predicateUri, objectUri);
        }
        return statement;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.subject);
        hash = 53 * hash + Objects.hashCode(this.predicate);
        hash = 53 * hash + Objects.hashCode(this.object);
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
        final Triple other = (Triple) obj;
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
    public String toString() {
        return "Triple{" + "subject=" + subject + ", predicate=" + predicate + ", object=" + object + '}';
    }    
    
}
