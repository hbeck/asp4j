package asp4j.mapping.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author hbeck date Apr 31, 2013
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DefConstant {

    /**
     *
     * @return predicate symbol
     */
    String value();
}
