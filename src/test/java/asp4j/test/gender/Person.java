package asp4j.test.gender;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;

/**
 *
 * @author hbeck Jun 9, 2013
 */
@DefAtom("person")
public class Person {
    
    private String id;
    
    public Person() {
    }
    
    public Person(String id) {
        this.id=id;
    }
    
    @Arg(0)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id=id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
