package asp4j.lang.answerset;

import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck
 * date May 19, 2013
 */
public interface AnswerSets<T> {
    
    Set<T> cautiousConsequence();
    Set<T> braveConsequence();
    List<AnswerSet<T>> asList();

}
