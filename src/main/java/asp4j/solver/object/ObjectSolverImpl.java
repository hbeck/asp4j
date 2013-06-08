package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hbeck May 25, 2013
 */
public class ObjectSolverImpl implements ObjectSolver {

    private final Solver solver;

    public ObjectSolverImpl(Solver solver) {
        this.solver = solver;
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program) throws Exception {
        return Collections.unmodifiableList(computeAnswerSets(program, new Binding(), new Filter()));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding) throws Exception {
        return Collections.unmodifiableList(computeAnswerSets(program, binding, new Filter()));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Filter filter) throws Exception {
        return Collections.unmodifiableList(computeAnswerSets(program, new Binding(), filter));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding, Filter filter) throws Exception {
        return Collections.unmodifiableList(computeAnswerSets(program, binding, filter));
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode) throws Exception {
        return getConsequence(program, mode, new Binding(), new Filter());
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding) throws Exception {
        return getConsequence(program, mode, binding, new Filter());
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Filter filter) throws Exception {
        return getConsequence(program, mode, new Binding(), filter);
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding, Filter filter) throws Exception {
        Program<Atom> atomProgram = getAtomProgram(program, binding, filter);
        Set<Atom> atoms = solver.getConsequence(atomProgram, mode);
        return binding.filterAndMap(atoms, filter);
    }

    private List<AnswerSet<Object>> computeAnswerSets(Program<?> program, Binding binding, Filter filter) throws Exception {
        Program<Atom> atomProgram = getAtomProgram(program, binding, filter);
        List<AnswerSet<Atom>> answerSets = solver.getAnswerSets(atomProgram);
        return binding.filterAndMap(answerSets, filter);
    }

    private Program<Atom> getAtomProgram(Program<?> program, Binding binding, Filter filter) throws Exception {
        prepareIO(program, binding, filter);
        ProgramBuilder<Atom> builder = new ProgramBuilder<>();
        builder.addFiles(program.getFiles());
        for (Object input : program.getInput()) {
            builder.add((Atom) binding.mapAsLangElem(input));
        }
        return builder.build();
    }

    private void prepareIO(Program<?> program, Binding binding, Filter filter) throws Exception {
        Set<Class<?>> inputClasses = new HashSet<>();
        for (Object object : program.getInput()) {
            inputClasses.add(object.getClass());
        }
        //input
        binding.addAll(inputClasses);
        //output
        Set<Class<?>> filterClasses = filter.getClasses();
        if (filterClasses.isEmpty()) {
            filter.addAll(inputClasses);
        } else {
            binding.addAll(filterClasses);
        }
    }
}
