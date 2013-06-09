package asp4j.test.gender;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;

/**
 *
 * @author hbeck Jun 9, 2013
 */
@DefAtom("gender")
public class PersonGender {    
    
    private String id;
    private Gender gender;
    
    public PersonGender() {
    }
    public PersonGender(String id, Gender gender) {
        this.id = id;
        this.gender=gender;
    }

    @Arg(0)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    @Arg(1)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + (this.gender != null ? this.gender.hashCode() : 0);
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
        final PersonGender other = (PersonGender) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.gender != other.gender) {
            return false;
        }
        return true;
    }    

}
