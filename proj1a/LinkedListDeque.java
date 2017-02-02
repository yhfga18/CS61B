public class LinkedListDeque<Item> {

    public class Node {

        private Item value;
        private Node prev, next;

        public Node(){
            this.value = null;
        }
        public Node(Item value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        this.sentinel = new Node();
        this.sentinel.prev = this.sentinel;
        this.sentinel.next = this.sentinel;
        this.size = 0;
    }

    // public LinkedListDeque(Item value){
    //   this.sentinel = new Node();
    //   this.sentinel.next = new Node(value, this.sentinel, this.sentinel);
    //   this.sentinel.prev = this.sentinel.next;
    //   this.size = 1;
    // }

    public void addFirst(Item value) {
        if (isEmpty()) {
            this.sentinel.next = new Node(value, this.sentinel, this.sentinel);
            this.sentinel.prev = this.sentinel.next;
            this.size += 1;
        } else {
            Node oldFrontNode = this.sentinel.next;
            Node newFrontNode = new Node(value, this.sentinel, oldFrontNode);
            this.sentinel.next = newFrontNode;
            oldFrontNode.prev = newFrontNode;
            this.size += 1;
            // oldFrontNodeとnewFrontNodeのポインタが残ってる状態。
        }
    }

    public void addLast(Item value) {
        if (isEmpty()) {
            addFirst(value);
        } else {
            Node oldLastNode = sentinel.prev;
            Node newLastNode = new Node(value, oldLastNode, this.sentinel);
            this.sentinel.prev = newLastNode;
            oldLastNode.next = newLastNode;
            this.size += 1;
        }
    }

    public boolean isEmpty() {
        return (this.sentinel.next == this.sentinel);
    }

    public int size() {
        return this.size;
    }

    public void printDeque() {
        Node pointer = this.sentinel;
        while (pointer.next != this.sentinel) {
            System.out.print(pointer.next.value.toString() + ' ');
            pointer = pointer.next;
        }
        System.out.println();
    }


    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            Node frontNode = this.sentinel.next;
            this.sentinel.next.next.prev = this.sentinel;
            this.sentinel.next = this.sentinel.next.next;
            // frontNode.prev = null; //必要？
            // frontNode.next = null;
            this.size -= 1;
            Item returnValue = frontNode.value;
            frontNode = null;
            return returnValue;
        }
    }

    public Item removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            Node lastNode = this.sentinel.prev;
            this.sentinel.prev.prev.next = this.sentinel;
            this.sentinel.prev = this.sentinel.prev.prev;
            // lastNode.prev = null;
            // lastNode.next = null;//必要？
            this.size -= 1;
            Item returnValue = lastNode.value;
            lastNode = null;
            return returnValue;
        }
    }

    public Item get(int index) {
        if (index <= size / 2) {
            Node pointer = this.sentinel.next;
            int i = 0;
            while (index != i) {
                pointer = pointer.next;
                i += 1;
            }
            return pointer.value;
        } else {
            Node pointer = this.sentinel.prev;
            int i = size - 1;
            while (index != i) {
                pointer = pointer.prev;
                i -= 1;
            }
            return pointer.value;
        }
    }

    public Item getRecursive(int index) {
        Node pointer = sentinel.next;
        int i = 0;
        Item ithItem = helper(i, index, pointer);
        return ithItem;
    }

    private Item helper(int ith, int index, Node pointer) {
        if (index == ith) {
            return pointer.value;
        } else {
            ith += 1;
            pointer = pointer.next;
            return helper(ith, index, pointer);

        }
    }
}
