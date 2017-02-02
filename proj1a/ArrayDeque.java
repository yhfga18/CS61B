/**
 * Created by hideyoshitakahashi on 1/27/17.
 */

public class ArrayDeque<Item> {

    private Item[] items;
    private int size;
    private int first;
    private int rear;
    final static int RFACTOR = 4;
    final static int RDIVIDOR = 2;
    final static int START = 4;

    public ArrayDeque(){
        this.items = (Item[]) new Object[8];
        this.size = 0;
        this.first = 0;
        this.rear = 0;
    }

    private void resize(){
        Item[] newItems = (Item[]) new Object[this.items.length * RFACTOR];
        if (this.first == 0){
            System.arraycopy(this.items, this.first, newItems, 0, this.rear); // 0じゃなく4にして、最後にitemを入れる
        }
        else {
            System.arraycopy(this.items, this.first, newItems, 0, this.items.length - this.first - 1); // 0じゃなく4にして、最後にitemを入れる
            System.arraycopy(this.items, 0, newItems, this.items.length - this.first - 1, this.rear);
        }
        this.first = 0;
        this.rear = this.items.length - 1;
        this.items = newItems;
        // size += 1;
        // System.out.print("ResizeAddFirst Done!")
    }

    public void addFirst(Item item){
        int capacity = this.items.length;

        if (isEmpty() & capacity > 0){
            this.items[this.first] = item;
            this.first = 0;
            this.rear = 0;
        }
        else if (capacity == this.size){
            resize();
            addFirst(item);
        }
        else if (this.first == 0){
            this.first = capacity - 1;
            this.items[this.first] = item;
        }
        else {
            this.first += 1;
            this.items[this.first] = item;
        }
        this.size += 1;
    }

    public void addLast(Item item){
        int capacity = this.items.length;
        if (isEmpty() & capacity > 0) {
            this.items[0] = item;
            this.first = 0;
            this.rear = 0;
        }
        else if (this.size == capacity){
            resize();
            addLast(item);
        }
        else if (this.rear == capacity - 1) {
            this.rear = 0;
            this.items[rear] = item;
        }
        else {
            this.rear += 1;
            this.items[this.rear] = item;
        }
        this.size += 1;

    }
    //     if (this.first == 0 & this.rear == capacity){
    //         resize(); // resize
    //         this.items[this.rear + 1] = item;
    //         this.rear += 1;
    //     }
    //     else if (this.first <= this.rear & this.rear < capacity){
    //         this.items[rear + 1] = item;
    //         this.rear += 1;
    //     }
    //     else if ((0 < this.first) & (this.first < this.rear) & (this.rear == capacity)){
    //         this.items[0] = item;
    //         this.rear = 0;
    //     }
    //     else if (this.rear < this.first){
    //         if (this.rear < this.first - 1){
    //             this.items[rear + 1] = item;
    //             this.rear += 1;
    //         }
    //         else if (this.rear == this.first - 1){
    //             resize(); //resize
    //         };
    //     }
    //     else if (isEmpty() & capacity > 0) {
    //         this.items[0] = item;
    //         this.first = 0;
    //         this.rear = 0;
    //     }
    //     this.size += 1;
    // }
    public boolean isEmpty(){
        return (this.size == 0);

    }
    public int size(){
        return this.size;
    }
    public void printDeque(){

    }
    public Item removeFirst(){
        if (isEmpty()){
            return null;
        }
        else {
            Item returnItem = this.items[this.first];
            this.items[this.first] = null;
            if (this.first == this.items.length - 1){
                this.first = 0;
            }
            else{
                this.first += 1;
            }
            this.size -= 1;
            return returnItem;
        }
    }
    public Item removeLast(){
        if (isEmpty()){
            return null;
        }
        else {
            Item returnItem = this.items[this.rear];
            this.items[this.rear] = null;
            if (this.rear == 0){
                this.rear = this.items.length - 1;
            }
            else{
                this.rear -= 1;
            }
            this.size -= 1;
            return returnItem;
        }

    }
    public Item get(int index){
        if (isEmpty()){
            return null;
        }
        else {
            return this.items[(this.first + index) % this.items.length];
        }
    }

}




// addFirst()
        // if ((isEmpty() & this.items.length > 0) | (0 < this.first < this.rear)) {
        //     this.items[first] = item;
        //     this.first = 0;
        //     this.rear = 0;
        //     // ここのケースでfirst = rear = 0 を使うと、そもそもでlength = 0 をキャッチできないと思ったが、これだとどっちにしてもキャッチ不可能。arrayDeque constructorで必ず100このlengthで作ってるからオッケーと思えばいいのかどうかの問題
        // }

        // else if (this.first == 0 & this.rear < capacity) {
        //     this.first = capacity;
        //     this.items[this.first] = item;
        //     this.size += 1;

        // }

        // else if (this.first > this.rear) {
        //     if (this.first - 1 > r) {
        //         this.items[this.first - 1] = items;
        //         this.first -= 1;
        //         this.size += 1;
        //     }
        //     else if (this.first - 1 == this.rear){
        //         resize(this.items);
        //         addFirst(item);
        //         this.size += 1;
        //     }
        // }


// addFirst ver.2
        // else if (this.first == 0 & this.rear == capacity){
        //     resize(); // resize
        //     addFirst(item);
        // }
        // else if (this.first == 0 & this.rear < capacity){
        //     this.first = capacity;
        //     this.items[this.first] = item;
        // }
        // else if (this.rear < this.first){
        //     if (this.rear < this.first - 1){
        //         this.items[this.rear + 1] = item;
        //     }
        //     else if (this.rear == this.first - 1){
        //         resize(); // resize
        //     }
        // }


// addLast
    // public void addLast(Item item){
    //     int capacity = this.items.length - 1;
    //     if (this.first == 0 & this.rear == capacity){
    //         resize(); // resize
    //         this.items[this.rear + 1] = item;
    //         this.rear += 1;
    //     }
    //     else if (this.first <= this.rear & this.rear < capacity){
    //         this.items[rear + 1] = item;
    //         this.rear += 1;
    //     }
    //     else if ((0 < this.first) & (this.first < this.rear) & (this.rear == capacity)){
    //         this.items[0] = item;
    //         this.rear = 0;
    //     }
    //     else if (this.rear < this.first){
    //         if (this.rear < this.first - 1){
    //             this.items[rear + 1] = item;
    //             this.rear += 1;
    //         }
    //         else if (this.rear == this.first - 1){
    //             resize(); //resize
    //         };
    //     }
    //     else if (isEmpty() & capacity > 0) {
    //         this.items[0] = item;
    //         this.first = 0;
    //         this.rear = 0;
    //     }
    //     this.size += 1;
    // }

// removeFirst
        // if (isEmpty()){
        //     return null;
        // }
        // else{
        //     Item returnItem = items[this.first]
        //     items[this.first] = null;
        //     if(this.first + 1 < rear){
        //         this.first += 1;
        //     }
        //     else if (this.first+ 1 == this.rear)|(this.first -1 == this.rear){
        //         this.first;
        //     }
        //     else{
        //         this.first -= 1;
        //     }
        // }



// private void resize(){
    //     Item[] newItems = (Item[]) new Object[this.items.length * RFACTOR];
    //     if (this.first < this.rear){
    //         System.arraycopy(this.items, this.first, newItems, 0, this.rear - this.first - 1); // 0じゃなく4にして、最後にitemを入れる
    //     }
    //     else {
    //         System.arraycopy(this.items, this.first, newItems, 0, this.items.length - this.first); // 0じゃなく4にして、最後にitemを入れる
    //         System.arraycopy(this.items, 0, newItems, this.items.length - this.first - 1, this.rear);
    //     }
    //     this.first = 0;
    //     this.rear = this.items.length - 1;
    //     this.items = newItems;
    //     // size += 1;
    //     // System.out.print("ResizeAddFirst Done!")
    // }

