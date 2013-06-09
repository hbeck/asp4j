package asp4j.test.reach.v1;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;

/**
 *
 * @author hbeck Jun 9, 2013
 */
@DefAtom("blocked")
public class Blocked {
    
    private Node node;

    public Blocked() {
    }
    
    public Blocked(String node) {
        this.node = new Node(node);
    }

    public Blocked(Node node) {
        this.node = node;
    }

    @Arg(0)
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.node);
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
        final Blocked other = (Blocked) obj;
        if (!Objects.equals(this.node, other.node)) {
            return false;
        }
        return true;
    }    

    @Override
    public String toString() {
        return "Blocked{" + "node=" + node + '}';
    }
    
    

}
