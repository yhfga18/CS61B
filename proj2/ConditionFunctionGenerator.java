//import db.Database;
//import db.things. *;
//
//import javax.xml.crypto.Data;
//import java.util.ArrayList;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by hideyoshitakahashi on 3/3/17.
// */
//public class validityTest { // condition validity
//
//    /*
//    public validityTest(String[] columnTitle, String[] tableName, String[] condition) {
//        columns = columnTitle;
//        tables = tableName;
//        cond = condition;
//        index = invalidColIndex(columns, tables, cond);
//    }
//    */
//
//    public static boolean isValidColumns(String[] columnTitle, String[] tableName, String[] condition) {
//        ArrayList<String> tableColumnNames = new ArrayList<String>();
//
//        // test if tables in database
//        for (String elem : tableName) {
//            if (!(Database.hasTable(elem))){
//                return false; // "ERROR: table called elem doesn't exist"
//            }
//        }
//        // now all the tables are guaranteed to be in database
//        // test if columns exist in these tables.
//        // 最初にthese tablesが持つすべてのcolumnのlistを作る。
//        for (String elem: tableName) {
//            Table theTable = Database.getTable(elem);
//            for (String colName : columnTitle) {
//                tableColumnNames.add(theTable.getExactColName(colName));
//            }
//        }
//        // 上で、対象になりうるすべてのコラムの名前をtableColumnName というArray List に格納した (type = String)
//        // その中に columns が含まれるか確認。
//        for (int i = 0; i < tableColumnNames.size(); i++) {
//            if (!(tableColumnNames.contains(columnTitle[i]))) {
//                return false; // "ERROR: column called columnTitle[i] doesn't exist"
//            }
//        }
//
//        // 最後にconditionに入ってるcolumnが上のtableColumnNameに含まれるか確認
//        for (){
//            getExactColName(condition[0])
//            condition[2]
//        }
//        return true;
//    }
//
//    private static boolean isValidOperation(String[] tableName, String[] condition) {
//        // test if tables in database
//        for (String elem : tableName) {
//            if (!(Database.hasTable(elem))){
//                return false; // "ERROR: table called elem doesn't exist"
//            }
//        }
//        // now all the tables are guaranteed to be in database
//        return true;
//
//    }
//
//    private static String singleTypeCheck(String str) {
//        if (str.contains("\'")) {
//            return "string";
//        } else if (str.contains(".")) {
//            return "float";
//        }
//        try {
//            Integer.parseInt(str);
//        } catch (NumberFormatException e) {
//            return "ERORR: Type of the operand is wrong";
//        }
//        return "int";
//    }
//
//    private static boolean isValidWhere(String[] condition){
//        String operators[] = {"=", "!=", "<", ">", "<=", ">="};
//        List<String> operatorsList = Arrays.asList(operators);
//        if (condition.length == 3 && operatorsList.contains(condition[1])){
//            if ( (!(operatorsList.contains(condition[0]))) && (!(operatorsList.contains(condition[0])))){
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    /*
//    if (!(isValidCondition(condition))){
//        return "ERROR: Malformed where in dealSelect in Dealer";
//    } else {
//        //WhereFunction conditionFunction = ConditionFunctionGenerator(columnTitle, tableName, condition);
//    }
//    */
//
//    /*
//    public String generateFunction(){
//        if (this.index > 0){
//            return "ERROR: There isn't column called " + this.columns[this.index] + " in apply() in ConditionFunctionGenerator";
//        }
//    }
//    */
//
//}
//
///*
//    private static boolean typeCheck(Table t, String values[]) {
//        String[] colTitles = t.getColumnName();
//        try {
//            for (int i = 0; i < colTitles.length; i++) {
//                if (colTitles[i].split(" ")[1].equals("int")) {
//                    if (!(values[i].equals("NOVALUE") || values[i].equals("NaN"))) {
//                        Integer.parseInt(values[i]);
//                    }
//                } else if (colTitles[i].split(" ")[1].equals("string")) {
//                    if (!values[i].contains("\'")) {
//                        throw new NumberFormatException("Erorr: wrong type");
//                    }
//                } else { // float type should be put
//                    if (!(values[i].equals("NOVALUE") || values[i].equals("NaN"))) {
//                        if (values[i].contains("\'")) {
//                            throw new NumberFormatException("Erorr: wrong type"); // must be string
//                        }
//                        if (!values[i].contains(".")) {
//                            throw new NumberFormatException("Erorr: wrong type"); // cannot be float
//                        }
//                        Float.parseFloat(values[i]);  // catch "abc"  no double quotation inside
//                    }
//                }
//            }
//        } catch (NumberFormatException e) {
//            return false;
//        }
//        return true;
//    }
