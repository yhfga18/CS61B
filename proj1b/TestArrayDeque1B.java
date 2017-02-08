import org.junit.Test;

import static org.junit.Assert.assertEquals;

// Write a single JUnit test
// marked with the @Test annotation in lab3.

// The name of your test method does not matter.

// Your test should randomly call StudentArrayDeque and
// ArrayDequeSolution methods until they disagree on an output.

// You can generate random numbers using the StdRandom library.
// Use StudentArrayDequeLauncher as a guide, and if you copy and paste
// code from StudentArrayDequeLauncher, make sure to cite your source.

public class TestArrayDeque1B {
    static String message = "";
    @Test
    public void main() {
        StudentArrayDeque<Integer> studentA = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> rightA = new ArrayDequeSolution<Integer>();
        for (int i = 0; i < 10; i += 1) {
            int randomN1 = StdRandom.uniform(9);
            if (randomN1 < 4) {
                String s = " ";
                studentA.addFirst(i);
                rightA.addFirst(i);
                saveString(messageAddFirst(i));
                assertEquals(message, studentA.removeFirst(), rightA.removeFirst());
            } else {
                studentA.addLast(i);
                rightA.addLast(i);
                saveString(messageAddLast(i));
//                 assertEquals("2nd MESSAGE!!!!!", studentA.removeLast(), rightA.removeLast());
            }
        }
        for (int i = 0; i < 10; i += 1) {
            int randomN2 = StdRandom.uniform(9);
            if (randomN2 < 4) {
                saveString("removeFirst()");
                Integer actual = studentA.removeFirst();
                assertEquals(message, actual, rightA.removeFirst());
            } else {
                saveString("removeLast()");
                Integer actual = studentA.removeLast();
                assertEquals(message, actual, rightA.removeLast());
            }

        }
        for (int i = 0; i < rightA.size(); i++) {
            assertEquals("3rd MESSAGE!!", studentA.get(i), rightA.get(i));
        }
    }

    static String messageAddFirst(int i) {
        return "addFirst(" + i + ")";
    }
    static String messageAddLast(int i) {
        return "addLast(" + i + ")";
    }
    static void saveString(String s) {
        message += s + "\n";
    }
}

