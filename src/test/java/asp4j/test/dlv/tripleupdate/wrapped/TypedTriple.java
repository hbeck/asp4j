package asp4j.test.dlv.tripleupdate.wrapped;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.mapping.direct.ObjectAtom;
import java.util.Objects;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck date May 14, 2013
 */
public abstract class TypedTriple implements ObjectAtom {

    protected Statement statement;

    public TypedTriple() {
    }

    public void initStatement(Statement statement) {
        this.statement = statement;
    }

    public final void init(Atom atom) {
        initStatement(statement(atom));
    }

    public TypedTriple(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public abstract String predicateName();

    @Override
    public Atom asAtom() {
        String subject = ((URI) statement.getSubject()).getLocalName();
        String predicate = statement.getPredicate().getLocalName();
        String object = ((URI) statement.getObject()).getLocalName();
        return new AtomImpl(predicateName(), new String[]{subject, predicate, object});
    }

    protected Statement statement(Atom atom) {
        URI subject = new URIImpl("urn:" + atom.getArg(0));
        URI predicate = new URIImpl("urn:" + atom.getArg(1));
        URI object = new URIImpl("urn:" + atom.getArg(2));
        return new StatementImpl(subject, predicate, object);
    }

    @Override
    public String toString() {
        return predicateName() + ": " + statement.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.statement);
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
        final TypedTriple other = (TypedTriple) obj;
        if (!Objects.equals(this.statement, other.statement)) {
            return false;
        }
        return true;
    }
}
