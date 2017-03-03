package things;

import db.Database;
import java.util.LinkedList;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import java.text.DecimalFormat;


/*import com.sun.scenario.effect.impl.state.LinearConvolveKernel;*/
/*import com.sun.xml.internal.ws.api.ha.StickyFeature;*/
/*import sun.awt.image.ImageWatched;*/

import java.util.List;

import java.io.IOException;


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

    public static String dealStore(String tableName) {
        Table t = Database.getTable(tableName);

        if (t == null) {
            return "ERROR: There isn't table in database called " + tableName;
        }
        try{
            File file0 = new File(tableName + ".tbl");
            if (!file0.exists()) {
                file0.createNewFile();
            }
            FileWriter filewriter;
            if (checkBeforeWritefile(file0)) {
                filewriter = new FileWriter(file0);
                filewriter.write(t.toString());
                filewriter.close();
            } else {
                System.out.println("cannot write");
            }
        }catch(IOException e){
            System.out.println(e);
        }
        return "";
    }

    public static String dealLoad(String fileName) {
        try {

            File file = new File(fileName + ".tbl");
          /*  File tempFile1 = new File("examples/" + fileName + ".tbl"); */
            //checks if file exists in the current directory or examples
            if (!(checkBeforeReadfile(file))) {
                return "ERROR: .*"; //couldn't open or find the file.
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            // creates column titles by reading the first line
            String[] columnTitles = br.readLine().split("\\s*,\\s*");
            Table newTable = new Table(fileName, columnTitles);
            String str = br.readLine();
            // reads files line by line and adds rows
            while (str != null) {
                newTable.addRowLast(str.split("\\s*,\\s*"));
                str = br.readLine();
            }
            Database.saveTable(newTable);
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: " + e);
        } catch (IOException e) {
            System.out.println(e);
        }
        return "";
    }

// checks if the file exists and works
    private static boolean checkBeforeReadfile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkBeforeWritefile(File file) {
        if (file.isFile() && file.canWrite()) {
            return true;
        }
        return false;
    }


    public static String dealDrop(String tableName){
        Database.removeTable(tableName);
        return "";
    }

    public static String dealInsert(String tableName, String[] values) {
        if (!(Database.hasTable(tableName))) {
            return "There isn't table called " + tableName + " in database...";
        }
        Table t = Database.getTable(tableName);
        String[] colTitles = t.getColumnName();
        if (!typeCheck(t, values)) {
            return "Error: wrong type";
        }
        t.addRowLast(values);
        return "";
    }


    private static boolean typeCheck(Table t, String values[]) {
        String[] colTitles = t.getColumnName();
        try {
            for (int i = 0; i < colTitles.length; i++) {
                if (colTitles[i].split(" ")[1].equals("int")) {
                    if (!(values[i].equals("NOVALUE") || values[i].equals("NaN"))) {
                        Integer.parseInt(values[i]);
                    }
                } else if (colTitles[i].split(" ")[1].equals("string")) {
                    if (!values[i].contains("\"")) {
                        throw new NumberFormatException("Erorr: wrong type");
                    }
                } else { // float type should be put
                    if (!(values[i].equals("NOVALUE") || values[i].equals("NaN"))) {
                        if (values[i].contains("\"")) {
                            throw new NumberFormatException("Erorr: wrong type"); // must be string
                        }
                        if (!values[i].contains(".")) {
                            throw new NumberFormatException("Erorr: wrong type"); // cannot be float
                        }
                        Float.parseFloat(values[i]);  // catch "abc"  no double quotation inside
                    }
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    public static String dealPrint(String tableName){
        if (Database.hasTable(tableName)) {
            Table t = Database.getTable(tableName);
            return t.toString();
        }
        // make a string to return. ここちゃんとできてない
        return "ERROR: There isn't such the table called " + tableName + " from dealPrint in Dealer";
    }
    public static String dealSelect(String[] columnTitle,
                                    String[] tableName, String condition) {
//        String columnTitle = m.group(1); // x ( -- different case for * !)
//        String tableName = m.group(2);   // T1
//        String condition = m.group(3);   // x > 2

        // checks if such tables exist
        for (int i = 0; i < tableName.length; i++) {
            if (!(Database.hasTable(tableName[i]))) {
                return "There isn't table called " + tableName + " in database...";
            }
        }

        Table temp;
        int tableNameLen = tableName.length;

        // joins tables if there are at least two tables.
        // puts the result into temp.
        if (tableNameLen != 1) {
            while (true) {
                // if there two tables
                if (tableNameLen == 2) {
                    String[] newTableName = new String[]{tableName[0], tableName[1]};
                    temp = joinSelect(columnTitle, newTableName, condition);
                    break;
                    // if there are more than two table.
                    // uses recursion
                } else {
                    String[] newTableName = new String[]{tableName[0], tableName[1]};
                    joinSelect(columnTitle, newTableName, condition);
                    for (int i = 2; i < tableName.length; i++) {
                        tableName[i - 1] = tableName[i];
                    }
                    tableName[0] = "joinedTable";
                    tableNameLen -= 1;
                }
            }
            // if there is only one table.
            // no join
        } else {
            temp = Database.getTable(tableName[0]);
        }

        // if oolumn is *, uses all columns (original table itself)
        if (columnTitle[0].equals("*")) {
            return temp.toString();
        }

        String[] exactColTitle = new String[columnTitle.length];
        // puts exact column names into exactColTitle (x -> x int)
        for (int j = 0; j < columnTitle.length; j++) {
            String[] colChank = columnTitle[j].split("\\s* as \\s*");
            String name = temp.getExactColName(colChank[0]);
            if (colChank.length != 1) {         // x+y as w int
                try {
                    String type = colChank[1].split(" ")[1];
                    if (!(type.equals("int") || type.equals("float"))) {
                        return "Error: wrong type";
                    }
                    exactColTitle[j] = colChank[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    return "Error: wrong type";
                }
            } else if (name == null) {
                String[] array = containOperator(colChank[0]);
                if (array ==  null) {
                    return "no such column";
                }
                String firstType = temp.getExactColName(array[0]).split(" ")[1];
                String secondType = temp.getExactColName(array[2]).split(" ")[1];
                if (firstType.equals("string") || secondType.equals("string")) {
                    return "Error: wrong type";
                }
                if (firstType.equals("int") && secondType.equals("int")) {
                    exactColTitle[j] = colChank[0] + " int";
                } else {
                    exactColTitle[j] = colChank[0] + " float";
                }
            } else {
                exactColTitle[j] = name;
            }
        }

        // creates a new table
        Table anonTable = new Table("anon", exactColTitle);
        // sees the given columns one by one
        for (int k = 0; k < columnTitle.length; k++) {
            // checks if the given column name has operator
            // if it exits, separates it into three elements (see below)
            // if not, array = null
            String[] array = containOperator(columnTitle[k].split("\\s* as \\s*")[0]); // e.g. array = ["x", "+", "y"]
            Integer[] convertedInt = new Integer[temp.getNumRow()];
            Float[] convertedFloat = new Float[temp.getNumRow()];
            boolean flagFirst = false;   // e.g. x int -> flagFirst = true
            boolean flagSecond = false;  // e.g. y int -> flagSecond = true

            // if it has operator
            if (array != null) {
                if (temp.getExactColName(array[0]) == null) {
                    return "There isn't a column called " + array[0] + " in " + tableName;
                }
                if (temp.getExactColName(array[2]) == null) {
                    return "There isn't a column called " + array[2] + " in " + tableName;
                }

                // checks the type of x
                // converts string column into int or float column
                if (temp.checkType(array[0], "int")) {
                    convertedInt = convertInt(temp.getColumn(array[0]));
                    flagFirst = true;
                }
                if (temp.checkType(array[0], "float")) {
                    convertedFloat = convertFloat(temp.getColumn(array[0]));
                }

                // checks the type of y
                // converts string column into int or float column
                /* if x and y have the same type,
                   combines the the column created above with the new converted column
                 */
                // if not, creates another column
                if (temp.checkType(array[2], "int")) {
                    if (flagFirst) {
                        flagSecond = true;
                        Integer[] tempIntArray = convertInt(temp.getColumn(array[2]));
                        for (int t = 0; t < tempIntArray.length; t++) {
                            convertedInt[t] = operateInt(convertedInt[t],
                                    tempIntArray[t], array[1]);
                        }
                    } else {
                        convertedInt = convertInt(temp.getColumn(array[2]));
                    }
                }
                if (temp.checkType(array[2], "float")) {
                    if (!(flagFirst)) {
                        Float[] tempFloatArray = convertFloat(temp.getColumn(array[2]));
                        for (int t = 0; t < tempFloatArray.length; t++) {
                            convertedFloat[t] = operateFloat(convertedFloat[t],
                                    tempFloatArray[t], array[1]);
                        }
                    } else {
                        flagSecond = true;
                        convertedFloat = convertFloat(temp.getColumn(array[2]));
                    }
                }

                // Accoding to the types of x and y, add their columns into the new table
                // x int, y int
                if (flagFirst && flagSecond) {
                    anonTable.addColumnLast(convertIntToString(convertedInt));
                    // x int,  y float
                } else if (flagFirst && !flagSecond) {
                    Float[] tempNewCol = new Float[temp.getNumRow()];
                    for (int a = 0; a < tempNewCol.length; a++) {
                        tempNewCol[a] = operateFloatInt(convertedFloat[a], convertedInt[a], array[1], false);
                    }
                    anonTable.addColumnLast(convertFloatToString(tempNewCol));
                    // x float, y int
                } else if (!flagFirst && flagSecond) {
                    Float[] tempNewCol = new Float[temp.getNumRow()];
                    for (int b = 0; b < tempNewCol.length; b++) {
                        tempNewCol[b] = operateFloatInt(convertedFloat[b], convertedInt[b], array[1], true);
                    }
                    anonTable.addColumnLast(convertFloatToString(tempNewCol));
                    // x float, y float
                } else {
                    anonTable.addColumnLast(convertFloatToString(convertedFloat));
                }


                // if no operator
            } else {
                if (temp.getExactColName(columnTitle[k]) == null) {
                    return "There isn't a column called " + columnTitle[k] + " in " + tableName;
                }
                anonTable.addColumnLast(temp.getColumn(columnTitle[k]));
            }
        }

        for (int x = 0; x < anonTable.getNumRow(); x++) {
            String[] row = new String[anonTable.getNumCol()];
            for (int w = 0; w < anonTable.getNumCol(); w++) {
                row[w] = anonTable.getRow(w).get(w).toString();
            }
            if (!typeCheck(anonTable, row)) {
                return "Error; wrong type";
            }
        }
        return anonTable.toString();
    }    // converts float column to String column
    // if it has zero division error (represented as null in operateFloat or operateInt method),
    // put Nal
    private static String[] convertFloatToString(Float[] array) {
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                result[i] = "NaN";
            } else {
                result[i] = Float.toString(array[i]);
            }
        }
        return result;
    }

    // converts int column to String column
    // if it has zero division error (represented as null in operateFloat or operateInt method),
    // put Nal
    private static String[] convertIntToString(Integer[] array) {
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                result[i] = "NaN";
            } else {
                result[i] = Integer.toString(array[i]);
            }
        }
        return result;
    }

    // takes in float and int input.
    // changes the operatino oder depending on flag (e.g. flag = true -> a operator b)
    // accoding to the given operator, return the computed result as float number \
    // if it has zero division error, return null. This will be handled in convertFloatToString or convertToIntString
    private static Float operateFloatInt(Float a, Integer b, String operator, boolean flag) {
        if (operator.equals("+")) {
            return a + b;
        } else if (operator.equals("-")) {
            if (flag) {
                return a - b;
            }
            return b - a;
        } else if (operator.equals("*")) {
            return a * b;
        } else {
            if (flag) {
                if (b == 0) {
                    return null;
                }
                return a / b;
            }
            if (a == 0) {
                return null;
            }
            return b / a;
        }
    }

    // takes in int and int input.
    // accoding to the given operator, return the computed result as int number
    // if it has zero division error, return null. This will be handled in convertFloatToString or convertToIntString
    private static Integer operateInt(Integer a, Integer b, String operator) {
        if (operator.equals("+")) {
            return a + b;
        } else if (operator.equals("-")) {
            return a - b;
        } else if (operator.equals("*")) {
            return a * b;
        } else {
            if (b == 0) {
                return null;
            }
            return a / b;
        }
    }

    // takes in float and float input.
    // accoding to the given operator, return the computed result as float number
    // if it has zero division error, return null. This will be handled in convertFloatToString or convertToIntString
    private static Float operateFloat(Float a, Float b, String operator) {
        if (operator.equals("+")) {
            return a + b;
        } else if (operator.equals("-")) {
            return a - b;
        } else if (operator.equals("*")) {
            return a * b;
        } else {
            if (b == 0) {
                return null;
            }
            return a / b;
        }
    }

    // converts String column to int column
    // if it has NOVALUE, changes it to 0
    private static Integer[] convertInt(List<String> p) {
        Integer[] array = new Integer[p.size()];
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i) == "NOVALUE") {
                array[i] = 0;
            } else {
                array[i] = Integer.parseInt((p.get(i)));
            }
        }
        return array;
    }

    // converts String column to float column
    // if it has NOVALUE, changes it to 0.0f
    private static Float[] convertFloat(List<String> p) {
        Float[] array = new Float[p.size()];
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i) == "NOVALUE") {
                array[i] = 0.0f;
            } else {
                array[i] = Float.parseFloat(String.format(p.get(i)));
            }
        }
        return array;
    }


    // checks if the given String array has empty space
    private static boolean containsSpace(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(" ")) {
                return true;
            }
        }
        return false;
    }

    // check if the string has any operator
    // handles both cases where "x + y" and "x+y"
    private static String[] containOperator(String str) {
        String[] array;
        if (!containsSpace(str.split(""))) {
            array = str.split("");
        } else {
            array = str.split(" ");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("+") || array[i].equals("-") || array[i].equals("*") || array[i].equals("/")) {
                return array;
            }
        }
        return null;
    }

    // joins two tables
    private static Table joinSelect(String[] columnTitle, String[] tableName, String condition) {
        Table tempTableP =  Database.getTable(tableName[0]);
        Table tempTableQ = Database.getTable(tableName[1]);
        String[] tempColNameP = tempTableP.getColumnName();
        String[] tempColNameQ = tempTableQ.getColumnName();
        // creates temporary jointed column. Duplicated one will be droped
        String[] newColTitle = combine(tempColNameP, tempColNameQ);

        // creates a new table
        // This will be the result
        Table joited = new Table("joinedTable", newColTitle);

        LinkedList<String> pRowContents;
        LinkedList<String> qRowContents;
        // adds all possible rows
        // if there are invalid rows, doesn't add it (e.g. jointed table has two x column, but dont't have common values)
        for (int pRow = 0; pRow < tempTableP.getNumRow(); pRow++) {
            pRowContents = tempTableP.getRow(pRow);
            for (int qRow = 0; qRow < tempTableQ.getNumRow(); qRow++) {
                qRowContents = tempTableQ.getRow(qRow);
                // check its validity
                if (check(tempTableP, pRowContents, tempTableQ, qRowContents)) {
                    joited.addRowLast(catenate(pRowContents, qRowContents));
                }
            }
        }
        // drops duplicated column
        for (int i = 0; i < joited.getNumCol(); i++) {
            for (int indexToBeDeleted = i + 1; indexToBeDeleted < joited.getNumCol(); indexToBeDeleted++){
                if (joited.getColumnName()[i].equals(joited.getColumnName()[indexToBeDeleted])) {
                    joited.removeColmnTitle(indexToBeDeleted);
                    joited.removeColumn(indexToBeDeleted);
                }
            }
        }
        // saves it as "jointedTable" in db just in case where it will be call somewhere
        Database.saveTable(joited);

        return joited;
    }

    // combines two columns to single one
    // allows duplicated names
    private static String[] combine(String[] p, String[] q) {
        String[] result = new String[p.length + q.length];
        for (int i = 0; i < p.length; i++) {
            result[i] = p[i];
        }
        for (int k = 0; k < q.length; k++) {
            result[p.length + k] = q[k];
        }
        return result;
    }

    // connects two LinkedList and returns its result
    // not destructive
    private static LinkedList<String> catenate(LinkedList<String> p, LinkedList<String> q) {
        LinkedList<String> result = new LinkedList();
        for (int i = 0; i < p.size(); i++) {
            result.addLast(p.get(i)); {
            }
        }
        for (int k = 0; k < q.size(); k++) {
            result.addLast(q.get(k));
        }
        return result;
    }

    //checks if two columns have common value
    private static boolean check(Table pTable, LinkedList<String> p, Table qTable, LinkedList<String> q) {
        //specify two columns
        for (int categoryIndexOfP = 0; categoryIndexOfP < p.size(); categoryIndexOfP++) {
            for (int categoryIndexOfQ = 0; categoryIndexOfQ < q.size(); categoryIndexOfQ++) {
                //check if they have the same name
                if (pTable.getColumnName()[categoryIndexOfP].equals(qTable.getColumnName()[categoryIndexOfQ])) {
                    //check if they have the same value for specific rows(indexOfP and indexOfQ)
                    if (!(p.get(categoryIndexOfP).equals(q.get(categoryIndexOfQ)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

/*
    public static void dealWhere(String condition) {

    }
*/
}

