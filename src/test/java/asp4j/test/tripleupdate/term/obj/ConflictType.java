package asp4j.test.tripleupdate.term.obj;

import asp4j.mapping.annotations.DefConstantSymbols;
import asp4j.mapping.annotations.MapAs;

/**
 *
 * @author hbeck Jun 8, 2013
 */
@DefConstantSymbols
public enum ConflictType {
    
    @MapAs("single_violation") single_violation, //redundancy test
    @MapAs("narrower_broader") narrower_broader_clash

}
