package db;
import Things.Parse;
import Things.Table;

import java.util.Map;
import java.util.HashMap;

public class Database {
    public static Map<String, Table> database  = new HashMap<String, Table>();

    // no constructor because fuck you that's why

    public static String transact(String query) {
        return Parse.eval(query);
    }

    public static void saveTable(Table t) {
        database.put(t.getName(), t);
    }

    public static boolean hasTable(String tableName){
        return Database.database.containsKey(tableName);
    }

    public static Table getTable(String tableName) {
        return database.get(tableName);
    }

    public static void removeTable(String tableName) {
        database.remove(tableName);
    }




//    public void add(Table table) {
//        temp.add(table);
//    }
}
