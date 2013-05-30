package asp4j.lang;

/**
 *
 * @author hbeck
 * date May 30, 2013
 */
public abstract class ConstantBase implements Constant {

    @Override
    public final int arity() {
        return 0;
    }

    @Override
    public final Term getArg(int idx) {
        return null;
    }
    
    @Override
    public String toString() {
        return symbol();
    }    

}
