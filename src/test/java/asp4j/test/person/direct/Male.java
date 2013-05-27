package asp4j.test.person.direct;

import java.util.Objects;

/**
 *
 * @author hbeck 
 * date May 20, 2013
 */
public class Male extends Person {
    
    public Male() {}
    
    public Male(String id) {
        super(id);
    }

    @Override
    public String predicateName() {
        return "male";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Male other = (Male) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }    
    
}
