package db.things;

import java.util.function.BinaryOperator;

/**
 * Created by hideyoshitakahashi on 3/3/17.
 */
public class WhereFunction {
    String operator;
    Function function;

    public WhereFunction(String operator, String t1, String t2){
        this.operator = operator;
        /*
        if ((t1.equals("int") && t2.equals("float")) || (t2.equals("int") && t1.equals("float"))
            || (t1.equals("float") && t2.equals("float"))) {
            FloatFunction();
        } else if ((t1.equals("int") && t2.equals("int"))){
            IntegerFunction();
        } else {
            StringFunction();
        }
        */
        if ((t1.equals("string") && t2.equals("string"))) {
            this.function = StringFunction();
        } else {
            this.function = FloatFunction();
        }
    }
    /*
    public ApplyFunction IntegerFunction() {
        BinaryOperator<Integer> f = (n1, n2) -> n1 - n2;
        return new ApplyFunction(f, this.operator);
    }
    */

    public StringComparator StringFunction() {
        StringComparator sc = new StringComparator(this.operator);
        return sc;
    }

    public ApplyFunction FloatFunction() {
        BinaryOperator<Float> f = (n1, n2) -> n1 - n2;
        return new ApplyFunction(f, this.operator);
    }


}

interface Function {
    default boolean finalApply(float diff, String operator) { // a compare to b  = -1  a < b ... a.compareTo(b) = -1
        if (operator.equals("==")) {
            return (diff == 0);
        } else if (operator.equals("!=")) {
            return (diff != 0);
        } else if (operator.equals("<")) {
            return (diff < 0);
        } else if (operator.equals("<=")) {
            return (diff <= 0);
        } else if (operator.equals(">")) {
            return (diff > 0);
        } else if (operator.equals(">=")) {
            return (diff >= 0);
        }
        return false; //////////////////////////////////////////////////////// CHECK!!!!!! ////////////////////////////////////////
    }
    boolean apply(String s1, String s2);
}

class StringComparator implements Function{
    String operator;
    public StringComparator(String operator){
        this.operator = operator;
    }
    public boolean apply(String s1, String s2){
        float diff = s1.compareTo(s2);
        return finalApply(diff, this.operator);
    }
}

class ApplyFunction implements Function{
    BinaryOperator<Float> function;
    String operator;
    public ApplyFunction(BinaryOperator<Float> func, String oper){
        this.function = func;
        this.operator = oper;
    }
    public boolean apply(String v1, String v2) {
        float fv1;
        float fv2;
        if (v1.equals("NaN") && v2.equals("NaN")) {
            fv1 = 0.0f;
            fv2 = 0.0f;
        } else if (v1.equals("NaN") && (!(v2.equals("NaN")))) {
            fv1 = 10.0f;
            fv2 = 0.0f;
        } else if ((!(v1.equals("NaN"))) && (v2.equals("NaN"))) {
            fv1 = 0.0f;
            fv2 = 10.0f;
        } else {
            fv1 = Float.parseFloat(v1);
            fv2 = Float.parseFloat(v2);
        }
        float diff = this.function.apply(fv1, fv2);
        return finalApply(diff, this.operator);
    }

}



/*
class Applier {
    String operator;
    int difference;
    public Applier(String operator, int difference){
        this.operator = operator;
        this.difference = difference;
    }
    private static boolean apply() {
        if (this.operator.equals("=")) {
            return (a == 0);
        } else if (this.operator.equals("!=")) {
            return (a != 0);
        } else if (this.operator.equals("<")) {
            return (a < 0);
        } else if (this.operator.equals("<=")) {
            return (a <= 0);
        } else if (this.operator.equals(">")) {
            return (a > 0);
        } else if (this.operator.equals(">=")) {
            return (a >= 0);
        } else if (this.operator.equals("<")) {
            return (a < 0);
        } else if (this.operator.equals("<")) {
            return (a <= 0);
        } else {
            return "ERROR : operator " + this.operator + " is not allowed."
        }

    }
}
*/


//    private String typeDeterminator(String type1, String type2){
//        if ((type1.equals("int")) && (type2.equals("int"))){
//            return "int";
//        } else if ((type1.equals("int")) && (type2.equals("float"))) {
//            return "";
//        } else if ((type1.equals("")) && (type2.equals(""))) {
//            return "";
//        } else if ((type1.equals("")) && (type2.equals(""))) {
//            return "";
//        }
//    }