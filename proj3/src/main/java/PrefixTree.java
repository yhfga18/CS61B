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
    String name;

    PrefixTree(String name) {
        this.name = name;
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
                if (words == null) {
                    words = new HashSet<>();
                }
                words.add(actualName);
                parentNode.isWord = true;
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

            //convert to lower case
            firstLetter = firstLetter.toLowerCase();

            Map<String, PrefixTree.Node> n = parentNode.children;
            if (n.containsKey(firstLetter)) {
                current = n.get(firstLetter);
            } else {
                current = new Node(firstLetter);
                n.put(firstLetter, current);
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
        if (current.children == null) {
            return;
        }
        if (current.isWord) {
            for (String word: current.words) {
                wordList.add(word);
            }
        }
        for (Node node : current.children.values()) {
                collectWords(node);
        }
    }


}

