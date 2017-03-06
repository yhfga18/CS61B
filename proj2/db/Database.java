package db;
import db.things.Table;

import java.util.Map;
import java.util.HashMap;

public class Database {
    private Map<String, Table> database  = new HashMap<String, Table>();

    // no constructor because fuck you that's why

    public String transact(String query) {
        return db.things.Parse.eval(query, this);
    }

    public String saveTable(Table t) {
        if (hasTable(t.getName())) {
            return ""; // "ERROR: Table called " + t.getName() + " already exist";
        } else {
            database.put(t.getName(), t);
            return "";
        }
    }

    public boolean hasTable(String tableName) {
        return database.containsKey(tableName);
    }

    public Table getTable(String tableName) {
        if (hasTable(tableName)) {
            return database.get(tableName);
        }
        return null;
    }

    public String removeTable(String tableName) {
        if (!(hasTable(tableName))) {
            return "ERROR: Table called " + tableName + " does not exist.";
        } else {
            database.remove(tableName);
            return "";
        }
    }

}
