package asp4j.test.rdf;

import java.util.Collection;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck May 25, 2013
 */
public interface Update {

    Collection<Statement> getAdditions();

    Collection<Statement> getDeletions();
}
