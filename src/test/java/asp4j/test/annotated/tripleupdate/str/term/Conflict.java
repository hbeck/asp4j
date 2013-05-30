package asp4j.test.annotated.tripleupdate.str.term;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;

/**
 *
 * @author hbeck May 30, 2013
 */
@DefAtom("confl")
public class Conflict {

    private String type;
    private Triple t1;
    private Triple t2;

    public Triple[] getConflictPair() {
        return new Triple[]{t1,t2};
    }

    @Arg(0)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Arg(1)
    public Triple getT1() {
        return t1;
    }

    public void setT1(Triple t1) {
        this.t1 = t1;
    }

    @Arg(2)
    public Triple getT2() {
        return t2;
    }

    public void setT2(Triple t2) {
        this.t2 = t2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.type);
        hash = 79 * hash + Objects.hashCode(this.t1);
        hash = 79 * hash + Objects.hashCode(this.t2);
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
        if (!Objects.equals(this.t1, other.t1)) {
            return false;
        }
        if (!Objects.equals(this.t2, other.t2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Conflict{" + "type=" + type + ", t1=" + t1 + ", t2=" + t2 + '}';
    }    

}
