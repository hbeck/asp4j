package asp4j.test.rdf;

import java.util.Collection;
import java.util.List;
import org.openrdf.model.Graph;
import org.openrdf.model.Statement;

/**
 *
 * @author hbeck
 * date May 25, 2013
 */
public interface LogicRequests {    
    
    List<Update> update(ConflictSpecification spec, UpdateRequest request) throws ConflictException;
    
    List<Conflict> getConflicts(ConflictSpecification spec, Collection<Statement> statements);    
    
    List<Graph> getDiagnoses(ConflictSpecification spec, Collection<Statement> statements, Conflict conflict);
    
    List<Update> getRepairs(ConflictSpecification spec, Collection<Statement> statements, Conflict conflict);
    
    List<Update> getRepairs(ConflictSpecification spec, Collection<Statement> statements);

}
