package Tool;

import Graph.Detail.TwoMonomorphicGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class CustomHashSet extends ArrayList<TwoMonomorphicGraph> {
    HashSet<String> stringHashSet = new HashSet<>();

    @Override
    public boolean add(TwoMonomorphicGraph t) {
        if (!stringHashSet.add(t.toString())) return false;
        return super.add(t);
    }

    @Override
    public boolean addAll(Collection<? extends TwoMonomorphicGraph> c) {
        for (TwoMonomorphicGraph t : c) {
            add(t);
        }
        return true;
    }
}
