package asp4j.test.person.direct;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
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
        setId(atom.getArg(0));
    }

    @Override
    public String predicateName() {
        return "person";
    }

    @Override
    public Atom asAtom() {
        return new AtomImpl(predicateName(),getId());
    }

    @Override
    public String toString() {
        return predicateName().toUpperCase()+'[' + id + ']';
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
