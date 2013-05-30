package asp4j.test.annotated.tripleupdate.term.str;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;

/**
 *
 * @author hbeck May 30, 2013
 */
@DefAtom("add")
public class Addition {
    
    private Triple triple;

    public Addition() {
    }

    public Addition(Triple triple) {
        this.triple=triple;
    }
    
    @Arg(0)
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
        if (!Objects.equals(this.triple, other.triple)) {
            return false;
        }
        return true;
    }    

    @Override
    public String toString() {
        return "Addition{" + "triple=" + triple + '}';
    }    

}
