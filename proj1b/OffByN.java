/**
 * Created by hideyoshitakahashi on 2/4/17.
 */
public class OffByN implements CharacterComparator {
    private int N;
    public OffByN(int N) {
        this.N = N;
    }
    @Override
    public boolean equalChars(char x, char y) {
        if (x - y == -this.N || y - x == this.N) {
            return true;
        }
        return false;
    }
}
