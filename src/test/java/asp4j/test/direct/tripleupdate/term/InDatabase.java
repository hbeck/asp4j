package asp4j.test.direct.tripleupdate.term;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.Term;
import asp4j.mapping.direct.CanAsAtom;
import asp4j.mapping.direct.CanInitFromAtom;
import java.util.Objects;

/**
 *
 * @author hbeck May 30, 2013
 */
public class InDatabase implements CanAsAtom, CanInitFromAtom {

    private Triple triple;

    public InDatabase() {
    }

    public InDatabase(Triple triple) {
        this.triple = triple;
    }

    public Triple getTriple() {
        return triple;
    }

    public void setTriple(Triple triple) {
        this.triple = triple;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.triple);
        hash = 97 * hash + Objects.hashCode("db");
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
        final InDatabase other = (InDatabase) obj;
        if (!Objects.equals(this.triple, other.triple)) {
            return false;
        }
        return true;
    }

    @Override
    public Atom asAtom() {
        return new AtomImpl(symbol(), new Term[]{triple.asTerm()});
    }

    @Override
    public void init(Atom atom) {
        Triple t = new Triple();
        t.init(atom.getArg(0));
        this.triple = t;
    }

    @Override
    public String symbol() {
        return "db";
    }
}
