package asp4j.test.reach.v1;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefTerm;
import java.util.Objects;

/**
 *
 * @author hbeck Jun 9, 2013
 */
@DefTerm("node")
public class Node {

    private String id;

    public Node() {
    }

    public Node(String id) {
        this.id = id;
    }

    @Arg(0)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Node other = (Node) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + '}';
    }
    
    
}
