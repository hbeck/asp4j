package asp4j.test.tripleupdate.term.obj;

import asp4j.mapping.annotations.DefEnumAtoms;

/**
 *
 * @author hbeck Jun 8, 2013
 */
@DefEnumAtoms
public enum ConflictCategory {
    
    conflict,
    //TODO rename to skos, use @Map("some_conflict")
    skos_conflict

}
