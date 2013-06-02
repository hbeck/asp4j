package asp4j.test.annotated.person;

import asp4j.lang.AnswerSet;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.Binding;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import java.io.File;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hbeck date May 20, 2013
 */
public class TestPersonAnnotated {

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

        Person person = new Person("id42");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);

        //"person.lp"
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();
        Binding binding = new Binding().addFilter(Male.class).addFilter(Female.class);
        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, binding);

        // ==> answerSets.size() == 2
        assertEquals(2, answerSets.size());

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding);
        // ==> cautiousConsequence.isEmpty()
        assertTrue(cautiousConsequence.isEmpty());

        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding);
        // ==> braveConsequence.size() == 2
        // ==> braveConsequence.contains(new Female("id42"))
        // ==> braveConsequence.contains(new Male("id42"))
        assertEquals(2, braveConsequence.size());
        assertTrue(braveConsequence.contains(new Female("id42")));
        assertTrue(braveConsequence.contains(new Male("id42")));

        binding.addFilter(Person.class);

        cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, binding);
        // ==> cautiousConsequence.size() == 1
        // ==> cautiousConsequence.contains(person)

        assertEquals(1, cautiousConsequence.size());
        assertTrue(cautiousConsequence.contains(person));

        braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, binding);
        assertTrue(braveConsequence.contains(new Female("id42")));
        assertTrue(braveConsequence.contains(new Male("id42")));

    }
}
