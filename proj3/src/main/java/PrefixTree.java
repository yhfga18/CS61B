/**
 * Created by hideyoshitakahashi on 4/20/17.
 */
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class PrefixTree {

    Node root;

    PrefixTree() {
        root = new Node(null);
    }

    List<String> wordList;

    class Node {
        String singleString;
        boolean isWord; // if this is the last character of the word
        Map<String, Node> children = new HashMap<>();
        Set<String> words = new HashSet<>();

        Node(String s) {
            singleString = s;
        }

        public void add(String word) {
            add(word, this, word);
        }

        /*
                public void add(String word, Node node, String locationName) {

                }
        */
        public void add(String word, Node parentNode, String actualName) {
            int lenOfWord = word.length();
            if (lenOfWord < 1 || word == null) {
                parentNode.isWord = true;
                if (words == null) {
                    words = new HashSet<>();
                }
                words.add(actualName);
                return;
            }

            if (parentNode == null) {
                return;
            }

            String firstLetter = word.substring(0, 1);

            if (firstLetter.matches("[^A-Za-z|^ ]")) {
                add(word.substring(1, lenOfWord), this, actualName);
                return;
            }


            Node current;

            firstLetter = firstLetter.toLowerCase();
            if (parentNode.children.containsKey(firstLetter)) {
                current = parentNode.children.get(firstLetter);
            } else {
                current = new Node(firstLetter);
                parentNode.children.put(firstLetter, current);
            }
            current.add(word.substring(1, lenOfWord), current, actualName);
        }
    }

    public List<String> getLocationsByPrefix(String prefix) {
        wordList = new ArrayList<>();
        Node currentNode = root;
        int prefixLen = prefix.length();
        for (int k = 0; k < prefixLen; k++) {
            String  ch = prefix.substring(k, k + 1);
            if (currentNode.children.containsKey(ch)) {
                currentNode = currentNode.children.get(ch);
            } else {
                return wordList;
            }
        }
        collectWords(currentNode);
        currentNode = root;
        return wordList;
    }
    private void collectWords(Node current) {
        if (current.isWord) {
            for (String word: current.words) {
                wordList.add(word);
            }
            if (current.children == null) {
                return;
            }
        }
        for (Node node : current.children.values()) {
                collectWords(node);
        }
    }


}

