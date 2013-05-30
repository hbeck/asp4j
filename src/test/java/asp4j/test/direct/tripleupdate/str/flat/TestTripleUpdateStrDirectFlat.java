package asp4j.test.direct.tripleupdate.str.flat;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.mapping.ParseUtils;
import asp4j.mapping.direct.CanAsAtom;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.FilterBinding;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 * Triple Update. Direct mapping via interfaces and Strings only. No terms (flat).
 *
 * @author hbeck date 2013-05-14
 */
public class TestTripleUpdateStrDirectFlat {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/triple-update-flat.lp";

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

    @Test
    public void test_lowlevel_dlv_common() throws Exception {
        test_lowlevel(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_lowlevel_clingo_common() throws Exception {
        test_lowlevel(new SolverClingo(), rulefile_common);
    }

    public void test_lowlevel(Solver externalSolver, String rulefile) throws Exception {

        /*
         * IN:
         * 
         * add(car,hasColor,blue).
         * add(hasColor,inverseOf,colorOf).
         * 
         * OUT:
         * 
         * {...
         *  add(car,hasColor,blue),
         *  add(hasColor,inverseOf,colorOf),
         *  add(blue,colorOf,car),           <-- inferred
         *  ...}
         */
        
        Program<Atom> program =new ProgramBuilder<Atom>()
                .add(new File(rulefile))
                .add(ParseUtils.parseAtom("add(car,hasColor,blue)"))
                .add(ParseUtils.parseAtom("add(hasColor,inverseOf,colorOf)"))
                .build();

        Set<Atom> cautiousConsequence = externalSolver.getConsequence(program, ReasoningMode.CAUTIOUS);
        Set<Atom> braveConsequence = externalSolver.getConsequence(program, ReasoningMode.BRAVE);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        assertTrue(cautiousConsequence.contains(ParseUtils.parseAtom("add(blue,colorOf,car)")));
        List<AnswerSet<Atom>> as = externalSolver.getAnswerSets(program);
        assertEquals(1, as.size());

    }

    public void test(Solver externalSolver, String rulefile) throws Exception {

        Statement car_hasColor_blue = statement("urn:car", "urn:hasColor", "urn:blue");
        Statement blue_colorOf_car = statement("urn:blue", "urn:colorOf", "urn:car");
        Statement hasColor_inverseOf_colorOf = statement("urn:hasColor", "urn:inverseOf", "urn:colorOf");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        
        Program<Object> program = new ProgramBuilder()
                .add(new File(rulefile))
                .add(new Addition(car_hasColor_blue))
                .add(new Addition(hasColor_inverseOf_colorOf))
                .build();                        

        FilterBinding binding = new FilterBinding().add(Addition.class);

        Set<Object> cautiousConsequence = solver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);
        Set<Object> braveConsequence = solver.getConsequence(program, binding, ReasoningMode.BRAVE);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        List<AnswerSet<Object>> as = solver.getAnswerSets(program, binding);
        assertEquals(1, as.size());
        assertTrue(as.get(0).atoms().contains(new Addition(blue_colorOf_car)));

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

        Statement hasColor_inverseOf_colorOf = statement("urn:hasColor", "urn:inverseOf", "urn:colorOf");
        Statement hasColor_type_single = statement("urn:hasColor", "urn:type", "urn:single");
        Statement car_hasColor_blue = statement("urn:car", "urn:hasColor", "urn:blue");
        Statement blue_colorOf_car = statement("urn:blue", "urn:colorOf", "urn:car");
        Statement red_colorOf_car = statement("urn:red", "urn:colorOf", "urn:car");
        Statement car_hasColor_red = statement("urn:car", "urn:hasColor", "urn:red");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);

        Program<CanAsAtom> program = new ProgramBuilder()
                .add(new File(rulefile))
                .add(new InDatabase(hasColor_inverseOf_colorOf))
                .add(new InDatabase(hasColor_type_single))
                .add(new InDatabase(car_hasColor_blue))
                .add(new InDatabase(blue_colorOf_car))
                .add(new Addition(red_colorOf_car))
                .build();

        FilterBinding binding = new FilterBinding().add(Conflict.class);

        Set<Object> cautiousConsequence = solver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);
        Set<Object> braveConsequence = solver.getConsequence(program, binding, ReasoningMode.BRAVE);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        Conflict expected = new Conflict();
        //confl(single_violation(car,hasColor,blue,red))
        expected.setType("single_violation");
        expected.setStatement1(car_hasColor_blue);
        expected.setStatement2(car_hasColor_red);
        List<AnswerSet<Object>> as = solver.getAnswerSets(program, binding);
        assertTrue(as.get(0).atoms().contains(expected));
        assertEquals(1, as.size());
        assertEquals(1, as.get(0).atoms().size());
        
        //
        binding = new FilterBinding().add(SomeConflict.class);
        Set<Object> cons = solver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);
        assertEquals(1, cons.size());
        assertEquals(new SomeConflict(),(SomeConflict)cons.iterator().next());        
        
    }

    private static Statement statement(String subject, String predicate, String object) {
        return new StatementImpl(new URIImpl(subject), new URIImpl(predicate), new URIImpl(object));
    }
}
