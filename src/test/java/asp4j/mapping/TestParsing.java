package asp4j.mapping;

import asp4j.lang.Atom;
import asp4j.lang.Constant;
import asp4j.lang.Term;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author hbeck May 30, 2013
 */
public class TestParsing {

    //
    // terms
    //
    @Test
    public void test_term_arity0() throws Exception {
        Term c = ParseUtils.parseTerm("c");
        check_term_arity0(c, "c");
    }

    //cs
    private void check_term_arity0(Term c, String cs) throws Exception {
        assertTrue(c instanceof Constant);
        assertEquals(0, c.arity());
        assertEquals(cs, c.symbol());
        assertEquals(null, c.getArg(-1));
        assertEquals(null, c.getArg(0));
        assertEquals(null, c.getArg(1));
    }

    @Test
    public void test_term_arity1() throws Exception {
        Term t = ParseUtils.parseTerm("t(c)");
        check_term_arity1(t, "t", "c");
    }

    //ts(cs)
    private void check_term_arity1(Term t, String ts, String cs) throws Exception {
        assertEquals(1, t.arity());
        assertEquals(ts, t.symbol());
        Term c = t.getArg(0);
        check_term_arity0(c, cs);
    }

    @Test
    public void test_term_arity2() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t(c0,c1)");
        check_term_arity2(t, "t", "c0", "c1");
        t = ParseUtils.parseTerm("t(c,c)");
        check_term_arity2(t, "t", "c", "c");
    }

    //ts(c0s,c1s)
    private void check_term_arity2(Term t, String ts, String c0s, String c1s) throws Exception {
        assertEquals(2, t.arity());
        assertEquals(ts, t.symbol());
        Term c = t.getArg(0);
        check_term_arity0(c, c0s);
        c = t.getArg(1);
        check_term_arity0(c, c1s);
    }

    @Test
    public void test_term_arity3() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t(c0,c1,c2)");
        check_term_arity3(t, "t", "c0", "c1", "c2");
        t = ParseUtils.parseTerm("t(c,c,c)");
        check_term_arity3(t, "t", "c", "c", "c");
    }

    //ts(c0s,c1s,c2s)
    private void check_term_arity3(Term t, String ts, String c0s, String c1s, String c2s) throws Exception {
        assertEquals(3, t.arity());
        assertEquals(ts, t.symbol());
        Term c = t.getArg(0);
        check_term_arity0(c, c0s);
        c = t.getArg(1);
        check_term_arity0(c, c1s);
        c = t.getArg(2);
        check_term_arity0(c, c2s);
    }

    @Test
    public void test_term_arity3_middle_nested() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t0(c0,t1(c1),c2)");
        check_term_arity3_middle_nested(t, "t0", "c0", "t1", "c1", "c2");
        t = ParseUtils.parseTerm("t(c,t(c),c)");
        check_term_arity3_middle_nested(t, "t", "c", "t", "c", "c");
    }

    //ts0(c0s,ts1(c1s),c2s)
    private void check_term_arity3_middle_nested(Term t, String ts0, String c0s, String t1s, String c1s, String c2s) throws Exception {
        assertEquals(3, t.arity());
        assertEquals(ts0, t.symbol());
        Term c0 = t.getArg(0);
        check_term_arity0(c0, c0s);
        Term t1 = t.getArg(1);
        check_term_arity1(t1, t1s, c1s);
        Term c1 = t.getArg(2);
        check_term_arity0(c1, c2s);
    }

    @Test
    public void test_term_arity1_nested() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t0(t1(c))");
        check_term_arity1_nested(t, "t0", "t1", "c");
        t = ParseUtils.parseTerm("t(t(c))");
        check_term_arity1_nested(t, "t", "t", "c");
    }

    //ts0(ts1(c))
    private void check_term_arity1_nested(Term t0, String t0s, String t1s, String cs) throws Exception {
        assertEquals(1, t0.arity());
        assertEquals(t0s, t0.symbol());
        Term t1 = t0.getArg(0);
        check_term_arity1(t1, t1s, cs);
    }

    @Test
    public void test_term_arity2_first_nested() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t0(t1(c0),c1)");
        check_term_arity2_first_nested(t, "t0", "t1", "c0", "c1");
        t = ParseUtils.parseTerm("t(t(c),c)");
        check_term_arity2_first_nested(t, "t", "t", "c", "c");
    }

    //t0(t1(c0),c1)
    private void check_term_arity2_first_nested(Term t0, String t0s, String t1s, String c0s, String c1s) throws Exception {
        assertEquals(2, t0.arity());
        assertEquals(t0s, t0.symbol());
        Term t1 = t0.getArg(0);
        check_term_arity1(t1, t1s, c0s);
        Term c1 = t0.getArg(1);
        check_term_arity0(c1, c1s);
    }

    @Test
    public void test_term_arity2_second_nested() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t0(c0,t1(c1))");
        check_term_arity2_second_nested(t, "t0", "c0", "t1", "c1");
        t = ParseUtils.parseTerm("t(c,t(c))");
        check_term_arity2_second_nested(t, "t", "c", "t", "c");
    }

    //t0(c0,t1(c1))
    private void check_term_arity2_second_nested(Term t0, String t0s, String c0s, String t1s, String c1s) throws Exception {
        assertEquals(2, t0.arity());
        assertEquals(t0s, t0.symbol());
        Term c0 = t0.getArg(0);
        check_term_arity0(c0, c0s);
        Term t1 = t0.getArg(1);
        check_term_arity1(t1, t1s, c1s);
    }

    @Test
    public void test_term_multiple_nesting() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t0(t1(t2(c)))");
        assertEquals(1, t.arity());
        assertEquals("t0", t.symbol());
        check_term_arity1_nested(t.getArg(0), "t1", "t2", "c");
        //
        t = ParseUtils.parseTerm("t0(t1(t2(t3(c))))");
        assertEquals(1, t.arity());
        assertEquals("t0", t.symbol());
        t = t.getArg(0);
        assertEquals(1, t.arity());
        assertEquals("t1", t.symbol());
        check_term_arity1_nested(t.getArg(0), "t2", "t3", "c");
    }

    @Test
    public void test_term_nesting_and_arities() throws Exception {
        Term t;
        t = ParseUtils.parseTerm("t(t0(c00,c01,c02),t1(c10,c11,c12),t2(c20,c21,c22))");
        assertEquals(3, t.arity());
        assertEquals("t", t.symbol());
        Term t0 = t.getArg(0);
        check_term_arity3(t0, "t0", "c00", "c01", "c02");
        Term t1 = t.getArg(1);
        check_term_arity3(t1, "t1", "c10", "c11", "c12");
        Term t2 = t.getArg(2);
        check_term_arity3(t2, "t2", "c20", "c21", "c22");
        //
        t = ParseUtils.parseTerm("t(t0(c00,t(c01),c02),t1(c10,t(c11),c12),t2(c20,t(c21),c22))");
        assertEquals(3, t.arity());
        assertEquals("t", t.symbol());
        t0 = t.getArg(0);
        check_term_arity3_middle_nested(t0, "t0", "c00", "t", "c01", "c02");
        t1 = t.getArg(1);
        check_term_arity3_middle_nested(t1, "t1", "c10", "t", "c11", "c12");
        t2 = t.getArg(2);
        check_term_arity3_middle_nested(t2, "t2", "c20", "t", "c21", "c22");
    }

    //
    // atoms
    //
    @Test
    public void test_atom_arity0() throws Exception {
        Atom p = ParseUtils.parseAtom("p");
        check_atom_arity0(p, "p");
        p = ParseUtils.parseAtom("p.");
        check_atom_arity0(p, "p");
    }

    //ps
    private void check_atom_arity0(Atom p, String ps) throws Exception {
        assertEquals(0, p.arity());
        assertEquals(ps, p.symbol());
        assertEquals(null, p.getArg(-1));
        assertEquals(null, p.getArg(0));
        assertEquals(null, p.getArg(1));
    }

    @Test
    public void test_atom_arity1() throws Exception {
        Atom p = ParseUtils.parseAtom("p(c)");
        check_atom_arity1(p, "p", "c");
    }

    //ps(cs)
    private void check_atom_arity1(Atom p, String ps, String cs) throws Exception {
        assertEquals(1, p.arity());
        assertEquals(ps, p.symbol());
        Term c = p.getArg(0);
        check_term_arity0(c, cs);
    }
    
   @Test
    public void test_atom_arity2() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(c0,c1)");
        check_atom_arity2(p, "p", "c0", "c1");
        p = ParseUtils.parseAtom("p(c,c)");
        check_atom_arity2(p, "p", "c", "c");
    }

    //ps(c0s,c1s)
    private void check_atom_arity2(Atom p, String ps, String c0s, String c1s) throws Exception {
        assertEquals(2, p.arity());
        assertEquals(ps, p.symbol());
        Term c = p.getArg(0);
        check_term_arity0(c, c0s);
        c = p.getArg(1);
        check_term_arity0(c, c1s);
    }

    @Test
    public void test_atom_arity3() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(c0,c1,c2)");
        check_atom_arity3(p, "p", "c0", "c1", "c2");
        p = ParseUtils.parseAtom("p(c,c,c)");
        check_atom_arity3(p, "p", "c", "c", "c");
    }

    //ps(c0s,c1s,c2s)
    private void check_atom_arity3(Atom p, String ps, String c0s, String c1s, String c2s) throws Exception {
        assertEquals(3, p.arity());
        assertEquals(ps, p.symbol());
        Term c = p.getArg(0);
        check_term_arity0(c, c0s);
        c = p.getArg(1);
        check_term_arity0(c, c1s);
        c = p.getArg(2);
        check_term_arity0(c, c2s);
    }

    @Test
    public void test_atom_arity3_middle_nested() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(c0,t(c1),c2)");
        check_atom_arity3_middle_nested(p, "p", "c0", "t", "c1", "c2");
        p = ParseUtils.parseAtom("p(c,t(c),c)");
        check_atom_arity3_middle_nested(p, "p", "c", "t", "c", "c");
    }

    //ps(c0s,ts(c1s),c2s)
    private void check_atom_arity3_middle_nested(Atom p, String ps, String c0s, String ts, String c1s, String c2s) throws Exception {
        assertEquals(3, p.arity());
        assertEquals(ps, p.symbol());
        Term c0 = p.getArg(0);
        check_term_arity0(c0, c0s);
        Term t = p.getArg(1);
        check_term_arity1(t, ts, c1s);
        Term c1 = p.getArg(2);
        check_term_arity0(c1, c2s);
    }

    @Test
    public void test_atom_arity1_nested() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(t(c))");
        check_atom_arity1_nested(p, "p", "t", "c");
    }

    //p(t(c))
    private void check_atom_arity1_nested(Atom p, String ps, String ts, String cs) throws Exception {
        assertEquals(1, p.arity());
        assertEquals(ps, p.symbol());
        Term t = p.getArg(0);
        check_term_arity1(t, ts, cs);
    }

    @Test
    public void test_atom_arity2_first_nested() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(t(c0),c1)");
        check_atom_arity2_first_nested(p, "p", "t", "c0", "c1");
        p = ParseUtils.parseAtom("p(t(c),c)");
        check_atom_arity2_first_nested(p, "p", "t", "c", "c");
    }

    //p(t(c0),c1)
    private void check_atom_arity2_first_nested(Atom p, String ps, String ts, String c0s, String c1s) throws Exception {
        assertEquals(2, p.arity());
        assertEquals(ps, p.symbol());
        Term t = p.getArg(0);
        check_term_arity1(t, ts, c0s);
        Term c1 = p.getArg(1);
        check_term_arity0(c1, c1s);
    }

    @Test
    public void test_atom_arity2_second_nested() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(c0,t(c1))");
        check_atom_arity2_second_nested(p, "p", "c0", "t", "c1");
        p = ParseUtils.parseAtom("p(c,t(c))");
        check_atom_arity2_second_nested(p, "p", "c", "t", "c");
    }

    //p(c0,t(c1))
    private void check_atom_arity2_second_nested(Atom p, String ps, String c0s, String ts, String c1s) throws Exception {
        assertEquals(2, p.arity());
        assertEquals(ps, p.symbol());
        Term c0 = p.getArg(0);
        check_term_arity0(c0, c0s);
        Term t = p.getArg(1);
        check_term_arity1(t, ts, c1s);
    }

    @Test
    public void test_atom_multiple_nesting() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(t0(t1(c)))");
        assertEquals(1, p.arity());
        assertEquals("p", p.symbol());
        check_term_arity1_nested(p.getArg(0), "t0", "t1", "c");
        //
        p = ParseUtils.parseAtom("p(t0(t1(t2(c))))");
        assertEquals(1, p.arity());
        assertEquals("p", p.symbol());
        Term t0 = p.getArg(0);
        assertEquals(1, t0.arity());
        assertEquals("t0", t0.symbol());
        check_term_arity1_nested(t0.getArg(0), "t1", "t2", "c");
    }

    @Test
    public void test_atom_nesting_and_arities() throws Exception {
        Atom p;
        p = ParseUtils.parseAtom("p(t0(c00,c01,c02),t1(c10,c11,c12),t2(c20,c21,c22))");
        assertEquals(3, p.arity());
        assertEquals("p", p.symbol());
        Term t0 = p.getArg(0);
        check_term_arity3(t0, "t0", "c00", "c01", "c02");
        Term t1 = p.getArg(1);
        check_term_arity3(t1, "t1", "c10", "c11", "c12");
        Term t2 = p.getArg(2);
        check_term_arity3(t2, "t2", "c20", "c21", "c22");
        //
        p = ParseUtils.parseAtom("p(t0(c00,t(c01),c02),t1(c10,t(c11),c12),t2(c20,t(c21),c22))");
        assertEquals(3, p.arity());
        assertEquals("p", p.symbol());
        t0 = p.getArg(0);
        check_term_arity3_middle_nested(t0, "t0", "c00", "t", "c01", "c02");
        t1 = p.getArg(1);
        check_term_arity3_middle_nested(t1, "t1", "c10", "t", "c11", "c12");
        t2 = p.getArg(2);
        check_term_arity3_middle_nested(t2, "t2", "c20", "t", "c21", "c22");
    }

}
