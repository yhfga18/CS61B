import edu.princeton.cs.algs4.Queue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex = pivotIndex - 1;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        // Your code here!
        int length = unsorted.size();
        for (int i = 0; i < length; i = i + 1) {
            Item thing = unsorted.dequeue();
            if (thing.compareTo(pivot) < 0) {
                less.enqueue(thing);
            } else if (thing.compareTo(pivot) == 0) {
                equal.enqueue(thing);
            } else {
                greater.enqueue(thing);
            }

        }
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
        // Your code here!
        if (items.size() == 1 || items.size() == 0) {
            return items;
        }

        Item pivot = getRandomItem(items);
        Queue<Item> negative = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> positive = new Queue<>();

        partition(items, pivot, negative, equal, positive);
        return catenate(catenate(quickSort(negative), equal), quickSort(positive));
    }

    public static void main(String[] args) {
        Queue<Integer> students = new Queue<>();
        students.enqueue(4);
        students.enqueue(9);
        students.enqueue(8);
        students.enqueue(9);
        students.enqueue(3);
        students.enqueue(2);
        students.enqueue(1);
        students.enqueue(7);
        students.enqueue(5);
        students.enqueue(6);
        students.enqueue(3);
        students.enqueue(3);
        students.enqueue(5);
        students.enqueue(8);
        students.enqueue(3);
        students.enqueue(5);
        students.enqueue(6);
        students.enqueue(6);
        students.enqueue(2);
        // print unsorted queue
        for (Integer num: students) {
            System.out.println(num);
        }
        // create deep copy
        Queue<Integer> studentCopy  = new Queue<>();

        for (Integer num: students) {
            studentCopy.enqueue(num);
        }

        System.out.println("");

        students = QuickSort.quickSort(students);
        for (Integer num: students) {
            System.out.println(num);
        }

        System.out.println("");

        for (Integer num: studentCopy) {
            System.out.println(num);
        }
    }




}
