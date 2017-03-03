package db;
import things.Table;

import java.util.Map;
import java.util.HashMap;

public class Database {
    private static Map<String, Table> database  = new HashMap<String, Table>();

    // no constructor because fuck you that's why

    public static String transact(String query) {
        return things.Parse.eval(query);
    }

    public static String saveTable(Table t) {
        if (hasTable(t.getName())) {
            return "ERROR: Table called " + t.getName() + " does not exist.";
        } else {
            database.put(t.getName(), t);
            return "";
        }
    }

    public static boolean hasTable(String tableName) {
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
