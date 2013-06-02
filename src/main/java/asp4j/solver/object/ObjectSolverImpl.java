package asp4j.solver.object;

import asp4j.lang.AnswerSet;
import asp4j.lang.Atom;
import asp4j.program.Program;
import asp4j.program.ProgramBuilder;
import asp4j.solver.ReasoningMode;
import asp4j.solver.Solver;
import java.util.ArrayList;
import java.util.Collections;
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
        return Collections.unmodifiableList(computeAnswerSets(program, null, null));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding) throws Exception {
        if (binding == null) {
            throw new NullPointerException();
        }
        return Collections.unmodifiableList(computeAnswerSets(program, binding, null));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Filter filter) throws Exception {
        if (filter == null) {
            throw new NullPointerException();
        }
        return Collections.unmodifiableList(computeAnswerSets(program, null, filter));
    }

    @Override
    public List<AnswerSet<Object>> getAnswerSets(Program<?> program, Binding binding, Filter filter) throws Exception {
        if (binding == null || filter == null) {
            throw new NullPointerException();
        }
        return Collections.unmodifiableList(computeAnswerSets(program, binding, filter));
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode) throws Exception {
        Binding binding = new Binding();
        Set<Atom> atoms = solver.getConsequence(computeLowLevelProgram(program, binding), mode);
        return binding.map(atoms);
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding) throws Exception {
        if (binding == null) {
            throw new NullPointerException();
        }
        Set<Atom> atoms = solver.getConsequence(computeLowLevelProgram(program, binding), mode);
        return binding.map(atoms);
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Filter filter) throws Exception {
        if (filter == null) {
            throw new NullPointerException();
        }
        Binding binding = new Binding();
        Set<Atom> atoms = solver.getConsequence(computeLowLevelProgram(program, binding), mode);
        return binding.filterAndMap(atoms, filter);
    }

    @Override
    public Set<Object> getConsequence(Program<?> program, ReasoningMode mode, Binding binding, Filter filter) throws Exception {
        if (binding == null || filter == null) {
            throw new NullPointerException();
        }
        Set<Atom> atoms = solver.getConsequence(computeLowLevelProgram(program, binding), mode);
        return binding.filterAndMap(atoms, filter);
    }

    /**
     * @param binding may be null
     */
    private List<AnswerSet<Object>> computeAnswerSets(Program<?> program, Binding binding, Filter filter) throws Exception {
        if (binding==null) {
            binding = new Binding();
        }
        List<AnswerSet<Atom>> answerSets = solver.getAnswerSets(computeLowLevelProgram(program, binding));
        List<AnswerSet<Object>> list = new ArrayList<>();
        if (filter == null) {
            for (AnswerSet<Atom> answerSet : answerSets) {
                list.add(binding.map(answerSet));
            }
        } else {
            for (AnswerSet<Atom> answerSet : answerSets) {
                list.add(binding.filterAndMap(answerSet, filter));                
            }
        }
        return list;
    }

    /**
     * @param program
     * @param binding not null
     * @return
     * @throws Exception
     */
    private Program<Atom> computeLowLevelProgram(final Program<?> program, Binding binding) throws Exception {
        if (binding == null) {
            throw new NullPointerException();
        }
        for (Object input : program.getInput()) {
            binding.add(input);
        }
        ProgramBuilder<Atom> builder = new ProgramBuilder<>();
        builder.addFiles(program.getFiles());
        for (Object input : program.getInput()) {
            builder.add(binding.asAtom(input));
        }
        return builder.build();
    }
}
