package asp4j.solver.query.impl;

import asp4j.solver.query.TupleQuery;

/**
 *
 * @author hbeck Jun 16, 2013
 */
public class TupleQueryImpl implements TupleQuery {
    
    protected String queryString;

    public TupleQueryImpl(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String asString() {
        return queryString;
    }
    

}
