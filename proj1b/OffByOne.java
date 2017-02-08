/**
 * Created by hideyoshitakahashi on 2/4/17.
 */
public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        if (x - y == -1 || y - x == 1) {
            return true;
        }
//        else if (x - y == 26 || y - x == 26) {
//            return true;
//        }
        return false;
    }
}
