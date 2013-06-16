package asp4j.test.gender.v1;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.lang.Constant;
import asp4j.lang.ConstantImpl;
import asp4j.lang.Term;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import asp4j.solver.SolverClingo;
import asp4j.solver.SolverDLV;
import asp4j.solver.object.Filter;
import asp4j.solver.object.ObjectSolver;
import asp4j.solver.object.ObjectSolverImpl;
import asp4j.solver.object.query.ObjectBooleanQuery;
import asp4j.solver.object.query.ObjectBooleanQueryBuilder;
import asp4j.solver.object.query.ObjectTupleBinding;
import asp4j.solver.object.query.ObjectTupleQuery;
import asp4j.solver.object.query.ObjectTupleQueryBuilder;
import asp4j.solver.object.query.ObjectTupleQueryResult;
import asp4j.solver.object.query.impl.ObjectBooleanQueryImpl;
import asp4j.solver.object.query.impl.ObjectTupleQueryImpl;
import asp4j.solver.query.BooleanQuery;
import asp4j.solver.query.TupleBinding;
import asp4j.solver.query.TupleQuery;
import asp4j.solver.query.TupleQueryResult;
import asp4j.solver.query.impl.BooleanQueryImpl;
import asp4j.solver.query.impl.TupleQueryImpl;
import asp4j.util.ParseUtils;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author hbeck Jun 9, 2013
 */
public class TestGender {

    private final String rulefile_common = System.getProperty("user.dir") + "/src/test/common/gender.lp";
    private final String rulefile_dlv = System.getProperty("user.dir") + "/src/test/dlv/gender.lp";
    private final String rulefile_clingo = System.getProperty("user.dir") + "/src/test/clingo/gender.lp";

    @Test
    public void test1() throws Exception {
        test(new SolverDLV(), rulefile_dlv);
        test(new SolverClingo(), rulefile_clingo);
        test(new SolverDLV(), rulefile_common);
        test(new SolverClingo(), rulefile_common);
    }

    @Test
    public void test_dlv() throws Exception {
        test_answersets(new SolverDLV(), rulefile_dlv);
        test_cautiouscons(new SolverDLV(), rulefile_dlv);
        test_bravecons(new SolverDLV(), rulefile_dlv);
    }

    @Test
    public void test_clingo() throws Exception {
        test_answersets(new SolverClingo(), rulefile_clingo);
        test_cautiouscons(new SolverClingo(), rulefile_clingo);
        test_bravecons(new SolverClingo(), rulefile_clingo);
    }

    @Test
    public void test_dlv_common() throws Exception {
        test_answersets(new SolverDLV(), rulefile_common);
        test_cautiouscons(new SolverDLV(), rulefile_common);
        test_bravecons(new SolverDLV(), rulefile_common);
    }

    @Test
    public void test_clingo_common() throws Exception {
        test_answersets(new SolverClingo(), rulefile_common);
        test_cautiouscons(new SolverClingo(), rulefile_common);
        test_bravecons(new SolverClingo(), rulefile_common);
    }

    //@Test TODO
    public void test_query_1_dlv() throws Exception {
        test_query1(new SolverDLV(), rulefile_dlv);
        test_query1(new SolverDLV(), rulefile_common);
    }

    //@Test TODO
    public void test_query_2_dlv() throws Exception {
        test_query2(new SolverDLV(), rulefile_dlv);
        test_query2(new SolverDLV(), rulefile_common);
    }

    //@Test TODO
    public void test_query_7_dlv() throws Exception {
        test_query7(new SolverDLV(), rulefile_dlv);
        test_query7(new SolverDLV(), rulefile_common);
    }

    //@Test TODO
    public void test_query_16_dlv() throws Exception {
        test_query16(new SolverDLV(), rulefile_dlv);
        test_query16(new SolverDLV(), rulefile_common);
    }

    //TODO clingo first
    
    //TODO ...then other test cases
    
    /*
     * RULES (common; dlv and clingo similar)
     * 
     * gender(X,female) :- person(X), not gender(X,male).
     * gender(X,male)   :- person(X), not gender(X,female).
     * 
     * IN:
     * person(id42).
     * 
     * OUT:
     * 
     * {gender(id42,female). person(id42).}
     * {gender(id42,male). person(id42).}
     */
    public void test(Solver externalSolver, String rulefile) throws Exception {
        Person person = new Person("id42");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder().add(new File(rulefile)).add(person).build();
        Filter filter = new Filter().add(PersonGender.class);

        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program, filter);

        // ==> answerSets.size() == 2
        assertEquals(2, answerSets.size());

        Set<Object> cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS);

        // ==> cautiousConsequence.size() == 1
        assertEquals(1, cautiousConsequence.size());
        // ==> cautiousConsequence.contains(person);
        assertTrue(cautiousConsequence.contains(person));

        cautiousConsequence = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);

        // ==> cautiousConsequence.isEmpty()
        assertTrue(cautiousConsequence.isEmpty());

        Set<Object> braveConsequence = solver.getConsequence(program, ReasoningMode.BRAVE, filter);

        // ==> braveConsequence.size() == 2
        assertEquals(2, braveConsequence.size());
        // ==> braveConsequence.contains(new PersonGender("id42", Gender.FEMALE))
        assertTrue(braveConsequence.contains(new PersonGender("id42", Gender.FEMALE)));
        // ==> braveConsequence.contains(new PersonGender("id42", Gender.MALE))
        assertTrue(braveConsequence.contains(new PersonGender("id42", Gender.MALE)));

    }

    public void test_answersets(Solver externalSolver, String rulefile) throws Exception {

        Person person = new Person("id42");
        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        //

        List<AnswerSet<Object>> answerSets = solver.getAnswerSets(program);

        assertEquals(2, answerSets.size());
        Set<Object> as0 = answerSets.get(0).atoms();
        Set<Object> as1 = answerSets.get(1).atoms();
        assertTrue(CollectionUtils.isEqualCollection(as0, as1));
        assertEquals(1, as0.size());
        assertEquals(person, as0.iterator().next());

        //

        Filter filter = new Filter().add(PersonGender.class);
        answerSets = solver.getAnswerSets(program, filter);

        PersonGender female = new PersonGender("id42", Gender.FEMALE);
        PersonGender male = new PersonGender("id42", Gender.MALE);

        assertEquals(2, answerSets.size());
        as0 = answerSets.get(0).atoms();
        as1 = answerSets.get(1).atoms();
        assertFalse(CollectionUtils.isEqualCollection(as0, as1));
        assertEquals(1, as0.size());
        assertEquals(1, as1.size());
        Object obj0 = as0.iterator().next();
        Object obj1 = as1.iterator().next();
        if (obj0.equals(male)) {
            assertEquals(obj1, female);
        } else {
            assertEquals(obj1, male);
            assertEquals(obj0, female);
        }

        //

        filter.add(Person.class);
        answerSets = solver.getAnswerSets(program, filter);
        as0 = answerSets.get(0).atoms();
        as1 = answerSets.get(1).atoms();
        assertFalse(CollectionUtils.isEqualCollection(as0, as1));
        assertTrue(as0.contains(person));
        assertTrue(as1.contains(person));
        assertEquals(2, as0.size());
        assertEquals(2, as1.size());

    }

    public void test_cautiouscons(Solver externalSolver, String rulefile) throws Exception {

        Person person = new Person("id42");
        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        //

        Set<Object> cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS);

        assertEquals(1, cons.size());
        assertEquals(person, cons.iterator().next());

        //

        Filter filter = new Filter().add(PersonGender.class);
        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertTrue(cons.isEmpty());

        //

        filter.add(Person.class);
        cons = solver.getConsequence(program, ReasoningMode.CAUTIOUS, filter);
        assertEquals(1, cons.size());
        assertEquals(person, cons.iterator().next());

    }

    public void test_bravecons(Solver externalSolver, String rulefile) throws Exception {

        Person person = new Person("id42");
        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        //

        Set<Object> cons = solver.getConsequence(program, ReasoningMode.BRAVE);

        assertEquals(1, cons.size());
        assertEquals(person, cons.iterator().next());

        //

        Filter filter = new Filter().add(PersonGender.class);
        cons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);

        PersonGender female = new PersonGender("id42", Gender.FEMALE);
        PersonGender male = new PersonGender("id42", Gender.MALE);

        assertEquals(2, cons.size());
        Iterator<?> it = cons.iterator();
        Object first = it.next();
        if (first.equals(male)) {
            assertEquals(female, it.next());
        } else {
            assertEquals(female, first);
            assertEquals(male, it.next());
        }

        //

        filter.add(Person.class);
        cons = solver.getConsequence(program, ReasoningMode.BRAVE, filter);
        assertEquals(3, cons.size());
        assertTrue(cons.contains(person));
        assertTrue(cons.contains(female));
        assertTrue(cons.contains(male));

    }

    /*
     * RULES (common; dlv and clingo similar)
     * 
     * gender(X,female) :- person(X), not gender(X,male).
     * gender(X,male)   :- person(X), not gender(X,female).
     * 
     * IN:
     * person(id42).
     * 
     * OUT:
     * 
     * {gender(id42,female). person(id42).}
     * {gender(id42,male). person(id42).}
     * 
     * QUERY 1:
     * person(id42)? --> cautiously TRUE / bravely TRUE
     * 
     */
    public void test_query1(Solver externalSolver, String rulefile) throws Exception {

        Atom personAtom = ParseUtils.parseAtom("person(id42)");
        Program<Atom> externalProgram = new ProgramBuilder<Atom>().add(new File(rulefile)).add(personAtom).build();

        boolean b;

        BooleanQuery query = new BooleanQueryImpl("person(id42)?");

        b = externalSolver.booleanQuery(externalProgram, query, ReasoningMode.CAUTIOUS);
        assertTrue(b);
        b = externalSolver.booleanQuery(externalProgram, query, ReasoningMode.BRAVE);
        assertTrue(b);

        //

        Person person = new Person("id42");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        b = solver.booleanQuery(program, query, ReasoningMode.CAUTIOUS);
        assertTrue(b);
        b = solver.booleanQuery(program, query, ReasoningMode.BRAVE);
        assertTrue(b);

        for (ObjectBooleanQuery obQuery :
             new ObjectBooleanQuery[]{
                    new ObjectBooleanQueryBuilder().add(person).build(),
                    new ObjectBooleanQueryImpl(person)}) {

            b = solver.booleanQuery(program, obQuery, ReasoningMode.CAUTIOUS);
            assertTrue(b);
            b = solver.booleanQuery(program, obQuery, ReasoningMode.BRAVE);
            assertTrue(b);

        }

    }

    /* 
     * QUERY 2:
     * person(X)? --> id42 / id42
     */
    public void test_query2(Solver externalSolver, String rulefile) throws Exception {

        Atom personAtom = ParseUtils.parseAtom("person(id42)");
        Program<Atom> externalProgram = new ProgramBuilder<Atom>().add(new File(rulefile)).add(personAtom).build();

        TupleQuery query = new TupleQueryImpl("person(X)?");

        TupleQueryResult result;

        result = externalSolver.tupleQuery(externalProgram, query, ReasoningMode.CAUTIOUS);
        test_query2_sub1(result);
        result = externalSolver.tupleQuery(externalProgram, query, ReasoningMode.BRAVE);
        test_query2_sub1(result);

        //

        Person person = new Person("id42");

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        result = solver.tupleQuery(program, query, ReasoningMode.CAUTIOUS);
        test_query2_sub1(result);
        result = solver.tupleQuery(program, query, ReasoningMode.BRAVE);
        test_query2_sub1(result);

        //

        ObjectTupleQuery otQuery = new ObjectTupleQueryBuilder().add(person).build();

        ObjectTupleQueryResult oresult = solver.tupleQuery(program, otQuery, ReasoningMode.CAUTIOUS);
        test_query2_sub2(oresult);

        oresult = solver.tupleQuery(program, otQuery, ReasoningMode.BRAVE);
        test_query2_sub2(oresult);

    }

    private void test_query2_sub1(TupleQueryResult result) {
        assertEquals(1, result.asList().size());
        Set<String> variables = result.getVariables();
        assertEquals(1, variables.size());
        assertTrue(variables.contains("X"));
        Iterator<TupleBinding> it = result.iterator();
        TupleBinding tbinding = it.next();
        Map<String, Term> map = tbinding.asMap();
        assertEquals(1, map.size());
        Constant id42term = (Constant) map.get("X");
        Constant expected = new ConstantImpl("id42");
        assertEquals(expected, id42term);
        Term boundTerm = tbinding.getBinding("X");
        if (!(boundTerm instanceof Constant)) {
            fail();
        }
        assertEquals(expected, (Constant) boundTerm);
    }

    private void test_query2_sub2(ObjectTupleQueryResult result) {
        TupleQueryResult tqresult = result.getTupleQueryResult();
        test_query2_sub1(tqresult);
        //
        assertEquals(1, result.asList().size());
        Iterator<ObjectTupleBinding> it = result.iterator();
        ObjectTupleBinding otbinding = it.next();
        assertEquals(1, otbinding.size());
        List<Object> list = otbinding.asList();
        assertEquals(1, list.size());
        Object object = list.get(0);
        if (!(object instanceof Person)) {
            fail();
        }
        Person personId42 = (Person) object;
        String id42 = personId42.getId();
        assertEquals("id42", id42);
    }

    /*
     * QUERY 7:
     * person(id42), gender(id42,female)? --> FALSE / TRUE
     */
    public void test_query7(Solver externalSolver, String rulefile) throws Exception {

        Atom personAtom = ParseUtils.parseAtom("person(id42)");
        Program<Atom> externalProgram = new ProgramBuilder<Atom>().add(new File(rulefile)).add(personAtom).build();

        boolean b;

        BooleanQuery query = new BooleanQueryImpl("person(id42), gender(id42,female)?");

        b = externalSolver.booleanQuery(externalProgram, query, ReasoningMode.CAUTIOUS);
        assertFalse(b);
        b = externalSolver.booleanQuery(externalProgram, query, ReasoningMode.BRAVE);
        assertTrue(b);

        //

        Person person = new Person("id42");
        PersonGender female = new PersonGender("id42", Gender.FEMALE);

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        b = solver.booleanQuery(program, query, ReasoningMode.CAUTIOUS);
        assertTrue(b);
        b = solver.booleanQuery(program, query, ReasoningMode.BRAVE);
        assertTrue(b);

        for (ObjectBooleanQuery obQuery :
             new ObjectBooleanQuery[]{
                    new ObjectBooleanQueryBuilder().add(person).add(female).build(),
                    new ObjectBooleanQueryImpl(Arrays.asList(person, female))}) {

            b = solver.booleanQuery(program, obQuery, ReasoningMode.CAUTIOUS);
            assertFalse(b);
            b = solver.booleanQuery(program, obQuery, ReasoningMode.BRAVE);
            assertTrue(b);

        }

    }

    /*
     * QUERY 16:
     * person(X), gender(Y,Z) ? --> -- / id42, id42, female; id42, id42, male
     */
    public void test_query16(Solver externalSolver, String rulefile) throws Exception {

        Atom personAtom = ParseUtils.parseAtom("person(id42)");
        Program<Atom> externalProgram = new ProgramBuilder<Atom>().add(new File(rulefile)).add(personAtom).build();

        //internal binding names for object-variant
        //use also here for utilizing uniform sub-routine test_query16_sub1_brave
        TupleQuery query = new TupleQueryImpl("person(X0), gender(X1,X2)?");

        TupleQueryResult result;

        result = externalSolver.tupleQuery(externalProgram, query, ReasoningMode.CAUTIOUS);
        assertTrue(result.isEmpty());
        result = externalSolver.tupleQuery(externalProgram, query, ReasoningMode.BRAVE);
        test_query16_sub1_brave(result);

        //

        //note: getters returning null => variables.
        Person person = new Person();
        PersonGender personGender = new PersonGender();

        ObjectSolver solver = new ObjectSolverImpl(externalSolver);
        Program<Object> program = new ProgramBuilder<>().add(new File(rulefile)).add(person).build();

        result = solver.tupleQuery(program, query, ReasoningMode.CAUTIOUS);
        test_query2_sub1(result);
        result = solver.tupleQuery(program, query, ReasoningMode.BRAVE);
        test_query2_sub1(result);

        for (ObjectTupleQuery otQuery : new ObjectTupleQuery[]{
                    new ObjectTupleQueryBuilder().add(person).add(personGender).build(),
                    new ObjectTupleQueryImpl(Arrays.asList(person, personGender))}) {

            ObjectTupleQueryResult oresult = solver.tupleQuery(program, otQuery, ReasoningMode.CAUTIOUS);
            assertTrue(oresult.isEmpty());

            oresult = solver.tupleQuery(program, otQuery, ReasoningMode.BRAVE);
            test_query16_sub2_brave(oresult);
        }

    }

    // person(X), gender(Y,Z) ? --> -- / id42, id42, female; id42, id42, male
    private void test_query16_sub1_brave(TupleQueryResult result) {
        List<TupleBinding> tblist = result.asList();
        assertEquals(2, tblist.size());
        Set<String> variables = result.getVariables();
        assertEquals(3, variables.size());
        for (String s : new String[]{"X0", "X1", "X2"}) {
            assertTrue(variables.contains(s));
        }

        Constant id42constant = new ConstantImpl("id42");
        boolean checkFemale = false;
        boolean checkMale = false;
        for (int i = 0; i <= 1; i++) {
            TupleBinding tb = tblist.get(i);
            Map<String, Term> map = tb.asMap();
            assertEquals(3, map.size());
            //
            Constant x0 = (Constant) map.get("X0");
            assertEquals(id42constant, x0);
            Term boundX0 = tb.getBinding("X0");
            if (!(boundX0 instanceof Constant)) {
                fail();
            }
            assertEquals(id42constant, (Constant) boundX0);
            //
            Constant x1 = (Constant) map.get("X1");
            assertEquals(id42constant, x1);
            Term boundX1 = tb.getBinding("X1");
            if (!(boundX1 instanceof Constant)) {
                fail();
            }
            assertEquals(id42constant, (Constant) boundX1);
            //
            Constant x2 = (Constant) map.get("X2");
            Term boundX2 = tb.getBinding("X2");
            if (!(boundX2 instanceof Constant)) {
                fail();
            }
            String symbol = x2.symbol();
            Constant female = new ConstantImpl("female");
            Constant male = new ConstantImpl("male");
            if ("female".equals(symbol)) {
                assertEquals(female, x2);
                assertEquals(female, boundX2);
                checkFemale = true;
            } else if ("mail".equals(symbol)) {
                assertEquals(male, x2);
                assertEquals(male, boundX2);
                checkMale = true;
            } else {
                fail();
            }
        }
        assertTrue(checkFemale);
        assertTrue(checkMale);

    }

    private void test_query16_sub2_brave(ObjectTupleQueryResult result) {
        TupleQueryResult tqresult = result.getTupleQueryResult();
        test_query16_sub1_brave(tqresult);
        //
        assertEquals(1, result.asList().size());
        Iterator<ObjectTupleBinding> it = result.iterator();
        ObjectTupleBinding otbinding = it.next();
        assertEquals(1, otbinding.size());
        List<Object> list = otbinding.asList();
        assertEquals(1, list.size());
        Object object = list.get(0);
        if (!(object instanceof Person)) {
            fail();
        }
        Person personId42 = (Person) object;
        String id42 = personId42.getId();
        assertEquals("id42", id42);
    }
    /*    
     * QUERY 3:
     * gender(id42,female)? --> FALSE / TRUE
     * 
     * QUERY 4:
     * gender(id42,X)? --> -- / female; male
     * 
     * QUERY 5:
     * gender(X,female)? --> -- / id42
     * 
     * QUERY 6:
     * gender(X,Y)? --> -- / id42, female; id42, male
     * 
     * QUERY 8:
     * person(id42), gender(id42,female)? --> FALSE / FALSE
     * 
     * QUERY 9:
     * person(X), gender(X,female) ? --> -- / id42
     * 
     * QUERY 10:
     * person(X), gender(Y,female) ? --> -- / id42, id42
     * 
     * QUERY 11:
     * person(id42), gender(id42,X) ? --> -- / female; male
     * 
     * QUERY 12:
     * person(X), gender(X,X) ? --> -- / --
     * 
     * QUERY 13:
     * person(Y), gender(X,X) ? --> -- / --
     * 
     * QUERY 14:
     * person(X), gender(Y,X) ? --> -- / --
     * 
     * QUERY 15:
     * person(X), gender(X,Y) ? --> -- / id42, female; id42, male
     * 
     * 
     */
}