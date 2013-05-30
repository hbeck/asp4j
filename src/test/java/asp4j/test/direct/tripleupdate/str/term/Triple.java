package asp4j.test.direct.tripleupdate.str.term;

import asp4j.lang.Term;
import asp4j.mapping.ParseUtils;
import asp4j.mapping.direct.CanAsTerm;
import asp4j.mapping.direct.CanInitFromTerm;
import java.text.ParseException;
import java.util.Objects;
import org.junit.Assert;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck May 30, 2013
 */
public class Triple implements CanAsTerm, CanInitFromTerm {

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
        this.statement = null;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
        this.statement = null;
    }

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
        return "TypedTriple{" + "subject=" + subject + ", predicate=" + predicate + ", object=" + object + '}';
    }

    @Override
    public Term asTerm() {
        StringBuilder sb = new StringBuilder();
        sb.append("t(").append(subject).append(",").append(predicate).append(",").append(object).append(")");
        Term term = null;
        try {
            term = ParseUtils.parseTerm(sb.toString());
        } catch (ParseException e) {            
            Assert.fail();
        }
        return term;
    }

    @Override
    public void init(Term term) {
        this.subject=term.getArg(0).symbol();
        this.predicate=term.getArg(1).symbol();
        this.object=term.getArg(2).symbol();
    }

    @Override
    public String symbol() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
