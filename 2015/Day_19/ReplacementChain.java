import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ReplacementChain {
    final int _depth;
    final String _text;

    ReplacementChain(int depth, String word) {
        _text = word;
        _depth = depth;
    }

    /**
     * Gets the next replacement Strings in the chain
     *
     * @param map with possible options for replacmenets
     * @param ignoredWords Set of already found replacements we can ignore for memory reasons
     * @return ArrayList<ReplacementChain> with the next options, no intended order as any replacement may be valid
     */
    public ArrayList<ReplacementChain> getReplacements(HashMap<String, HashSet<String>> map, Set<String> ignoredWords) {
        ArrayList<ReplacementChain> replacements = new ArrayList<>();
        for (String key : map.keySet()) {
            int index = _text.indexOf(key);
            while (index > -1) {
                Set<String> options = map.get(key);
                int endIndex = index + key.length();
                for (String option : options) {
                    StringBuilder sb = new StringBuilder(_text).replace(index, endIndex, option);
                    String next = sb.toString();
                    if (!ignoredWords.contains(next)) {
                        ignoredWords.add(next);
                        replacements.add(new ReplacementChain(_depth+1, next));
                    }
                }
                index = _text.indexOf(key, index+1);
            }
        }
        return replacements;
    }

    public int getDepth() {
        return _depth;
    }

    public String getText() {
        return _text;
    }

    public int getTextLength() {
        return _text.length();
    }
}
