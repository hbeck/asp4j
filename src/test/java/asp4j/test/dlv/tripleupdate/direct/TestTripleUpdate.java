package asp4j.test.dlv.tripleupdate.direct;

import asp4j.lang.Atom;
import asp4j.lang.AtomImpl;
import asp4j.lang.answerset.AnswerSets;
import asp4j.mapping.direct.CanAsAtom;
import asp4j.mapping.direct.CanInitFromAtom;
import asp4j.program.ProgramBuilder;
import asp4j.solver.Solver;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.FilterBinding;
import asp4j.solver.object.FilterBindingImpl;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
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
 * @author hbeck 
 * date 2013-05-14
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
        ProgramBuilder pbuilder = new ProgramBuilder();
        pbuilder.add(new File(rulefile))
                .add(AtomImpl.parse("add(car,hasColor,blue)"))
                .add(AtomImpl.parse("add(hasColor,inverseOf,colorOf)"));

        AnswerSets<Atom> as = solver.getAnswerSets(pbuilder.build());

        assertTrue(CollectionUtils.isEqualCollection(as.braveConsequence(), as.cautiousConsequence()));
        Set<Atom> cons = as.cautiousConsequence();
        assertTrue(cons.contains(AtomImpl.parse("add(blue,colorOf,car)")));
        assertEquals(1, as.asList().size());

    }

    @Test
    public void test() throws Exception {

        Statement car_hasColor_blue = statement("urn:car", "urn:hasColor", "urn:blue");
        Statement blue_colorOf_car = statement("urn:blue", "urn:colorOf", "urn:car");
        Statement hasColor_inverseOf_colorOf = statement("urn:hasColor", "urn:inverseOf", "urn:colorOf");

        ObjectSolver objectSolver = new ObjectSolverImpl(new SolverDLV());

        ProgramBuilder<CanAsAtom> builder = new ProgramBuilder();
        builder.add(new File(rulefile))
                .add(new Addition(car_hasColor_blue))
                .add(new Addition(hasColor_inverseOf_colorOf));

        FilterBinding binding = new FilterBindingImpl();
        binding.add(Addition.class);

        AnswerSets<Object> as = objectSolver.getAnswerSets(builder.build(), binding);

        assertTrue(CollectionUtils.isEqualCollection(as.braveConsequence(), as.cautiousConsequence()));
        assertTrue(as.asList().get(0).atoms().contains(new Addition(blue_colorOf_car)));
        assertEquals(1, as.asList().size());

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
        
        FilterBinding binding = new FilterBindingImpl();
        binding.add(Conflict.class);

        AnswerSets<Object> as = objectSolver.getAnswerSets(builder.build(), binding);

        assertTrue(CollectionUtils.isEqualCollection(as.braveConsequence(), as.cautiousConsequence()));
        Conflict expected = new Conflict();
        //confl(single_violation(car,hasColor,blue,red))
        expected.setType("single_violation");
        expected.setStatement1(car_hasColor_blue);
        expected.setStatement2(car_hasColor_red);
        assertTrue(as.asList().get(0).atoms().contains(expected));
        assertEquals(1, as.asList().size());
        assertEquals(1, as.asList().get(0).atoms().size());
    }

    private static Statement statement(String subject, String predicate, String object) {
        return new StatementImpl(new URIImpl(subject), new URIImpl(predicate), new URIImpl(object));
    }
}
