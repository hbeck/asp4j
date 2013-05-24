package asp4j.lang.answerset;

import com.google.common.base.Predicate;
import java.util.Set;

/**
 *
 * @author hbeck
 * date Apr 14, 2013
 */
public interface AnswerSet<T> {
    
    Set<T> atoms();
    
    Set<T> filter(Predicate<T> predicate);
    
//    <T extends SolverOutput> Collection<T> filter(Class<T> clazz) throws Exception;
//    Collection<SolverOutput> filter(Class<? extends SolverOutput> ... clazzes) throws Exception;
//    Collection<SolverOutput> filter(Collection<Class<? extends SolverOutput>> clazzes) throws Exception;

}
