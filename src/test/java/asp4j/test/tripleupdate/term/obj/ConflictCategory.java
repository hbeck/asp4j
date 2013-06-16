package asp4j.test.tripleupdate.term.obj;

import asp4j.mapping.annotations.DefPredicateSymbols;
import asp4j.mapping.annotations.MapAs;

/**
 *
 * @author hbeck Jun 8, 2013
 */
@DefPredicateSymbols
public enum ConflictCategory {
    
    //use default name for conflict
    conflict,
    //use explicit renaming for skos_conflict
    @MapAs("skos_conflict") skos

}
