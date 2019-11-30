package com.qzero.executor.token;

import com.qzero.executor.BaseDataMate;
import com.qzero.executor.function.ExecutableAction;
import com.qzero.executor.function.IExecutableAction;
import com.qzero.executor.operator.OperatorLoader;

/**
 * Class of operator token
 * @author QZero
 * @version 1.0
 */
public class OperatorToken extends OperationalToken {

    public static final int LEVEL_VERY_LOW=0;
    public static final int LEVEL_LOW=1;
    public static final int LEVEL_HIGH=2;
    public static final int LEVEL_HIGHEST=3;

    public enum OperatorType{
        OPERATOR_TYPE_ADD("+",LEVEL_LOW),
        OPERATOR_TYPE_MINUS("-",LEVEL_LOW),
        OPERATOR_TYPE_MULTIPLY("*",LEVEL_HIGH),
        OPERATOR_TYPE_DIVIDE("/",LEVEL_HIGH),
        OPERATOR_TYPE_EQUAL_TO("==",LEVEL_VERY_LOW),
        OPERATOR_TYPE_NOT_EQUAL_TO("!=",LEVEL_VERY_LOW),
        OPERATOR_TYPE_MORE_THAN(">",LEVEL_VERY_LOW),
        OPERATOR_TYPE_LESS_THAN("<",LEVEL_VERY_LOW),
        OPERATOR_TYPE_MORE_THAN_OR_EQUAL_TO(">=",LEVEL_VERY_LOW),
        OPERATOR_TYPE_LESS_THAN_OR_EQUAL_TO("<=",LEVEL_VERY_LOW),
        OPERATOR_TYPE_AND("&&",LEVEL_VERY_LOW),
        OPERATOR_TYPE_OR("||",LEVEL_VERY_LOW),
        OPERATOR_TYPE_NOT("!",LEVEL_VERY_LOW);

        private String sign;
        private int level;
        OperatorType(String operatorSign,int level){
            this.sign=operatorSign;
            this.level=level;
        }

    };

    private int operatorLevel;
    private OperatorType type;

    public OperatorToken(String tokenString, int operatorLevel, OperatorType type, ExecutableAction action) {
        super(tokenString,action);
        this.operatorLevel = operatorLevel;
        this.type = type;
    }

    /**
     * Get operator token from operator string
     * @param operatorSign
     * @return Operator token object,null if failed
     */
    public static OperatorToken getOperatorToken(String operatorSign){
       ExecutableAction action= OperatorLoader.getOperatorAction(operatorSign);
       if(action==null)
           return null;

       OperatorType[] types=OperatorType.values();
       for(OperatorType type:types){
           if(type.sign.equals(operatorSign)){
               OperatorToken operatorToken=new OperatorToken(operatorSign,type.level,type,action);
               return operatorToken;
           }
       }

       return null;
    }

    public int getOperatorLevel() {
        return operatorLevel;
    }

    public void setOperatorLevel(int operatorLevel) {
        this.operatorLevel = operatorLevel;
    }

    public OperatorType getType() {
        return type;
    }

    public void setType(OperatorType type) {
        this.type = type;
    }

    public ExecutableAction getAction() {
        return action;
    }

    public void setAction(ExecutableAction action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "OperatorToken{" +
                "operatorLevel=" + operatorLevel +
                ", type=" + type +
                ", tokenString='" + tokenString + '\'' +
                '}';
    }
}
