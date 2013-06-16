package asp4j.solver.call;

import asp4j.lang.Atom;
import asp4j.program.Program;
import asp4j.solver.query.Query;

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
}
