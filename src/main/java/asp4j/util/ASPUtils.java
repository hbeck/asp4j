package asp4j.util;

import asp4j.mapping.OutputAtom;
import com.google.common.base.Predicate;

/**
 *
 * @author hbeck
 * date May 20, 2013
 */
public class ASPUtils {
    
    public static Predicate<OutputAtom> classFilter(final Class<? extends OutputAtom> clazz) {
        return new Predicate<OutputAtom>(){
            @Override
            public boolean apply(OutputAtom input) {
                return (input.getClass().equals(clazz));
            }
        };
    }

}
