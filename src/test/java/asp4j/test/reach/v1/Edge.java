package asp4j.test.reach.v1;

import asp4j.mapping.annotations.Arg;
import asp4j.mapping.annotations.DefAtom;
import java.util.Objects;

/**
 *
 * @author hbeck Jun 9, 2013
 */
@DefAtom("edge")
public class Edge {
    
    private Node from;
    private Node to;

    public Edge() {
    }
    
    public Edge(String from, String to) {
        this.from = new Node(from);
        this.to = new Node(to);
    }

    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    @Arg(0)
    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    @Arg(1)
    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.from);
        hash = 23 * hash + Objects.hashCode(this.to);
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
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        if (!Objects.equals(this.to, other.to)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Edge{" + "from=" + from + ", to=" + to + '}';
    }    

}
