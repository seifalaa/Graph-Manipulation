package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DisjointSets {
    private List<Map<String, Set<String>>> disjointSet;

    public DisjointSets() {
        disjointSet = new ArrayList<Map<String, Set<String>>>();
    }

    public void create_set(String element) {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Set<String> set = new HashSet<String>();
        set.add(element);
        map.put(element, set);
        disjointSet.add(map);
    }

    public void union(String first, String second) {
        String first_rep = find_set(first);
        String second_rep = find_set(second);

        Set<String> first_set = null;
        Set<String> second_set = null;

        for (Map<String, Set<String>> map : disjointSet) {
            if (map.containsKey(first_rep)) {
                first_set = map.get(first_rep);
            } else if (map.containsKey(second_rep)) {
                second_set = map.get(second_rep);
            }
        }

        if (first_set != null && second_set != null)
            first_set.addAll(second_set);

        for (int index = 0; index < disjointSet.size(); index++) {
            Map<String, Set<String>> map = disjointSet.get(index);
            if (map.containsKey(first_rep)) {
                map.put(first_rep, first_set);
            } else if (map.containsKey(second_rep)) {
                map.remove(second_rep);
                disjointSet.remove(index);
            }
        }

    }

    public String find_set(String element) {
        for (Map<String, Set<String>> map : disjointSet) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Set<String> set = map.get(key);
                if (set.contains(element)) {
                    return key;
                }
            }
        }
        return null;
    }

    public int getNumberofDisjointSets() {
        return disjointSet.size();
    }
}

