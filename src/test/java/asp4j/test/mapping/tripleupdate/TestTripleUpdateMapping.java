package asp4j.test.mapping.tripleupdate;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.Binding;
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
import org.openrdf.model.Statement;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

/**
 *
 * Triple Update. Manual mapping ov involved objects via
 *
 * @author hbeck date June 1, 2013
 */
public class TestTripleUpdateMapping {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/triple-update-term-obj.lp";

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

        Statement car_hasColor_blue = statement("ex:car", "ex:hasColor", "ex:blue");
        Statement blue_colorOf_car = statement("ex:blue", "ex:colorOf", "ex:car");
        Statement hasColor_inverseOf_colorOf = statement("ex:hasColor", "owl:inverseOf", "ex:colorOf");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver); //Solver<Object>

        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(new Addition(car_hasColor_blue))
                .add(new Addition(hasColor_inverseOf_colorOf))
                .build();


        Binding binding = new Binding()                
                .add(new URIMapping())
                .add(new ValueMapping())
                .add(new StatementMapping())
                .add(new AdditionMapping()); //atom

        List<AnswerSet<Object>> as = solver.getAnswerSets(program, binding);

        assertEquals(1, as.size());
        Set<Object> atoms = as.get(0).atoms();
        assertEquals(3, atoms.size());
        assertTrue(atoms.contains(new Addition(blue_colorOf_car)));

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding);
        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));
        
        Filter filter = new Filter(Addition.class);
        
        cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
        braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding, filter);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));


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

        Statement hasColor_inverseOf_colorOf = statement("ex:hasColor", "owl:inverseOf", "ex:colorOf");
        Statement hasColor_type_single = statement("ex:hasColor", "rdf:type", "ex:single");
        Statement car_hasColor_blue = statement("ex:car", "ex:hasColor", "ex:blue");
        Statement blue_colorOf_car = statement("ex:blue", "ex:colorOf", "ex:car");
        Statement red_colorOf_car = statement("ex:red", "ex:colorOf", "ex:car");
        Statement car_hasColor_red = statement("ex:car", "ex:hasColor", "ex:red");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);

        Program<Object> program = new ProgramBuilder<>()
                .add(new File(rulefile))
                .add(new InDatabase(hasColor_inverseOf_colorOf))
                .add(new InDatabase(hasColor_type_single))
                .add(new InDatabase(car_hasColor_blue))
                .add(new InDatabase(blue_colorOf_car))
                .add(new Addition(red_colorOf_car))
                .build();

        Binding binding = new Binding()                
                .add(new URIMapping())
                .add(new ValueMapping())
                .add(new StatementMapping())
                .add(new AdditionMapping()) //atom
                .add(new InDatabaseMapping()) //atom
                .add(new ConflictMapping()); //atom
        
        Filter filter = new Filter(Conflict.class);

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, filter);
        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding, filter);
        assertTrue(CollectionUtils.isEqualCollection(cautiousConsequence, braveConsequence));

        Conflict expected = new Conflict();
        //confl(single_violation,car,hasColor,blue,red))
        expected.setType("single_violation");
        expected.setStatement1(car_hasColor_blue);
        expected.setStatement2(car_hasColor_red);

        List<AnswerSet<Object>> as = solver.getAnswerSets(program, binding, filter);
        assertTrue(as.get(0).atoms().contains(expected));
        assertEquals(1, as.size());
        assertEquals(1, as.get(0).atoms().size());

        //
        binding.add(new SomeConflictMapping());
        Set<Object> cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding, new Filter(SomeConflict.class));
        assertEquals(1, cons.size());
        assertEquals(new SomeConflict(), (SomeConflict) cons.iterator().next());
    }

    private static Statement statement(String subject, String predicate, String object) {
        return new StatementImpl(new URIImpl(subject), new URIImpl(predicate), new URIImpl(object));
    }
}
