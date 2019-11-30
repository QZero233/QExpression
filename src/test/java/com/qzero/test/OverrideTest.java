package com.qzero.test;

import com.qzero.executor.BaseDataMate;
import com.qzero.executor.ExpressionExecutor;
import com.qzero.executor.ExpressionLatexTranslator;
import com.qzero.executor.ExpressionToken;
import com.qzero.executor.function.ExecutableAction;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.function.IExecutableAction;
import com.qzero.executor.variable.VariableEnv;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OverrideTest {

    @Before
    public void init(){
        Init.init();
    }

    @Test
    public void testOverrideOperator(){
        String expression="(x+y)&&z";
        List<ExpressionToken> compiled=Init.getCompiled(expression);

        VariableEnv env=new VariableEnv(new HashMap<>());
        env.addOrEditVariable("x",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,true));
        env.addOrEditVariable("y",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,false));
        env.addOrEditVariable("z",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_BOOLEAN,false));

        System.out.println(ExpressionExecutor.executeCompiledExpression(compiled,env));
        //System.out.println(ExpressionLatexTranslator.translateToLatex(compiled));
    }

    @Test
    public void testOverrideFunction(){
        ExecutableAction executableAction=new ExecutableAction.Builder().addAction(
                new IExecutableAction() {
                    @Override
                    public int getParameterCount() {
                        return 1;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Double result=(Double) parameters[0].getDataValue();
                        result++;
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,result);
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Double.class};
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
                        return 1;
                    }

                    @Override
                    public BaseDataMate execute(BaseDataMate[] parameters) {
                        Date date= (Date) parameters[0].getDataValue();
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.YEAR,1);
                        return new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DATE,calendar.getTime());
                    }

                    @Override
                    public Class[] getParametersType() {
                        return new Class[]{Date.class};
                    }

                    @Override
                    public Class getReturnValueType() {
                        return Date.class;
                    }
                }
        ).build();

        FunctionLoader.addFunction("addOneS",executableAction);

        String expression="addOneS(x)";
        List<ExpressionToken> compiled=Init.getCompiled(expression);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        VariableEnv env=new VariableEnv(new HashMap<>());
        env.addOrEditVariable("x",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DATE,calendar.getTime()));

        System.out.println(ExpressionExecutor.executeCompiledExpression(compiled,env));

    }

}
