package asp4j.solver.object;

import asp4j.annotations.ReflectionUtils;
import asp4j.lang.Atom;
import asp4j.lang.answerset.AnswerSet;
import asp4j.lang.answerset.AnswerSetImpl;
import asp4j.lang.answerset.AnswerSets;
import asp4j.lang.answerset.AnswerSetsImpl;
import asp4j.mapping.ObjectAtom;
import asp4j.mapping.OutputAtom;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.Solver;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck date May 20, 2013
 */
public class ObjectSolverImpl implements ObjectSolver {

    private final Solver solver;

    public ObjectSolverImpl(Solver solver) {
        this.solver = solver;
    }

    @Override
    public AnswerSets<OutputAtom> getAnswerSets(Program<ObjectAtom> program, OutputAtomBinding binding) throws Exception {
        ProgramBuilder<Atom> builder = new ProgramBuilder();
        builder.addFiles(program.getFiles());
        for (ObjectAtom objectAtom : program.getInput()) {
            builder.add(objectAtom.asAtom());
        }
        List<AnswerSet<Atom>> answerSets = solver.getAnswerSets(builder.build()).asList();
        final Collection<String> predicateNames = binding.getBoundPredicateNames();
        Predicate<Atom> isOutputAtom = new Predicate<Atom>() {
            @Override
            public boolean apply(Atom atom) {
                return predicateNames.contains(atom.predicateName());
            }
        };
        List<AnswerSet<OutputAtom>> list = new LinkedList();
        for (AnswerSet<Atom> answerSet : answerSets) {
            Set<Atom> filtered = answerSet.filter(isOutputAtom);
            Collection<OutputAtom> as = new LinkedList();
            for (Atom atom : filtered) {
                as.add(binding.asObject(atom));
            }
            list.add(new AnswerSetImpl(as));
        }
        return new AnswerSetsImpl(list);
    }
    
    @Override
    public AnswerSets<Object> getAnswerSets(Program<Object> program, ObjectBinding binding) throws Exception {
        ProgramBuilder<Atom> builder = new ProgramBuilder();
        builder.addFiles(program.getFiles());
        for (Object object : program.getInput()) {
            builder.add(ReflectionUtils.asAtom(object));
        }
        List<AnswerSet<Atom>> answerSets = solver.getAnswerSets(builder.build()).asList();
        final Collection<String> predicateNames = binding.getBoundPredicateNames();
        Predicate<Atom> isOutputAtom = new Predicate<Atom>() {
            @Override
            public boolean apply(Atom atom) {
                return predicateNames.contains(atom.predicateName());
            }
        };
        List<AnswerSet<Object>> list = new LinkedList();
        for (AnswerSet<Atom> answerSet : answerSets) {
            Set<Atom> filtered = answerSet.filter(isOutputAtom);
            Collection<Object> as = new LinkedList();
            for (Atom atom : filtered) {
                as.add(binding.asObject(atom));
            }
            list.add(new AnswerSetImpl(as));
        }
        return new AnswerSetsImpl(list);
    }
}
