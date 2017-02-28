package Things;

import db.Database;
import java.util.LinkedList;
import java.util.*;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by hideyoshitakahashi on 2/24/17.
 */
public class Dealer {

    public static String dealCreateTable(String tableName, String[] columnTitles){

        Table newTable = new Table(tableName, columnTitles);
        Database.saveTable(newTable);
        /*
        newTable.addNameRow(tableName);
        newTable.addItemRow(tableColumns);
        */
        return "";
    }

    public static String dealStore(String tableName){
        Table t = Database.getTable(tableName);
        // make a file for the table somehow.
        return "dealStore!!, Table t = " + tableName;
    }

    public static String dealLoad(String fileName){
//        Table newTable = new Table(fileName);
//        Database.saveTable(newTable);
//        // load the file somehow
//        return "dealStore!!, Table t = " + fileName;
        fileName = "examples/" + fileName;
        try{
            File file = new File(fileName + ".tbl");
            if (checkBeforeReadfile(file)){
                BufferedReader br = new BufferedReader(new FileReader(file));
                String[] columnTitles = br.readLine().split(",");
                Table newTable = new Table(fileName.split("/")[1], columnTitles);
                String str = br.readLine();
                while(str != null){
                    newTable.addRowLast(str.split(","));
                    str = br.readLine();
                }
                Database.saveTable(newTable);
                br.close();
            }else{
                System.out.println("couldn't open or find the file.");
            }
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
        return "";
    }

    private static boolean checkBeforeReadfile(File file){
        if (file.exists()){
            if (file.isFile() && file.canRead()){
                return true;
            }
        }
        return false;
    }


    public static String dealDrop(String tableName){
        Database.removeTable(tableName);
        return "";
    }

    public static String dealInsert(String tableName, String[] values){
        if (!(Database.hasTable(tableName))) {
            return "There isn't table called " + tableName + " in database...";
        }
        Table t = Database.getTable(tableName);
        t.addRowLast(values);

        return "";
    }

    public static String dealPrint(String tableName){
        if (Database.hasTable(tableName)) {
            Table t = Database.getTable(tableName);
            return t.toString();
        }
        // make a string to return. ここちゃんとできてない
        return "There isn't such the table";
    }

    public static String dealSelect(String[] columnTitle, String tableName, String condition) {
//        String columnTitle = m.group(1); // x ( -- different case for * !)
//        String tableName = m.group(2);   // T1
//        String condition = m.group(3);   // x > 2

        if (!(Database.hasTable(tableName))) {
            return "There isn't table called " + tableName + " in database...";
        }

        Table originalTable = Database.getTable(tableName);

        for (String elem : columnTitle) {
            if (!(Arrays.asList(originalTable.getColumnName()).contains(elem))){
                    return "There isn't a column called " + elem + " in " + tableName;
                }
        }

        LinkedList[] newCol;
        Table anonTable;
        if (columnTitle[0].equals("*")) {
            anonTable = new Table("anon", originalTable.getColumnName());
            newCol = new LinkedList[originalTable.getNumCol()];
        } else {
            anonTable = new Table("anon", columnTitle);
            newCol = new LinkedList[columnTitle.length];
        }
        for (int i = 0; i < newCol.length; i++) {
            newCol[i] = (LinkedList) originalTable.getColumn(columnTitle[i]);
            anonTable.addColumnLast(newCol[i]);  // specified column added to anon table
        }
        return anonTable.toString();
    }
/*
    public static void dealWhere(String condition) {

    }
*/
}

