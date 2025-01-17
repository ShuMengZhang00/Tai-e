import java.util.Set;
public class NullDeref3{

    void foo() {
        NullDeref3 n = new NullDeref3();
        if (n != null)
            System.out.println("Not null");
        System.out.println(n.hashCode());
    }

    Object foo2(Object o) {
        if (o == null)
            return o;

        if (o instanceof String)
            return o;

        // we should flag the o != null test as redundant
        if (o != null && o instanceof Set)
            return ((Set) o).iterator();

        // no warning should be generated here
        return o.getClass();
    }

    Object bar(Object o) {
        if (o != null)
            return o;

        if (o == null)
            System.out.println("Got null");

        // Should get high priority
        System.out.println(o.hashCode());
        // Redundant
        if (o == null)
            return o;
        // Unreachable code
        return o.getClass();
    }
}
