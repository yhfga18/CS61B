package synthesizer;

/**
 * Created by hideyoshitakahashi on 2/21/17.
 */
public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;
    public int capacity() {
        return capacity;
    }
    public int fillCount() {
        return fillCount;
    }
}
