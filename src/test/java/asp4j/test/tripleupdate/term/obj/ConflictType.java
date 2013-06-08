package asp4j.test.tripleupdate.term.obj;

import asp4j.mapping.annotations.DefEnumConstants;
import asp4j.mapping.annotations.MapWith;

/**
 *
 * @author hbeck Jun 8, 2013
 */
@DefEnumConstants
public enum ConflictType {
    
    single_violation,
    @MapWith("narrower_broader")
    narrower_broader_clash

}
