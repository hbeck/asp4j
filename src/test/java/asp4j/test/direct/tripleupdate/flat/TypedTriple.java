package asp4j.test.direct.tripleupdate.flat;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.lang.Term;
import asp4j.lang.TermImpl;
import asp4j.mapping.direct.CanAsAtom;
import java.util.Objects;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * @author hbeck date May 14, 2013
 */
public abstract class TypedTriple implements CanAsAtom {

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

    public abstract String symbol();

    @Override
    public Atom asAtom() {
        String subject = ((URI) statement.getSubject()).getLocalName();
        String predicate = statement.getPredicate().getLocalName();
        String object = ((URI) statement.getObject()).getLocalName();
        Constant[] constants = new Constant[]{
            new ConstantImpl(subject),
            new ConstantImpl(predicate),
            new ConstantImpl(object)
        };
        return new AtomImpl(symbol(), constants);
    }

    protected Statement statement(Atom atom) {
        URI subject = new URIImpl("urn:" + atom.getArg(0));
        URI predicate = new URIImpl("urn:" + atom.getArg(1));
        URI object = new URIImpl("urn:" + atom.getArg(2));
        return new StatementImpl(subject, predicate, object);
    }

    @Override
    public String toString() {
        return symbol() + ": " + statement.toString();
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
