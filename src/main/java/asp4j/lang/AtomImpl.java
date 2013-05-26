package asp4j.lang;

import java.util.Objects;

/**
 *
 * @author hbeck date May 14, 2013
 */
public class AtomImpl implements Atom {

    private final String predicateName;
    private final String[] args;

    public AtomImpl(String predicateName, String... args) {
        this.predicateName = predicateName;
        this.args = args;
    }

    public static Atom parse(String atomString) {
        String s = atomString.trim();
        if (s.endsWith(".")) {
            s = s.substring(0, s.length() - 1);
        }
        int parenIdx = s.indexOf("(");
        String predicateName = s.substring(0, parenIdx);
        String inner = s.substring(parenIdx + 1, s.length() - 1);
        String[] args = inner.split(",");
        for (String arg : args) {
            if (arg.contains("(")) {
                throw new UnsupportedOperationException("function symbols not supported");
            }
        }
        return new AtomImpl(predicateName, args);
    }

    @Override
    public int arity() {
        return args.length;
    }

    @Override
    public String getArg(int idx) {
        return args[idx];
    }

    @Override
    public String predicateName() {
        return predicateName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(predicateName()).append("(");
        if (args.length > 0) {
            sb.append(args[0]);
            for (int i = 1; i < args.length; i++) {
                sb.append(",").append(args[i]);
            }
        }
        sb.append(").");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.predicateName);
        for (int i = 0; i < args.length; i++) {
            hash = 59 * hash + Objects.hash(args[i]);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AtomImpl other = (AtomImpl) obj;
        if (!Objects.equals(this.predicateName, other.predicateName)) {
            return false;
        }
        for (int i=0; i<args.length; i++) {
            if(!Objects.equals(this.args[i], other.args[i])) {
                return false;
            }
        }
        return true;
    }
}
