package asp4j.test.rdf;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author hbeck
 * date May 25, 2013
 */
public class ConflictException extends Exception {
    
    private final Collection<Conflict> conflicts;
    
    public ConflictException(Conflict conflict) {
        conflicts=Arrays.asList(conflict);
    }
    
    public ConflictException(Collection<Conflict> conflicts) {
        this.conflicts=conflicts;
    }

    public Collection<Conflict> getConflicts() {
        return conflicts;
    }    

}
