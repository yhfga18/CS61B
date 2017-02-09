/**
 * Created by hideyoshitakahashi on 2/4/17.
 */
public class Palindrome {

    public static Deque<Character> wordToDeque(String word) {
        ArrayDeque<Character> a = new ArrayDeque<Character>();
        // LinkedListDeque<Character> b = new LinkedListDeque<Character>();
        for (int i = 0; i < word.length(); i++) {
            a.addLast(word.charAt(i));
        }
        return a;
    }
    public static boolean isPalindrome(String word) {
        if (word.length() <= 1) {
            return true;
        } else {
            int i = 0;
            int j = word.length() - 1;
            while (i < word.length() / 2) {
                if (word.charAt(i) != word.charAt(j)) {
                    return false;
                }
                i += 1;
                j -= 1;
            }
            return true;
        }
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
//        Deque d = wordToDeque(word);
//        for (int i = 0; i < d.size() / 2; i++) {
//            Object item1 = d.get(i);
//            Object item2 = d.get(d.size() - 1 - i);
//            if (!cc.equalChars(((Character) item1), ((Character) item2))) {
//                return false;
//            }
//        }
//        return true;

        int j = word.length();
        if (j <= 1) {
            return true;
        }
        for (int i = 0; i < (j - 1) / 2; i++) {
            if (!(cc.equalChars(word.charAt(i), word.charAt(j - 1 - i)))) {
                return false;
            }
        }
        return true;
    }
}
