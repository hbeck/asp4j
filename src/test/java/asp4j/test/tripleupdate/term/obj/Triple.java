package asp4j.test.tripleupdate.term.obj;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefTerm;
import java.util.Objects;
import org.openrdf.model.URI;

/**
 *
 * @author hbeck June 8, 2013
 */
@DefTerm("t")
public class Triple {

    protected URI subject;
    protected Predicate predicate;
    protected URI object;
    
    public Triple() {
    }

    public Triple(URI subject, Predicate predicate, URI object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    @Arg(0)
    public URI getSubject() {
        return subject;
    }

    public void setSubject(URI subject) {
        this.subject = subject;
    }

    @Arg(1)
    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    @Arg(2)
    public URI getObject() {
        return object;
    }

    public void setObject(URI object) {
        this.object = object;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.subject);
        hash = 53 * hash + Objects.hashCode(this.predicate);
        hash = 53 * hash + Objects.hashCode(this.object);
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
        final Triple other = (Triple) obj;
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.predicate, other.predicate)) {
            return false;
        }
        if (!Objects.equals(this.object, other.object)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Triple{" + "subject=" + subject + ", predicate=" + predicate + ", object=" + object + '}';
    }    
    
}
