package asp4j.test.reach.v1;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.Filter;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author hbeck Jun 9, 2013
 */
public class TestReach {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/reach.lp";

    @Test
    public void test_dlv() throws Exception {
        test(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_clingo() throws Exception {
        test(new SolverClingo(), rulefile_common);
    }

    public void test(Solver externalSolver, String rulefile) throws Exception {

        /*
         * reach(X,Y) :- edge(X,Y), not blocked(X).
         * reach(X,Z) :- reach(X,Y), reach(Y,Z).
         * 
         * //1
         * edge(node(a),node(b)). edge(node(b),node(c)). edge(node(c),node(d)).
         * 
         * //2
         * blocked(node(c)).
         * 
         * //3
         * edge(node(b),node(d)).
         */

        Edge edge_ab = new Edge("a", "b");
        Edge edge_bc = new Edge("b", "c");
        Edge edge_cd = new Edge("c", "d");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(edge_ab).add(edge_bc).add(edge_cd)
                .build();

        Filter filter = new Filter().add(Reach.class);

        //1 
        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, filter);

        assertEquals(1, answerSets.size());
        Set<Object> as = answerSets.get(0).atoms();
        String[] exp = new String[]{"ab", "bc", "cd", "ac", "ad", "bd"};
        for (String s : exp) {
            assertTrue(as.contains(reach(s)));
        }
        assertEquals(exp.length, as.size());
        Set<Object> cautiousCons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        Set<Object> braveCons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        assertTrue(CollectionUtils.isEqualCollection(cautiousCons, braveCons));
        assertTrue(CollectionUtils.isEqualCollection(cautiousCons, as));

        //2
        program = new ProgramBuilder<>(program).add(new Blocked("c")).build();
        answerSets = solver.getAnswerSets(program, filter);

        assertEquals(1, answerSets.size());
        as = answerSets.get(0).atoms();
        exp = new String[]{"ab", "bc", "ac"};
        for (String s : exp) {
            assertTrue(as.contains(reach(s)));
        }
        assertEquals(exp.length, as.size());
        cautiousCons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        braveCons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        assertTrue(CollectionUtils.isEqualCollection(cautiousCons, braveCons));
        assertTrue(CollectionUtils.isEqualCollection(cautiousCons, as));

        //3
        program = new ProgramBuilder<>(program).add(new Edge("b","d")).build();
        answerSets = solver.getAnswerSets(program, filter);

        assertEquals(1, answerSets.size());
        as = answerSets.get(0).atoms();
        exp = new String[]{"ab", "bc", "ac", "bd", "ad"};
        for (String s : exp) {
            assertTrue(as.contains(reach(s)));
        }
        assertEquals(exp.length, as.size());
        cautiousCons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        braveCons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        assertTrue(CollectionUtils.isEqualCollection(cautiousCons, braveCons));
        assertTrue(CollectionUtils.isEqualCollection(cautiousCons, as));

    }

    private Reach reach(String s) {
        return new Reach(new Node("" + s.charAt(0)), new Node("" + s.charAt(1)));
    }
}
