import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Replacement {
    private final HashMap<String, ArrayList<String>> _map;
    private final String _text;
    private final Set<String> _foundReplacements;

    public Replacement(HashMap<String, ArrayList<String>> m, String txt) {
        _map = m;
        _text = txt;
        _foundReplacements = new HashSet<>();
    }

    public void calculateAllReplacements() {
        for (String key : _map.keySet()) {
            ArrayList<String> replacementOptions = _map.get(key);
            int index = 0;
            while (true) {
                index = _text.indexOf(key, index);
                if (index < 0) {
                    break;
                }
                for (String option : replacementOptions) {
                    StringBuilder replacer = new StringBuilder(_text);
                    replacer.replace(index, index+key.length(), option);
                    _foundReplacements.add(replacer.toString());
                }
                index++;
            }
        }
    }

    public int getNumberOfPossibleReplacements() {
        return _foundReplacements.size();
    }

    public String getText() {
        return _text;
    }

    public HashMap<String, ArrayList<String>> getMap() {
        return _map;
    }
}
