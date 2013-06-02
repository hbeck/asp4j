package asp4j.test.direct.person;

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
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hbeck date 2013-05-20
 */
public class TestPersonDirect {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/person.lp";
    private final String rulefile_dlv = System.getProperty("user.dir") + "/src/test/dlv/person.lp";
    private final String rulefile_clingo = System.getProperty("user.dir") + "/src/test/clingo/person.lp";

    @Test
    public void test_dlv() throws Exception {
        test(new SolverDLV(), rulefile_dlv);
    }

    @Test
    public void test_clingo() throws Exception {
        test(new SolverClingo(), rulefile_clingo);
    }

    @Test
    public void test_dlv_common() throws Exception {
        test(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_clingo_common() throws Exception {
        test(new SolverClingo(), rulefile_common);
    }

    public void test(Solver externalSolver, String rulefile) throws Exception {

        /*
         * (dlv) RULES:
         * male(X) v female(X) :- person(X).
         * 
         * IN:
         * person(id42).
         * 
         * OUT:
         * 
         * {male(id42). person(id42).}
         * {female(id42). person(id42).}
         */

        String id42 = "id42";
        Person person = new Person(id42);
        Male male = new Male(id42);
        Female female = new Female(id42);

        ObjectSolver objectSolver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();
        Binding binding = new Binding().add(Male.class).add(Female.class).add(Person.class);
        
        Filter filter = new Filter().add(Male.class).add(Female.class);

        Set<Object> cautiousConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.CAUTIOUS, filter);
        assertTrue(cautiousConsequence.isEmpty());
        
        Set<Object> braveConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.BRAVE, filter);
        assertEquals(2, braveConsequence.size());
        assertTrue(braveConsequence.contains(male));
        assertTrue(braveConsequence.contains(female));

        filter.add(Person.class);

        cautiousConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.CAUTIOUS, filter);
        assertTrue(cautiousConsequence.contains(person));
        assertEquals(1,cautiousConsequence.size());
        
        braveConsequence = objectSolver.getConsequence(program, binding, ReasoningMode.BRAVE, filter);
        assertTrue(braveConsequence.contains(person));
        assertTrue(braveConsequence.contains(male));
        assertTrue(braveConsequence.contains(female));
        assertEquals(3,braveConsequence.size());

    }
}
