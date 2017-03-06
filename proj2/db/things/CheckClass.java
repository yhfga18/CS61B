package db.things;


public class CheckClass {
    private Table table;
    private String firstElem;
    private String secondElem;


    public CheckClass(String[] array, Table table) {
        this.firstElem = array[0];
        this.secondElem = array[2];
        this.table = table;
    }

    public boolean isFirstElemColumn() {
        String result = this.table.getExactColName(this.firstElem);
        if (result == null) {
            return false;
        }
        return true;
    }

    public boolean isSecondElemColumn() {
        String result = this.table.getExactColName(this.secondElem);
        if (result == null) {
            return false;
        }
        return true;
    }

    public String getFirstElemType() {
        if (!isFirstElemColumn()) {
            return singleTypeChecker(this.firstElem);
        }
        String exactTitle = this.table.getExactColName(this.firstElem);
        return exactTitle.split(" ")[1];
    }

    public String getSecondElemType() {
        if (!isSecondElemColumn()) {
            return singleTypeChecker(this.secondElem);
        }
        String exactTitle = this.table.getExactColName(this.secondElem);
        return exactTitle.split(" ")[1];
    }

    public String getSecondElem() {
        return this.secondElem;
    }

    public String singleTypeChecker(String str) {
        if (str.contains("\'")) {
            return "string";
        } else if (str.contains(".")) {
            return "float";
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return "ERROR: Type of the operand is wrong in singleTypeChecker in CheckClass";
        }
        return "int";
    }

    public int getIntSecondElem() {
        if (isSecondElemColumn()){
            System.out.println("ERROR: when you use getIntSecondElem, the second elem should not be column name");
        } else if (!getSecondElemType().equals("int")) {
            System.out.print("ERROR: when you use getIntSecondElem, the second elem should be int");
        }
        return Integer.parseInt(this.secondElem);
    }

    public float getFloatSecondElem() {
        if (isSecondElemColumn()){
            System.out.println("ERROR: when you use getFloatSecondElem, the second elem should not be column name");
        } else if (!getSecondElemType().equals("float")) {
            System.out.print("ERROR: when you use getFloatSecondElem, the second elem should be float");
        }
        return Float.parseFloat(this.secondElem);
    }

    public String getStringSecondElem() {
        if (isSecondElemColumn()){
            System.out.println("ERROR: when you use getFloatSecondElem, the second elem should not be column name");
        } else if (!getSecondElemType().equals("string")) {
            System.out.print("ERROR: when you use getStringSecondElem, the second elem should be string");
        }
        return this.secondElem.substring(1,this.secondElem.length()-1);
    }
}

