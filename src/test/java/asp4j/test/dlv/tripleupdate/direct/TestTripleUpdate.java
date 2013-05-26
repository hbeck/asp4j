package asp4j.test.dlv.tripleupdate.direct;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.mapping.direct.CanAsAtom;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.FilterBinding;
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
 *
 * @author hbeck date 2013-05-14
 */
public class TestTripleUpdate {

    private final String rulefile = System.getProperty("user.dir") + "/src/test/dlv/tripleupdate/triple-update.lp";

    @Test
    public void test_lowlevel() throws Exception {

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


        Solver solver = new SolverDLV();
        ProgramBuilder<Atom> builder = new ProgramBuilder();
        builder.add(new File(rulefile))
                .add(AtomImpl.parse("add(car,hasColor,blue)"))
                .add(AtomImpl.parse("add(hasColor,inverseOf,colorOf)"));

        Program<Atom> program = builder.build();

        Set<Atom> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS);
        Set<Atom> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        assertTrue(cautiousConsequence.contains(AtomImpl.parse("add(blue,colorOf,car)")));
        List<AnswerSet<Atom>> as = solver.getAnswerSets(builder.build());
        assertEquals(1, as.size());

    }

    @Test
    public void test() throws Exception {

        Statement car_hasColor_blue = statement("urn:car", "urn:hasColor", "urn:blue");
        Statement blue_colorOf_car = statement("urn:blue", "urn:colorOf", "urn:car");
        Statement hasColor_inverseOf_colorOf = statement("urn:hasColor", "urn:inverseOf", "urn:colorOf");

        ObjectSolver objectSolver = new ObjectSolverImpl(new SolverDLV());

        ProgramBuilder<Object> builder = new ProgramBuilder();
        builder.add(new File(rulefile))
                .add(new Addition(car_hasColor_blue))
                .add(new Addition(hasColor_inverseOf_colorOf));

        FilterBinding binding = new FilterBinding();
        binding.add(Addition.class);

        Program<Object> program = builder.build();

        Set<Object> cautiousConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);
        Set<Object> braveConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.BRAVE);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        List<AnswerSet<Object>> as = objectSolver.getAnswerSets(builder.build(), binding);
        assertEquals(1, as.size());
        assertTrue(as.get(0).atoms().contains(new Addition(blue_colorOf_car)));

    }

    @Test
    public void test_conflict() throws Exception {
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

        ObjectSolver objectSolver = new ObjectSolverImpl(new SolverDLV());

        ProgramBuilder<CanAsAtom> builder = new ProgramBuilder();
        builder.add(new File(rulefile))
                .add(new InDatabase(hasColor_inverseOf_colorOf))
                .add(new InDatabase(hasColor_type_single))
                .add(new InDatabase(car_hasColor_blue))
                .add(new InDatabase(blue_colorOf_car))
                .add(new Addition(red_colorOf_car));

        FilterBinding binding = new FilterBinding();
        binding.add(Conflict.class);
        
        Program<CanAsAtom> program = builder.build();

        Set<Object> cautiousConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.CAUTIOUS);
        Set<Object> braveConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.BRAVE);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        Conflict expected = new Conflict();
        //confl(single_violation(car,hasColor,blue,red))
        expected.setType("single_violation");
        expected.setStatement1(car_hasColor_blue);
        expected.setStatement2(car_hasColor_red);
        List<AnswerSet<Object>> as = objectSolver.getAnswerSets(builder.build(), binding);
        assertTrue(as.get(0).atoms().contains(expected));
        assertEquals(1, as.size());
        assertEquals(1, as.get(0).atoms().size());
    }

    private static Statement statement(String subject, String predicate, String object) {
        return new StatementImpl(new URIImpl(subject), new URIImpl(predicate), new URIImpl(object));
    }
}
