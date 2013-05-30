package asp4j.lang;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author hbeck date May 30, 2013
 */
public class TermImpl implements Term {

    private final String symbol;
    private final Term[] args;

    public TermImpl(String functionSymbol, Term... args) {
        this.symbol = functionSymbol;
        if (args == null || args.length==0) {
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
        return symbol;
    }    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(symbol);
        if (args!=null && args.length>0) {
            sb.append("(").append(args[0].toString());
            for (int i=1;i<args.length;i++) {
                sb.append(",").append(args[i].toString());
            }
            sb.append(")");
        }        
        return sb.toString();
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.symbol);
        hash = 53 * hash + Arrays.deepHashCode(this.args);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Term)) {
            return false;
        }
        final Term other = (Term) obj;
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
