package asp4j.solver.query.impl;

import asp4j.solver.query.BooleanQuery;


/**
 *
 * @author hbeck Jun 16, 2013
 */
public class BooleanQueryImpl implements BooleanQuery {
    
    protected String queryString;

    public BooleanQueryImpl(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String asString() {
        return queryString;
    }

}
