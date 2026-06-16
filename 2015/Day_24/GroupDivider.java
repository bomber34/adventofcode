import java.util.ArrayList;
import java.util.List;

public class GroupDivider {

    private final List<Long> _originalList;
    private final Long _goalSum;

    public GroupDivider(List<Long> originalList, long goalSum) {
        _originalList = originalList;
        _goalSum = goalSum;
    }

    public List<ArrayList<Long>> findGroup() {
        ArrayList<ArrayList<Long>> groups = new ArrayList<>();

        divide(0, _goalSum, new ArrayList<>(), groups);
        return groups;
    }

    void divide(int curIdx, long goal, ArrayList<Long> curList, ArrayList<ArrayList<Long>> groups) {
        if (goal == 0) {
            groups.add(curList);
            return;
        } else if (curIdx >= _originalList.size()) {
            return;
        }
        int nextIdx = curIdx+1;
        ArrayList<Long> copy = new ArrayList<>(curList);
        Long curElem = _originalList.get(curIdx);
        if (curElem <= goal) {
            curList.add(curElem);
            divide(nextIdx, goal - curElem, curList, groups);
        }
        divide(nextIdx, goal, copy, groups);
    }
}
