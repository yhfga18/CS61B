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
            while (i <= word.length() / 2) {
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
        int j = word.length() - 1;
        for (int i = 0; i < word.length() / 2;) {
            if (!(cc.equalChars(word.charAt(i), word.charAt(j)))) {
                return false;
            }
            i += 1;
            j -= 1;
        }
        return true;
    }
}
