package things;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.StringJoiner;


public class Parse {
    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");

    /*create table が patternで、それをpattern.compileしたものは
    Javaがプログラムの中で使うための、Java用のpatternだと思えばいいらしい
    一応俺らから見て、CREAT_CMD は pattern. (an object)
    上のCREATE_CMDとかLOAD_CMDとかを pattern object と呼ぶそうです

     Matcher class は存在するけど、それはPattern classで
     定義されているmatcher メソッドを使って生成するらしい

     次は、上で生成したパターンオブジェクトがターゲットとなる文字列を対象に
     マッチしているかどうかを調べるためにマッチャを使う
    */

    // main method was here now copied/pasted at the end of the program

    /* mactherの使い方：
    （1）
    Matcher m = p.matcher(str)
    ここで str は対象となるString, p はJava用のpattern. patternのmethodであるmatcherをつかって
    Matcher object を生成した。

    （２）
    たぶん上のmにたいして.matchesで、実際にmatchしてるかどうかを調べている。

     */

    public static String eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            //System.out.println("group 1 ____ : " + m.group(1)); // T1
            return loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            // System.err.printf("Malformed query: %s\n", query);
            return "Malformed!!!";
        }
    }

    // create table afterSmith as select * from names where last > 'Smith'
    // select first + last as whole from afterSmith

    // create table seasonRatios as select City,Season,Wins/Losses as Ratio from teams,records

    private static String createTable(String expr) {
        // expr = T1 (x int, y int), for create table T1 (x int, y int)
        // expr = T1 as select * from T2 where x > y
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) { // new table を作る
            //System.out.println("in createTable method, ");
            //System.out.println("group1 is : " + m.group(1)); // T1
            //System.out.println("group2 is : " + m.group(2));
            //System.out.println(m.group(2).split(COMMA));
            String Tname = m.group(1);
            String[] Tcolumns = m.group(2).split(COMMA);
            return Dealer.dealCreateTable(Tname, Tcolumns);

        } else if ((m = CREATE_SEL.matcher(expr)).matches()) { // selectを含む
            // *** createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
            //create table T1 as select x from T2 where x > y
            /*
            System.out.println("m.group1 = " + m.group(1)); // T1
            System.out.println("m.group2 = " + m.group(2)); // x
            System.out.println("m.group3 = " + m.group(3)); // T2
            System.out.println("m.group4 = " + m.group(4)); // x > y
            */
            String newTableName = m.group(1); // T2
            String[] columnTitle;
            String newColTitle;
            System.out.println(m.group(2));
            if (m.group(2).contains("as")) {
                String[] temp = m.group(2).split("\\s* as \\s*");
                columnTitle = temp[0].split("\\s*,\\s*");
                newColTitle = temp[1];
            } else {
                columnTitle = m.group(2).split("\\s*,\\s*"); // x
                newColTitle = null;
            }
     /*       String[] columnName = m.group(2).split("\\s*, \\s*"); // x, y ... */
            String[] originalTableName = m.group(3).split("\\s*, \\s*"); // T1
            String condition = m.group(4); // x > 2
            String result = Dealer.dealSelect(columnTitle, originalTableName, condition, newColTitle); // handling select
            String[] s = result.split("\n"); // putting string repr into array
            //for (String elem : s) {System.out.println("elem!! : " + elem); }
            //System.out.println("s.length == " + s.length);
            //for (String elem : s){System.out.println("s elem! : " + elem);}
            String[] NewcolumnName = java.util.Arrays.copyOfRange(s, 0, 1);
            String[] s2 = java.util.Arrays.copyOfRange(s, 1, s.length);
            //for (String elem : s2){System.out.println("s2 elem! : " + elem);}

            Dealer.dealCreateTable(newTableName, NewcolumnName);
            for (String elem : s2) {
                Dealer.dealInsert(newTableName, elem.split(", "));
            }
            return Dealer.dealPrint(newTableName);

            //String result2 = eval(result);
            //System.out.println("result2 be like ... : " + result2);
//            return Dealer.dealCreateTable(newTableName, result2);

        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }
        return expr;
    }
    /* Printing stuffs info
    for "" create table T1 (x int, y int) "" ,
    System.out.println("in createTable method, ");
    System.out.println("group1 is : " + m.group(1)); // group1 is T1 (table's name)
    System.out.println("group2 is : " + m.group(2)); // x int, y int (かっこの中)
    System.out.println(m.group(2).split(COMMA)); // location info

     */


    /* createTable
    ここでは general な意味でcreate table に対応している。この中で、そのcreate tableの restが
        (1) select を含む → createSelectedTable();
        (2) select を含まない → createNewTable();
    に飛ばすか決めて飛ばした先でそれぞれにappropriateな操作を施す。
    */

    private static String createNewTable(String name, String[] cols) {
        // name = table's name,
        // cols = rest of the input
        StringJoiner joiner = new StringJoiner(", "); // package String Joiner
        for (int i = 0; i < cols.length-1; i++) {
            joiner.add(cols[i]);
        }

        String colSentence = joiner.toString() + " and " + cols[cols.length-1];
        System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);
        // System.out.println(cols[0]); // x int
        // System.out.println(cols[1]); // y int

        // うえのStringJoiner joiner から "You are tring..." まで全部適当。動くように適当に
        // 与えられてるだけ。以下からメインのコード

        // things.Dealer に飛ばす
        //String result = things.Dealer.dealCreateTable(name, cols);

        return name;

        /* format:
        create table <table name> (<column0 name> <type0>, <column1 name> <type1>, ...)
         つまり上のcolにはcolumn name と type が入りまくってる。
         */



    }


    private static String createSelectedTable(String name, String exprs, String tables, String conds) {
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        return name;
    }

    private static String loadTable(String fileName) {
        // System.out.printf("You are trying to load the table named %s\n", name);

        // load a file called name, somehow.
        System.out.println("LOAD fileName is: " + fileName);
        return Dealer.dealLoad(fileName);
    }

    private static String storeTable(String name) {
        System.out.printf("You are trying to store the table named %s\n", name);
        return Dealer.dealStore(name);
        // パソコンにどうにかして入れ戻す (String file にする iteration でやればいい)

    }

    private static String dropTable(String name) {
        System.out.printf("You are trying to drop the table named %s\n", name);
        return Dealer.dealDrop(name);

    }

    private static String insertRow(String expr) {
        /*
        original = insert into tname values (1,2,3,4)
        expr = tname values (1,2,3,4)
         */
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);
            return null;
        } else {
            //System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
            //System.out.println("m.group1, m.group2 are ...... : " + m.group(1) + " and " + m.group(2));
            String tableName = m.group(1);
            String[] values = m.group(2).split(COMMA);
            /*
            for (int i = 0; i < values.length; i++) {
                System.out.println("values[" + i +"]: " + values[i]);
            }
            */
            if (values[0].contains("(")) {
                values[0] = values[0].substring(1, values[0].length());
            }

            int len = values.length;

            if (values[len - 1].contains(")")) {
                values[len - 1] = values[len - 1].substring(0, values[len - 1].length()-1);

            }
            //System.out.println("value[0]: " + values[0] + ", ... values[0]: " + values[1] + ", ... values[2]: " + values[2] + " ...DONE!");
            return Dealer.dealInsert(tableName, values);
        }
    }

    private static String printTable(String name) {
        //System.out.printf("You are trying to print the table named %s\n", name);
        return Dealer.dealPrint(name);
    }

    private static String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
        }
        String[] columnTitle;
        String newColTitle;
        System.out.println(" m.group1 = " + m.group(1) + "  m.group2 = " +  m.group(2) + "  m.group3 = "+ m.group(3));
        // m.group3 = condition.
        if (m.group(1).contains("as")) {
            String[] temp = m.group(1).split("\\s* as \\s*");
            columnTitle = temp[0].split("\\s*,\\s*");
            newColTitle = temp[1];
        } else {
            columnTitle = m.group(1).split("\\s*,\\s*"); // x
            newColTitle = null;
        }
        String[] tableName = m.group(2).split("\\s*,\\s*");   // T1
        String condition = m.group(3);   // x > 2
        for (int i = 0; i < columnTitle.length; i++) {
            System.out.println("columnTitle[" + i +"]: " + columnTitle[i]);
            System.out.println("tableName[" + i +"]: " + tableName[i]);
        }

        // change
        //if (m.group(3) == null) {
        //    return Dealer.dealSelect(columnTitle, tableName, condition, newColTitle);
        //} else {
          //  return Dealer.dealSelect(columnTitle, tableName, condition, newColTitle, m.group(3));

        //}

        return Dealer.dealSelect(columnTitle, tableName, condition, newColTitle);
        // select x from T1 where x > 2 をした時は、
        // group1 = x つまり columnのtitle
        // group2 = T1 つまり table の名前
        // group3 = x > 2 つまり whereの中
    }

    private static void commaSeparator(String str) {
        for (int i = 0; i < str.length(); i++) {

        }
    }
    /*
    private static String select(String exprs, String tables, String conds) {
        System.out.printf("You are trying to select these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
        return exprs;
    }
    */
}





//    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.err.println("Expected a single query argument");
//            return;
//        }
//
//        eval(args[0]);
//    }




//    public static String evalCreateTable(String rest){
//        /*
//        rest: <table name> (<column0 name> <type0>, <column1 name> <type1>, ...)
//        or
//        rest: <table name> as <select clause> // SELECT!!
//         */
//
//        return rest;
//    }
//
//    public static String evalLoad(String rest){
//        /*
//        rest:
//         */
//        return "evaluating rest of load, which is ... " + rest + "!!";
//    }
//    public static String evalStore(String rest){
//        return "evalStore!";
//    }
//    public static String evalDrop(String rest){
//        return "evalDrop!";
//    }
//    public static String evalInsertRow(String rest){
//        return "evalRow!";
//    }
//    public static String evalPrint(String rest){
//        return "evalPrint!";
//    }
//    public static String evalSelect(String rest){
//        return "evalSelect!";
//    }





