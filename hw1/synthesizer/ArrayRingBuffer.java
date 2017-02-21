// TODO: Make sure to make this class a part of the synthesizer package
// package <package name>;
package synthesizer;
import synthesizer.AbstractBoundedQueue;

import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
        rb = (T[]) new Object[this.capacity];
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.

        rb[last] = x;
        last = (last + 1) % capacity;
        fillCount += 1;
        throw new RuntimeException("Ring Buffer Overflow");
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Empty, You Ugly Face!");
        }
        T itemToReturn = null;
        itemToReturn = rb[first];
        rb[first] = null;
        return itemToReturn;

        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()){
            throw new RuntimeException("Empty, You Nasty Animal!");
        }
        return rb[first];
        // TODO: Return the first item. None of your instance variables should change.
    }

    public Iterator iterator() {
        return new ArrayRingBufferIterator<T>(rb);
    }

    private class ArrayRingBufferIterator<T> implements Iterator<T>{
        private T[] bq;
        private int counter ;
        public ArrayRingBufferIterator(T[] bq) {
            counter = 0;
            this.bq = bq;
        }
        public boolean hasNext() {
            return !(isEmpty());
        }
        public T next() {
            counter += 1;
            return bq[counter - 1];
        }

    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
}
