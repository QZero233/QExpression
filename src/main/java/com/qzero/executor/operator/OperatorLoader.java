package com.qzero.executor.operator;

import com.qzero.executor.BaseDataMate;
import com.qzero.executor.function.ExecutableAction;
import com.qzero.executor.function.IExecutableAction;
import com.qzero.executor.token.OperatorToken;

import java.util.HashMap;
import java.util.Map;

public class OperatorLoader {

    private static Map<String, ExecutableAction> operatorLoader=new HashMap<>();

    static {
        //Init the global operator map

        /**
         * For add
         */
        ExecutableAction actionForAdd=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {
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

                    @Override
                    public Class getReturnValueType() {
                        return Double.class;
                    }
                }
        ).addAction(
                new IExecutableAction() {
                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean value1= (Boolean) parameters[0].getDataValue();
                        Boolean value2= (Boolean) parameters[1].getDataValue();
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,value1||value2);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Boolean.class,Boolean.class};
                    }

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("+",actionForAdd);

        /**
         * For minus
         */
        ExecutableAction actionForMinus=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {
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

                    @Override
                    public Class getReturnValueType() {
                        return Double.class;
                    }
                }
        ).build();
        operatorLoader.put("-",actionForMinus);

        /**
         * For divide
         */
        ExecutableAction actionForDivide=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {
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

                    @Override
                    public Class getReturnValueType() {
                        return Double.class;
                    }
                }
        ).build();
        operatorLoader.put("/",actionForDivide);

        /**
         * For multiply
         */
        ExecutableAction actionForMultiply=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {
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

                    @Override
                    public Class getReturnValueType() {
                        return Double.class;
                    }
                }
        ).build();
        operatorLoader.put("*",actionForMultiply);

        /**
         * -----------------------------------------------------------------
         */

        ExecutableAction actionForEqualTo=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("==",actionForEqualTo);

        ExecutableAction actionForNotEqualTo=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

                    @Override
                    public int getParameterCount() {
                        return 2;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Boolean result=!((Double)parameters[0].getDataValue()).equals((Double) parameters[1].getDataValue());
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class,Double.class};
                    }

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("!=",actionForNotEqualTo);

        ExecutableAction actionForMoreThan=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put(">",actionForMoreThan);

        ExecutableAction actionForLessThan=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("<",actionForLessThan);

        ExecutableAction actionForMoreThanOrEqualTo=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put(">=",actionForMoreThanOrEqualTo);

        ExecutableAction actionForLessThanOrEqualTo=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("<=",actionForLessThanOrEqualTo);

        /**
         * ------------------------------------------------------------------------------------
         */

        ExecutableAction actionForNot=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("!",actionForNot);

        ExecutableAction actionForOr=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("||",actionForOr);

        ExecutableAction actionForAnd=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {

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

                    @Override
                    public Class getReturnValueType() {
                        return Boolean.class;
                    }
                }
        ).build();
        operatorLoader.put("&&",actionForAnd);

    }

    public static ExecutableAction getOperatorAction(String sign){
        if(sign==null)
            return null;
        return operatorLoader.get(sign);
    }

}
