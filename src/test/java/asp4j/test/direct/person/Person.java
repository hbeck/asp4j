package asp4j.test.direct.person;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.lang.Term;
import asp4j.mapping.direct.CanAsAtom;
import asp4j.mapping.direct.CanInitFromAtom;
import java.util.Objects;

/**
 *
 * @author hbeck
 * date May 20, 2013
 */
public class Person implements CanInitFromAtom, CanAsAtom {
    
    private String id;
    
    public Person() {
    }

    public Person(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }    

    @Override
    public void init(Atom atom) {
        Constant arg = (Constant)atom.getArg(0);
        setId(arg.symbol());
    }

    @Override
    public String symbol() {
        return "person";
    }

    @Override
    public Atom asAtom() {
        Constant constId = new ConstantImpl(id);
        return new AtomImpl(symbol(),constId);
    }

    @Override
    public String toString() {
        return symbol().toUpperCase()+'[' + id + ']';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Person other = (Person) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }    
    
}
