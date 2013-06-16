package asp4j.solver.call;

import asp4j.lang.Atom;
import asp4j.program.Program;
import asp4j.solver.query.Query;
import java.util.Objects;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public abstract class QuerySolverCall extends SolverCallBase {

    protected Query query;

    public QuerySolverCall(Program<Atom> program, Query query) {
        super(program);
        this.query = query;
    }    

    @Override
    protected String inputString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.inputString());
        sb.append(" ").append(query.asString());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + super.hashCode();
        hash = 37 * hash + Objects.hashCode(this.query);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuerySolverCall other = (QuerySolverCall) obj;
        if (!Objects.equals(this.query, other.query)) {
            return false;
        }
        return true;
    }    
    
}
