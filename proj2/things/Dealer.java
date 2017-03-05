package things;

/*import com.sun.scenario.effect.impl.state.LinearConvolveKernel;*/
/*import com.sun.xml.internal.ws.api.ha.StickyFeature;*/
import db.Database;
/*import sun.awt.image.ImageWatched;*/

/*import java.util.ArrayList; */
import java.util.LinkedList;
import java.util.List;
/*import java.util.*; */

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
/* import java.util.StringJoiner; */


public class Dealer {

    public static String dealCreateTable(String tableName, String[] columnTitles) {
        Table newTable = new Table(tableName, columnTitles);
        if (!isTable(newTable)) {
            return "ERROR: invalid column type";
        }
        Database.saveTable(newTable);
        /*
        newTable.addNameRow(tableName);
        newTable.addItemRow(tableColumns);
        */
        return "";
    }

    public static String dealStore(String tableName) {
        if (!Database.hasTable(tableName)) {
            return "ERROR: such table does not exist";
        }
        Table t = Database.getTable(tableName);
        if (!isTable(t)) {
            return "ERROR: this is not a valid table";
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
                return "ERROR: couldn't open or find the file"; //couldn't open or find the file.
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
            if (!isTable(newTable)) {
                return "ERROR: invalid column type or invalid contents";
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


    public static String dealDrop(String tableName) {
        if (!Database.hasTable(tableName)) {
            return "ERROR: such table does not exist";
        }
        Database.removeTable(tableName);
        return "";
    }

    public static String dealInsert(String tableName, String[] values) {
        if (!(Database.hasTable(tableName))) {
            return "There isn't table called " + tableName + " in database...";
        }
        Table t = Database.getTable(tableName);
        if (values.length != t.getNumCol()) {
            return "ERROR: the number of inputs is invalid";
        }
        if (!typeCheck(t, values)) {
            return "ERROR: wrong type";
        }
        t.addRowLast(values);
        return "";
    }




    private static boolean isTable(Table t) {
        if (isValidColumn(t)) {
            for (int i = 0; i < t.getNumRow(); i++) {
                String[] rowArray = new String[t.getNumCol()];
                for (int k = 0; k < t.getNumCol(); k++) {
                    rowArray[k] = (String) t.getRow(i).get(k);
                }
                if (!typeCheck(t, rowArray)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isValidColumn(Table t) {
        for (int i = 0; i < t.getNumCol(); i++) {
            String[] array = t.getColumnName()[i].split("\\s* \\s*");
            if (array.length == 1) {
                return false;
            }
            String type = array[1];
            if (!(type.equals("int") || type.equals("float") || type.equals("string"))) {
                return false;
            }
        }
        return true;
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
                    if (!(values[i].contains("\'") || values[i].equals("NOVALUE"))) {
                        throw new NumberFormatException("ERROR: wrong type");
                    }
                } else { // float type should be put
                    if (!(values[i].equals("NOVALUE") || values[i].equals("NaN"))) {
                        if (values[i].contains("\'")) {
                            throw new NumberFormatException("ERROR: wrong type"); // must be string
                        }
                        if (!values[i].contains(".")) {
                            throw new NumberFormatException("ERROR: wrong type"); // cannot be float
                        }
                        Float.parseFloat(values[i]);  // catch "abc"  no double quotation inside
                    }
                }
            }
        } catch (NumberFormatException e) {
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public static String dealPrint(String tableName) {
        if (Database.hasTable(tableName)) {
            Table t = Database.getTable(tableName);
            return t.toString();
        }
        // make a string to return. ここちゃんとできてない
        return "ERROR: .*";
    }
    public static String dealSelect(String[] columnTitle,
                                    String[] tableName, String[][] conditions) {
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
                    temp = joinSelect(columnTitle, newTableName);
                    break;
                    // if there are more than two table.
                    // uses recursion
                } else {
                    String[] newTableName = new String[]{tableName[0], tableName[1]};
                    joinSelect(columnTitle, newTableName);
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

// Where を行う
        Table anonT = new Table("anon", temp.getColumnName());
        for (int i = 0; i < temp.getNumRow(); i++) {
            anonT.addRowLast(temp.getRow(i));
        }
        // これで anonT は temp と名前以外全く同じ内容のテーブルになった。これに対してforでwhereのconditionで引っかかる部分を抜いていく

        if (conditions != null) {
            for (String[] condition : conditions) {
                if (condition[0] == null) {
                    break;
                }
                String errorString = whereHandle(anonT, condition);
                if (errorString.length() > 1) {
                    return errorString;
                }
            }
        } else {
            anonT = temp;
        }



        // if column is *, uses all columns (original table itself)
        if (columnTitle[0].equals("*")) {
            return anonT.toString();
        }


        String[] exactColTitle = new String[columnTitle.length];
        // puts exact column names into exactColTitle (x -> x int)
        for (int j = 0; j < columnTitle.length; j++) {
            String[] colChank = columnTitle[j].split("\\s* as \\s*");
            String name = temp.getExactColName(colChank[0]);
            if (colChank.length != 1) {         // x+y as w int
                String[] array = containOperator(colChank[0]);
                if (array == null) {
                    return "ERROR: already has column name";
                }
                if (temp.getExactColName(array[0]) == null) {
                    return "ERROR: first element should be a column";
                }
                String firstType = temp.getExactColName(array[0]).split(" ")[1];
                if (temp.getExactColName(array[2]) ==  null) {
                    String result = singleTypeCheck(array[2]);
                    if (result.equals("int") || result.equals("float") || result.equals("string")) {
                        exactColTitle[j] = colChank[1] + " " + firstType;
                    } else {
                        return "ERROR: such column does not exists";
                    }
                } else {
                    String secondType = temp.getExactColName(array[2]).split(" ")[1];
                    String newType;
                    if (firstType.equals("int") && secondType.equals("int")) {
                        newType = "int";
                    } else if (firstType.equals("string")) {
                        newType = "string";
                    } else {
                        newType = "float";
                    }
                    exactColTitle[j] = colChank[1] + " " + newType;
                }
            } else if (name == null) {
                String[] array = containOperator(colChank[0]);
                if (array ==  null) {
                    return "ERROR: no such column";
                }
                return "ERROR: needs column name";
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
            Integer[] convertedInt = new Integer[anonT.getNumRow()];
            Float[] convertedFloat = new Float[anonT.getNumRow()];
            String[] convertedString = new String[anonT.getNumRow()];

            // if it has operator
            if (array != null) {
                CheckClass checker = new CheckClass(array, anonT);
                if (!checker.isFirstElemColumn()) {
                    return "ERROR: First operand should be column name or such column name does\'t exit";
                }
                if (!checker.isSecondElemColumn()) {
                    checker.getSecondElemType();
                }


                // checks the type of x
                // converts string column into int or float column
                if (checker.getFirstElemType().equals("int")) {
                    convertedInt = convertInt(anonT.getColumn(array[0]));
                } else if (checker.getFirstElemType().equals("float")) {
                    convertedFloat = convertFloat(anonT.getColumn(array[0]));
                } else  {
                    convertedString = convertString(anonT.getColumn(array[0]));
                }

                // checks the type of y
                // converts string column into int or float column
                /* if x and y have the same type,
                   combines the the column created above with the new converted column
                 */
                // if not, creates another column


                // second int
                if (checker.getSecondElemType().equals("int")) {
                    if (checker.getFirstElemType().equals("string")) {
                        return "ERROR: does not match types";
                    }
                    if (checker.getFirstElemType().equals("int")) {    // int, int
                        if (!checker.isSecondElemColumn()) {
                            for (int r = 0; r < convertedInt.length; r++) {
                                convertedInt[r] = operateInt(convertedInt[r], checker.getIntSecondElem(), array[1]);
                            }
                            anonTable.addColumnLast(convertIntToString(convertedInt));
                        } else {
                            Integer[] tempIntArray = convertInt(anonT.getColumn(array[2]));
                            for (int t = 0; t < tempIntArray.length; t++) {
                                convertedInt[t] = operateInt(convertedInt[t],
                                        tempIntArray[t], array[1]);
                            }
                            anonTable.addColumnLast(convertIntToString(convertedInt));
                        }


                    } else {                                         // float, int
                        if (!checker.isSecondElemColumn()) {
                            for (int r = 0; r < convertedInt.length; r++) {
                                convertedFloat[r] = operateFloatInt(convertedFloat[r], checker.getIntSecondElem(), array[1], true);
                            }
                            anonTable.addColumnLast(convertIntToString(convertedInt));
                        } else {
                            convertedInt = convertInt(anonT.getColumn(array[2]));
                            Float[] tempNewCol = new Float[anonT.getNumRow()];
                            for (int b = 0; b < tempNewCol.length; b++) {
                                tempNewCol[b] = operateFloatInt(convertedFloat[b], convertedInt[b], array[1], true);
                            }
                            anonTable.addColumnLast(convertFloatToString(tempNewCol));
                        }
                    }

                    // second float
                } else if (checker.getSecondElemType().equals("float")) {
                    if (checker.getFirstElemType().equals("string")) {
                        return "ERROR: does not match types";
                    }
                    if (checker.getFirstElemType().equals("float")) { //foat, float
                        if (!checker.isSecondElemColumn()) {
                            for (int r = 0; r < convertedInt.length; r++) {
                                convertedFloat[r] = operateFloat(convertedFloat[r], checker.getFloatSecondElem(), array[1]);
                            }
                            anonTable.addColumnLast(convertFloatToString(convertedFloat));
                        } else {
                            Float[] tempFloatArray = convertFloat(anonT.getColumn(array[2]));
                            for (int t = 0; t < tempFloatArray.length; t++) {
                                convertedFloat[t] = operateFloat(convertedFloat[t],
                                        tempFloatArray[t], array[1]);
                            }
                            anonTable.addColumnLast(convertFloatToString(convertedFloat));
                        }
                    } else {                                          // int, float
                        if (!checker.isSecondElemColumn()) {
                            for (int r = 0; r < convertedInt.length; r++) {
                                convertedFloat[r] = operateFloat(convertedFloat[r], checker.getFloatSecondElem(), array[1]);
                            }
                            anonTable.addColumnLast(convertFloatToString(convertedFloat));
                        } else {
                            convertedFloat = convertFloat(anonT.getColumn(array[2]));
                            Float[] tempNewCol = new Float[anonT.getNumRow()];
                            for (int a = 0; a < tempNewCol.length; a++) {
                                tempNewCol[a] = operateFloatInt(convertedFloat[a], convertedInt[a], array[1], false);
                            }
                            anonTable.addColumnLast(convertFloatToString(tempNewCol));
                        }
                    }


                } else {                                           // second is String
                    if (!checker.getFirstElemType().equals("string")) {
                        return "ERROR: does not match types";
                    }                                              // string, string
                    if (!array[1].equals("+")) {
                        return "ERROR: only stirng + string is allowed";
                    }
                    if (!checker.isSecondElemColumn()) {
                        for (int e = 0; e < convertedString.length; e++) {
                            convertedString[e] = stringAddition(convertedString[e], checker.getStringSecondElem());
                        }
                        anonTable.addColumnLast(convertedString);
                    } else {
                        String[] secondString = convertString(anonT.getColumn(array[2]));
                        for (int e = 0; e < convertedString.length; e++) {
                            convertedString[e] = stringAddition(convertedString[e], secondString[e]);
                        }
                        anonTable.addColumnLast(convertedString);
                    }

                }


                // if no operator
            } else {
                if (anonT.getExactColName(columnTitle[k].split("\\s* as \\s*")[0]) == null ) {
                    return "There isn't a column called " + columnTitle[k] + " in " + tableName;
                }
                anonTable.addColumnLast(anonT.getColumn(columnTitle[k]));
            }
        }

        for (int x = 0; x < anonTable.getNumRow(); x++) {
            String[] row = new String[anonTable.getNumCol()];
            for (int w = 0; w < anonTable.getNumCol(); w++) {
                row[w] = anonTable.getRow(x).get(w).toString();
            }
            if (!typeCheck(anonTable, row)) {
                return "Error; wrong type";
            }
        }


        return anonTable.toString();
    }



    private static String singleTypeCheck(String str) {
        if (str.contains("\'")) {
            return "string";
        } else if (str.contains(".")) {
            return "float";
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return "ERROR: Type of the operand is wrong or such column does not exist";
        }
        return "int";
    }

    // converts float column to String column
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

    private static String[] convertStringToString(String[] array) {
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                result[i] = "NaN";
            } else {
                result[i] = "'" + array[i] + "'";
            }
        }
        return result;
    }

    // takes in float and int input.
    // changes the operatino oder depending on flag (e.g. flag = true -> a operator b)
    // according to the given operator, return the computed result as float number
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

    private static String[] convertString(List<String> p) {
        String[] array = new String[p.size()];
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).equals("NOVALUE")) {
                array[i] = "";
            } else {
                array[i] = p.get(i).substring(1, p.get(i).length() - 1);
            }
        }
        return array;
    }

    // converts String column to int column
    // if it has NOVALUE, changes it to 0
    private static Integer[] convertInt(List<String> p) {
        Integer[] array = new Integer[p.size()];
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).equals("NOVALUE")) {
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
            if (p.get(i).equals("NOVALUE")) {
                array[i] = 0.0f;
            } else {
                array[i] = Float.parseFloat(p.get(i));
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


    private static String stringAddition(String a, String b) {
        String a0;
        String b0;
        String result;
        if (a.equals("NOVALUE")) {
            a0 = "";
        } else {
            a0 = a;
        }
        if (b.equals("NOVALUE")) {
            b0 = "";
        } else  {
            b0 = b;
        }
        result = a0 + b0;
        return "\'" + result + "\'";
    }

    // check if the string has any operator
    // handles both cases where "x + y" and "x+y"

    public static String[] containOperator(String str) {
        String[] array;
        String[] result = new String[3];
        if (hasPlus(str)) {
            array = str.split("[+]");
            result[0] = array[0].trim();
            result[2] = array[1].trim();
            result[1] = "+";
            return result;
        } else if (hasMinus(str)) {
            array = str.split("-");
            result[0] = array[0].trim();
            result[2] = array[1].trim();
            result[1] = "-";
            return result;
        } else if (hasMultiplication(str)) {
            array = str.split("[*]");
            result[0] = array[0].trim();
            result[2] = array[1].trim();
            result[1] = "*";
            return result;
        } else if (hasDivision(str)) {
            array = str.split("/");
            result[0] = array[0].trim();
            result[2] = array[1].trim();
            result[1] = "/";
            return result;
        }
        return null;
    }

    private static boolean hasPlus(String str) {
        String[] array = str.split("");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("+")) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasMinus(String str) {
        String[] array = str.split("");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("-")) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasMultiplication(String str) {
        String[] array = str.split("");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("*")) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasDivision(String str) {
        String[] array = str.split("");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("/")) {
                return true;
            }
        }
        return false;
    }




    // joins two tables
    private static Table joinSelect(String[] columnTitle, String[] tableName) {
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
            for (int indexToBeDeleted = i + 1; indexToBeDeleted < joited.getNumCol(); indexToBeDeleted++) {
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




    private static String whereHandle(Table anonT, String[] condition) {
        String operator = condition[1];
        String column1 = condition[0];
        String column2 = condition[2];
        Integer secondIntElem = null;
        Float secondFloatElem = null;
        String secondStringElem = null;

        CheckClass checker0 = new CheckClass(condition, anonT);
        if (!(checker0.isFirstElemColumn())) {
            return "ERROR: there isn't a column called " + column1;
        } else if (!(checker0.isSecondElemColumn())) { // second が normal value (non column)
            String type1 = checker0.getFirstElemType();
            String type2 = checker0.getSecondElemType();
            if (type2.equals("int")) {
                if (type1.equals("String")) {
                    return "ERROR: int and String cannot be compared";
                }
                secondIntElem = checker0.getIntSecondElem();
            } else if (type2.equals("float")) {
                if (type1.equals("String")) {
                    return "ERROR: float and String cannot be compared";
                }
                secondFloatElem = checker0.getFloatSecondElem();
            } else {
                if (type1.equals("int") || type1.equals("float")) {
                    return "ERROR: String and number cannot be compared";
                }
                secondStringElem = checker0.getStringSecondElem();
            }
            WhereFunction wf = new WhereFunction(operator, type1, type2);
            // function func をここで作って、first columnに対してfor loop
            for (int i = 0; i < anonT.getNumRow(); i++) {
                List<String> colValues = anonT.getColumn(column1);
                if (secondFloatElem != null) {
                    String a = colValues.get(i);
                    if (a.equals("NOVALUE") || a.equals("NaN")) {
                        a = "0.0f";
                    }
                    if (!((wf.function.apply(a, secondFloatElem.toString())))) {
                        anonT.removeRow(i);
                        i -= 1;
                    }
                } else if (secondIntElem != null) {
                    String a = colValues.get(i);
                    if (a.equals("NOVALUE") || a.equals("NaN")) {
                        a = "0";
                    }
                    if (!(wf.function.apply(a, secondIntElem.toString()))) {
                        anonT.removeRow(i);
                        i -= 1;
                    }
                } else if (secondStringElem != null) {
                    String a = colValues.get(i);
                    if (a.equals("NOVALUE") || a.equals("NaN")) {
                        a = "";
                        if (!(wf.function.apply(a, secondStringElem.toString()))) {
                            anonT.removeRow(i);
                            i -= 1;
                        }
                    } else if (!(wf.function.apply(a.substring(1, a.length() - 1), secondStringElem.toString()))) {
                        anonT.removeRow(i);
                        i -= 1;
                    }
                }
            }

        } else if (checker0.isSecondElemColumn()) {
            String type1 = checker0.getFirstElemType();
            String type2 = checker0.getSecondElemType();
            if (type2.equals("int")) {
                if (type1.equals("String")) {
                    return "ERROR: int and String cannot be compared";
                }
            } else if (type2.equals("float")) {
                if (type1.equals("String")) {
                    return "ERROR: float and String cannot be compared";
                }
            } else {
                if (type1.equals("int") || type1.equals("float")) {
                    return "ERROR: String and number cannot be compared";
                }
            }
            WhereFunction wf = new WhereFunction(operator, type1, type2);
            for (int i = 0; i < anonT.getNumRow(); i++) {
                List<String> col1Values = anonT.getColumn(column1);
                List<String> col2Values = anonT.getColumn(column2);
                if (!(wf.function.apply(col1Values.get(i), col2Values.get(i)))) {
                    anonT.removeRow(i);
                    i -= 1;
                }
            }
        }
        return "";
    }

/*
    public static void dealWhere(String condition) {

    }
*/
}

