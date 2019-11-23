package com.qzero.executor.token;

import com.qzero.executor.BaseDataMate;

/**
 * Class of operator token
 * @author QZero
 * @version 1.0
 */
public class OperatorToken extends TokenObject {

    public static final int LEVEL_VERY_LOW=0;
    public static final int LEVEL_LOW=1;
    public static final int LEVEL_HIGH=2;
    public static final int LEVEL_HIGHEST=3;

    public enum OperatorType{
        OPERATOR_TYPE_ADD,
        OPERATOR_TYPE_MINUS,
        OPERATOR_TYPE_MULTIPLY,
        OPERATOR_TYPE_DIVIDE,
        OPERATOR_TYPE_EQUAL_TO,
        OPERATOR_TYPE_MORE_THAN,
        OPERATOR_TYPE_LESS_THAN,
        OPERATOR_TYPE_MORE_THAN_OR_EQUAL_TO,
        OPERATOR_TYPE_LESS_THAN_OR_EQUAL_TO,
        OPERATOR_TYPE_AND,
        OPERATOR_TYPE_OR,
        OPERATOR_TYPE_NOT,
    };

    private int operatorLevel;
    private OperatorType type;
    private ExecutableAction action;

    private OperatorToken(String tokenString, int operatorLevel, OperatorType type,ExecutableAction action) {
        super(tokenString);
        this.operatorLevel = operatorLevel;
        this.type = type;
        this.action=action;
    }

    /**
     * Get operator token from operator string
     * @param operatorSign
     * @return Operator token object,null if failed
     */
    public static OperatorToken getOperatorToken(String operatorSign){
        switch (operatorSign){
            case "+":
                return new OperatorToken(operatorSign, LEVEL_LOW, OperatorType.OPERATOR_TYPE_ADD, new ExecutableAction() {
                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Double result=((Double)parameters[0].getDataValue())+((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "-":
                return new OperatorToken(operatorSign, LEVEL_LOW, OperatorType.OPERATOR_TYPE_MINUS, new ExecutableAction() {
                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Double result=((Double)parameters[0].getDataValue())-((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,result);
                    }


                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "*":
                return new OperatorToken(operatorSign, LEVEL_HIGH, OperatorType.OPERATOR_TYPE_MULTIPLY, new ExecutableAction() {
                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Double result=((Double)parameters[0].getDataValue())*((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "/":
                return new OperatorToken(operatorSign, LEVEL_HIGH, OperatorType.OPERATOR_TYPE_DIVIDE, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Double result=((Double)parameters[0].getDataValue())/((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "==":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_EQUAL_TO, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=((Double)parameters[0].getDataValue()).equals((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case ">":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_MORE_THAN, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=((Double)parameters[0].getDataValue())>((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "<":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_LESS_THAN, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=((Double)parameters[0].getDataValue())<((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case ">=":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_MORE_THAN_OR_EQUAL_TO, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=((Double)parameters[0].getDataValue())>=((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "<=":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_LESS_THAN_OR_EQUAL_TO, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=((Double)parameters[0].getDataValue())<=((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "!":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_NOT, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 1;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=!((Boolean)parameters[0].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Boolean.class};
                    }
                });
            case "||":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_OR, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=((Boolean)parameters[0].getDataValue())||((Boolean)parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Boolean.class,Boolean.class};
                    }
                });
            case "&&":
                return new OperatorToken(operatorSign, LEVEL_VERY_LOW, OperatorType.OPERATOR_TYPE_AND, new ExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=((Boolean)parameters[0].getDataValue())&&((Boolean)parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Boolean.class,Boolean.class};
                    }
                });
            default:
                return null;
        }
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
