package asp4j.solver.object;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author hbeck Jun 1, 2013
 */
public class Filter {

    private Set<Class<?>> classes;

    public Filter() {
        this.classes = new HashSet<>();
    }

    public Filter(Class<?> clazz) {
        this.classes = new HashSet<>();
        if (clazz != null) {
            this.classes.add(clazz);
        }
    }

    public Filter(Class<?>... classes) {
        this.classes = new HashSet<>();
        if (classes != null && classes.length>0 ) {
            this.classes.addAll(Arrays.asList(classes));
        }
    }
    
    public Filter(Collection<Class<?>> classes) {        
        this.classes = new HashSet<>();
        if (classes!=null) {
            this.classes.addAll(classes);
        }
    }    

    /**
     * test whether given Class is accepted by this filter
     *
     * @param clazz
     * @return
     */
    public boolean accepts(Class<?> clazz) {
        if (clazz==null) {
            return false;
        }
        return classes.contains(clazz);
    }

    public Filter add(Class<?> clazz) {
        this.classes.add(clazz);
        return this;
    }

    public Filter remove(Class<?> clazz) {
        this.classes.remove(clazz);
        return this;
    }

    public Set<Class<?>> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class<?>> classes) {
        this.classes = classes;
        if (this.classes==null) {
            this.classes = new HashSet<>();
        }
    }
}
