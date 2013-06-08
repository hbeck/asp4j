package asp4j.test.tripleupdate.term.str;

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
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * Triple Update. Mapping via Annotations, using terms. Only strings for values.
 *
 * @author hbeck date May 30, 2013
 */
public class TestTripleUpdateAnnotatedTermStr {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/triple-update-term-str.lp";

    @Test
    public void test_dlv_common() throws Exception {
        test(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_clingo_common() throws Exception {
        test(new SolverClingo(), rulefile_common);
    }

    @Test
    public void test_conflict_dlv_common() throws Exception {
        test_conflict(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_conflict_clingo_common() throws Exception {
        test_conflict(new SolverClingo(), rulefile_common);
    }

    public void test(Solver externalSolver, String rulefile) throws Exception {

        Triple car_hasColor_blue = triple("urn:car", "urn:hasColor", "urn:blue");
        Triple blue_colorOf_car = triple("urn:blue", "urn:colorOf", "urn:car");
        Triple hasColor_inverseOf_colorOf = triple("urn:hasColor", "urn:inverseOf", "urn:colorOf");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver); //Solver<Object>

        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(new Addition(car_hasColor_blue))
                .add(new Addition(hasColor_inverseOf_colorOf))
                .build();

        List<AnswerSet<Object>> as = solver.getAnswerSets(program);

        assertEquals(1, as.size());
        assertTrue(as.get(0).atoms().contains(new Addition(blue_colorOf_car)));

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS);
        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));
        assertEquals(3,cautiousConsequence.size());

    }

    public void test_conflict(Solver externalSolver, String rulefile) throws Exception {
        /*
         * db(hasColor,inverseOf,colorOf).
         * db(hasColor,type,single).
         * db(car,hasColor,blue).
         * db(blue,colorOf,car).
         * add(red,colorOf,car).
         *
         * --> add(car,hasColor,red).
         * --> confl(single_violation(car,hasColor,blue,red))
         */

        Triple hasColor_inverseOf_colorOf = triple("urn:hasColor", "urn:inverseOf", "urn:colorOf");
        Triple hasColor_type_single = triple("urn:hasColor", "urn:type", "urn:single");
        Triple car_hasColor_blue = triple("urn:car", "urn:hasColor", "urn:blue");
        Triple blue_colorOf_car = triple("urn:blue", "urn:colorOf", "urn:car");
        Triple red_colorOf_car = triple("urn:red", "urn:colorOf", "urn:car");
        Triple car_hasColor_red = triple("urn:car", "urn:hasColor", "urn:red");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);

        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(new InDatabase(hasColor_inverseOf_colorOf))
                .add(new InDatabase(hasColor_type_single))
                .add(new InDatabase(car_hasColor_blue))
                .add(new InDatabase(blue_colorOf_car))
                .add(new Addition(red_colorOf_car))
                .build();

        Filter filter = new Filter(Conflict.class);

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        assertEquals(1,cautiousConsequence.size());
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        Conflict expected = new Conflict();
        //confl(single_violation,car,hasColor,blue,red))
        expected.setType("single_violation");
        expected.setT1(car_hasColor_blue);
        expected.setT2(car_hasColor_red);

        List<AnswerSet<Object>> as = solver.getAnswerSets(program, filter);
        assertTrue(as.get(0).atoms().contains(expected));
        assertEquals(1, as.size());
        assertEquals(1, as.get(0).atoms().size());
        assertEquals(expected,cautiousConsequence.iterator().next());

        //
        filter = new Filter(SomeConflict.class);
        Set<Object> cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertEquals(1, cons.size());
        assertEquals(new SomeConflict(), (SomeConflict) cons.iterator().next());
    }

    private static Triple triple(String subject, String predicate, String object) {
        return new Triple(new StatementImpl(new URIImpl(subject), new URIImpl(predicate), new URIImpl(object)));
    }
}
