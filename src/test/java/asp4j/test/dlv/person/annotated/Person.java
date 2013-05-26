package asp4j.test.dlv.person.annotated;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.Atomname;
import java.util.Objects;

/**
 *
 * @author hbeck date May 23, 2013
 */
@Atomname("person")
public class Person {

    protected String id;

    public Person() {
    }

    public Person(String id) {
        this.id = id;
    }

    @Arg(0)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        if (this.id==null) {
            if (other.id!=null) {
                return false;
            }
            return true;
        }
        if (other.id==null) {
            return false;
        }
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + '}';
    }    
    
}
