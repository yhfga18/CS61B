package db;
import things.Parse;
import things.Table;

import java.util.Map;
import java.util.HashMap;

public class Database {
    public static Map<String, Table> database  = new HashMap<String, Table>();

    // no constructor because fuck you that's why

    public static String transact(String query) {
        return things.Parse.eval(query);
    }

    public static void saveTable(Table t) {
        if (hasTable(t.getName())) {
            System.out.println("ERROR: Table called " + t.getName() + " does not exist.");
        } else {
            database.put(t.getName(), t);
        }
    }

    public static boolean hasTable(String tableName){
        return Database.database.containsKey(tableName);
    }

    public static Table getTable(String tableName) {
        if (hasTable(tableName)) {
            return database.get(tableName);
        }
        return null;
    }

    public static void removeTable(String tableName) {
        database.remove(tableName);
    }

}
