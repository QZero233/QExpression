package com.qzero.executor.token;

/**
 * Class of operator token
 * @author QZero
 * @version 1.0
 */
public class OperatorToken extends TokenObject {

    public static final int LEVEL_LOW=1;
    public static final int LEVEL_HIGH=2;
    public static final int LEVEL_HIGHEST=3;

    public enum OperatorType{
        OPERATOR_TYPE_ADD,
        OPERATOR_TYPE_MINUS,
        OPERATOR_TYPE_MULTIPLY,
        OPERATOR_TYPE_DIVIDE,
    };

    private int operatorLevel;
    private OperatorType type;
    private ExecutableActionForOperator action;

    private OperatorToken(String tokenString, int operatorLevel, OperatorType type,ExecutableActionForOperator action) {
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
                return new OperatorToken(operatorSign, LEVEL_LOW, OperatorType.OPERATOR_TYPE_ADD, new ExecutableActionForOperator() {
                    @Override
                    public int[] getParametersCounts() {
                        return new int[]{1,2};
                    }

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public double execute(Object[] parameters) {
                        if(parameters.length==1)
                            return (Double)parameters[0];
                        else if(parameters.length==2)
                            return  ((Double)parameters[0])+((Double) parameters[1]);
                        else
                            throw new IllegalArgumentException("Operator add must have one or two parameters");
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "-":
                return new OperatorToken(operatorSign, LEVEL_LOW, OperatorType.OPERATOR_TYPE_MINUS, new ExecutableActionForOperator() {
                    @Override
                    public int[] getParametersCounts() {
                        return new int[]{1,2};
                    }

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public double execute(Object[] parameters) {
                        if(parameters.length==1)
                            return -(Double)parameters[0];
                        else if(parameters.length==2)
                            return  ((Double)parameters[0])-((Double) parameters[1]);
                        else
                            throw new IllegalArgumentException("Operator minus must have one or two parameters");
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "*":
                return new OperatorToken(operatorSign, LEVEL_HIGH, OperatorType.OPERATOR_TYPE_MULTIPLY, new ExecutableActionForOperator() {
                    @Override
                    public int[] getParametersCounts() {
                        return new int[]{2};
                    }

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public double execute(Object[] parameters) {
                        return ((Double)parameters[0])*((Double) parameters[1]);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }
                });
            case "/":
                return new OperatorToken(operatorSign, LEVEL_HIGH, OperatorType.OPERATOR_TYPE_DIVIDE, new ExecutableActionForOperator() {
                    @Override
                    public int[] getParametersCounts() {
                        return new int[]{2};
                    }

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public double execute(Object[] parameters) {
                        return ((Double)parameters[0])/((Double) parameters[1]);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
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

    public ExecutableActionForOperator getAction() {
        return action;
    }

    public void setAction(ExecutableActionForOperator action) {
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
