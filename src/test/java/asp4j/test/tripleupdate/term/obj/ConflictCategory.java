package asp4j.test.tripleupdate.term.obj;

import asp4j.mapping.annotations.DefEnumAtoms;
import asp4j.mapping.annotations.MapWith;

/**
 *
 * @author hbeck Jun 8, 2013
 */
@DefEnumAtoms
public enum ConflictCategory {
    
    //use default name for conflict
    conflict,
    //use explicit renaming for skos_conflict
    @MapWith("skos_conflict")
    skos

}
