public class ComboIterator {
    private final int[] _list;
    private final int _maxAmount;
    private final int _startIndex;

    ComboIterator(int types, int maxAmount) {
        _maxAmount = maxAmount;
        _list = new int[types];
        _startIndex = types-1;
        _list[_startIndex] = maxAmount;
    }

    /**
     * @return copy of internal list
     */
    public int[] getCurrentList() {
        return _list.clone();
    }

    /**
     * Checks if iterator is exhausted
     *
     * @return true if list can still be iterated
     */
    public boolean hasNext() {
        return _list[0] != _maxAmount;
    }

    /**
     * Iterates to the next combination
     *
     * @return true if state changed
     */
    public boolean next() {
        if (!hasNext()) {
            return false;
        }

        for (int i = _startIndex; i > 0; i--) {
            int value = _list[i];
            if (0 > value || value > _maxAmount) {
                String errorMsg = value > _maxAmount
                        ? String.format("Illegal array has more than the allowed max number %d", _maxAmount)
                        : "List contains value less than 0";
                throw new IllegalStateException(errorMsg);
            }

            if (value != 0) {
                _list[i]--;
                _list[i-1]++;
                if (i != _startIndex) {
                    _list[_startIndex] = _list[i];
                    _list[i] = 0;
                }
                break;
            }
        }
        return true;
    }

    @SuppressWarnings("unused") // debug help
    public String getCurrentListStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (int i : _list) {
            sb.append(String.format("%3d ", i));
        }
        return sb.toString();
    }
}
