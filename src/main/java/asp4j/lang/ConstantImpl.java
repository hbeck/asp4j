package asp4j.lang;

import java.util.Objects;

/**
 *
 * @author hbeck date May 30, 2013
 */
public class ConstantImpl extends ConstantBase {

    private final String symbol;

    public ConstantImpl(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.symbol);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Constant)) {
            return false;
        }
        final Constant other = (Constant) obj;
        if (!Objects.equals(this.symbol, other.symbol())) {
            return false;
        }
        return true;
    }
}
