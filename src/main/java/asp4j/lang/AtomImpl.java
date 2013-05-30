package asp4j.lang;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author hbeck May 14, 2013
 */
public class AtomImpl implements Atom {

    private final String predicateSymbol;
    private final Term[] args;

    public AtomImpl(String predicateSymbol, Term... args) {
        this.predicateSymbol = predicateSymbol;
        if (args == null || args.length == 0) {
            this.args = null;
        } else {
            this.args = args;
        }
    }

    @Override
    public int arity() {
        if (args == null) {
            return 0;
        }
        return args.length;
    }

    @Override
    public Term getArg(int idx) {
        if (args == null) {
            return null;
        }
        return args[idx];
    }

    @Override
    public String symbol() {
        return predicateSymbol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(symbol());
        if (args != null && args.length > 0) {
            sb.append("(").append(args[0].toString());
            for (int i = 1; i < args.length; i++) {
                sb.append(",").append(args[i].toString());
            }
            sb.append(")");
        }
        sb.append(".");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.predicateSymbol);
        hash = 53 * hash + Arrays.deepHashCode(this.args);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Atom)) {
            return false;
        }
        final Atom other = (Atom) obj;
        if (!Objects.equals(this.symbol(), other.symbol())) {
            return false;
        }
        if (this.arity()!=other.arity()) {
            return false;
        }
        for (int i=0; i<this.args.length; i++) {
            if (!this.getArg(i).equals(other.getArg(i))) {
                return false;
            }
        }
        return true;
    }
}
