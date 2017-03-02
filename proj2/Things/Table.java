package things;
import java.util.*;


public class Table<T> {

    private String name;
    private ArrayList<LinkedList<T>> zeroColumn;
    private LinkedList<String> zeroRow;
    private int columnCap;

    public Table() {
        this("anonymous", new String[0]); //this("", new String[0]) ?
    }

    public Table(String name) {
        this(name, new String[0]);
    }

    public Table(String name, String[] columnTitles) {
        this.name = name;
        this.zeroColumn = new ArrayList<LinkedList<T>>(10);
        this.columnCap = 10;
        this.zeroRow = new LinkedList<String>();
        for (String s : columnTitles) {
            zeroRow.add(s);
        }
//        System.out.println("zeroRow is now: ");
//        System.out.println(zeroRow.toString());
        // size() method でelement(row)の個数はわかる！
    }

    public String[] getColumnName(){
        return this.zeroRow.toArray(new String[0]);
    }

    public String getName(){
        return this.name;
    }

    public int getNumCol() {return  this.zeroRow.size();}

    public int getNumRow() { return this.zeroColumn.size(); }

    public boolean isFull(){
        return this.zeroColumn.size() >= this.columnCap;
    }

    public String getExactColName(String colName) {
        String[] array = getColumnName();
        for (int i = 0; i < this.zeroRow.size(); i++) {
            if (array[i].split(" ")[0].equals(colName)) {
                return this.zeroRow.get(i);
            }
        }
 /*       System.out.println("no such column"); */
        return null;
    }

    public boolean checkType(String colName, String type) {
        if (getExactColName(colName).split(" ")[1].equals(type)) {
            return true;
        }
        return false;
    }

    public boolean hasColumnType(String str) {
        String[] colTitles = getColumnName();
        for (int i = 0; i < colTitles.length; i++) {
            if (colTitles[i].split(" ")[1].equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void deleteColumn(int i) {
        this.zeroRow.remove(i);
        return;
    }

    // trimToSize() method でarrayListのcontainer減らせる


    // add row or column

    public void addRowLast(List<T> list) {
        LinkedList<T> newRow = new LinkedList<T>();
        for (T elem : list) {
            newRow.add(elem);
        }
        if (this.isFull()) {
            this.columnCap = this.columnCap * 2;
            this.zeroColumn.ensureCapacity(this.columnCap);
        }
        this.zeroColumn.add(newRow);
    }

    public void addRowLast(T[] list) {
        List<T> valueList = Arrays.asList(list);
        addRowLast(valueList);
    }


    public void addColumnLast(List<T> list) {
        if (this.zeroColumn.size() < 1) {
            for (int i = 0; i < list.size(); i++) {
                LinkedList<T> theList = new LinkedList<T>();
                theList.add(list.get(i));
                this.zeroColumn.add(i, theList);
            }
        } else {
            for (int i = 0; i < this.zeroColumn.size(); i++) {
                LinkedList<T> theList = this.zeroColumn.get(i);
                theList.add(list.get(i));
                this.zeroColumn.set(i, theList);
                this.toString();
            }
        }
    }

    public void addColumnLast(T[] list) { // add to the back
        List<T> valueList = Arrays.asList(list);
        addColumnLast(valueList);
    }

    // remove column or row

    public void removeRow(int i){
        // Index Error check needed
        this.zeroColumn.remove(i);
    }

    public void removeColumn(int i){
        for (int j = 0 ; j < this.zeroColumn.size() ; j++){
            LinkedList theRow = this.zeroColumn.get(j);
            theRow.remove(i);
            this.zeroColumn.set(j, theRow);
        }
    }

    public void removeColmnTitle(int i) {
        this.zeroRow.remove(i);
        return;
    }

    // get column or row

    public LinkedList<T> getRow(int ith) {
        return this.zeroColumn.get(ith);
    }

    public List<T> getColumn(int ith) { // for index number
        List<T> returnList = new LinkedList<T>();
        for (int i = 0 ; i < this.zeroColumn.size(); i++) {
            LinkedList<T> value = this.zeroColumn.get(i);
            T realValue = value.get(ith);
            returnList.add(realValue);
        }
        return returnList;
    }

    private boolean contain(String str0, String str1) {
        if (str0.split("\\s* \\s*")[0].equals(str1)) {
            return true;
        }
        return false;
    }

    public List<T> getColumn(String columnName) { // for column name
        int counter = 0;
        for (int i = 0; i < zeroRow.size(); i++) {
            if ((contain(zeroRow.get(i), columnName))) {
                counter = i;
                break;
            }
        } // searching the index number for the given column name.
        return getColumn(counter);
    }

    public String toString(){
//        LinkedList<T> stringList = new LinkedList();
        String returnString = "";
        for (int i = 0; i < this.zeroRow.size() - 1; i ++) {
            returnString = returnString + this.zeroRow.get(i) + ",";
        }
        returnString = returnString + this.zeroRow.get(this.zeroRow.size() - 1) + "\n";
        for (LinkedList elem : this.zeroColumn) {
            for (int i = 0; i < elem.size() - 1; i++) {
                returnString = returnString + elem.get(i) + ",";
            }
            returnString = returnString + elem.get(elem.size() - 1) + "\n";
        }
        return returnString;
    }
}

   /*
    private class Row {
        private List row;
        private List somePointer;

        public Row(){
            this.row = new LinkedList<T>(null);
            this.somePointer = null;
        }

        // column specialized methods come here
    }

    private class Column {
        private List column;
        private List somePointer;

        public Column(){
            this.column = new ArrayList<T>(null);
            this.somePointer = null;

        }
        // column specialized methods come here
    }

    */

    /*
    public void addColumnLast(List<T> list) {
        System.out.println("in addColumnLast method...: input list is : " + list);
        System.out.println("zeroColumn.size() is : " + this.zeroColumn.size());
        for (int i = 0; i < list.size(); i++) {
            LinkedList<T> theList = new LinkedList<T>();
            theList.add(list.get(i));
            System.out.println("list.get(i) is : " + list.get(i));
            System.out.println("theList is : " + theList);
            this.zeroColumn.set(i, theList);
            System.out.println();
            this.toString();
        }
    }
    */



