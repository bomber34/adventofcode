import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;

public class ReverseReplacer {
    private final HashMap<String, HashSet<String>> _map;
    private final String _text;

    public ReverseReplacer(HashMap<String, ArrayList<String>> m, String start) {
        _map = new HashMap<>();
        _text = start;

        for (String key : m.keySet()) {
            ArrayList<String> values = m.get(key);
            for (String val : values) {
                _map.putIfAbsent(val, new HashSet<>(1));
                _map.get(val).add(key);
            }
        }
    }

    public int calculateReplacementChainLength(String goal) {
        PriorityQueue<ReplacementChain> queue = new PriorityQueue<>(getComparator());
        HashSet<String> alreadyFoundWords = new HashSet<>();
        queue.add(new ReplacementChain(0, _text));
        while (!queue.isEmpty()) {
            ReplacementChain next = queue.poll();
            if (goal.equals(next.getText())) {
                return next.getDepth();
            }

            ArrayList<ReplacementChain> foundReplacements = next.getReplacements(_map, alreadyFoundWords);
            queue.addAll(foundReplacements);
        }
        return -1;
    }

    private Comparator<ReplacementChain> getComparator() {
        return Comparator.comparingInt(ReplacementChain::getTextLength).thenComparingInt(ReplacementChain::getDepth);
    }
}
