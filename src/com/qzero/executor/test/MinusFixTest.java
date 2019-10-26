package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import com.qzero.executor.variable.VariableLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MinusFixTest {

    @Before
    public void loadTestData() {
        ExecutableAction sinAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.sin((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };

        ExecutableAction cosAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.cos((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };

        ExecutableAction sigmaAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 4;
            }

            @Override
            public double execute(Object[] parameters) {

                double start= (double) parameters[0];
                double end= (double) parameters[1];
                String expression= (String) parameters[2];
                String unknownName= (String) parameters[3];

                List<ExpressionToken> analyzed= ExpressionTokenAnalyzer.analyzeExpression(expression);
                List<ExpressionToken> compiled= ExpressionCompiler.compile(analyzed);

                double sum=0;
                for(int i = (int) start; i<end; i++){
                    VariableLoader.addOrEditVariable(unknownName,new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,(double)i));
                    double tmp= ExpressionExecutor.executeCompiledExpression(compiled);
                    sum+=tmp;
                }


                return sum;
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class,Double.class,String.class,String.class};
            }
        };

        ExecutableAction sqrtAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.sqrt((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };

        ExecutableAction powAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 2;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.pow((Double) parameters[0],(Double) parameters[1]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class,Double.class};
            }
        };

        ExecutableAction lnAction = new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return Math.log((Double) parameters[0]);
            }

            @Override
            public Class[] getParametersType() {
                return new Class[]{Double.class};
            }
        };


        FunctionLoader.addFunction("sin", sinAction);
        FunctionLoader.addFunction("cos", cosAction);
        FunctionLoader.addFunction("ln", lnAction);
        FunctionLoader.addFunction("sigma", sigmaAction);
        FunctionLoader.addFunction("pow", powAction);
        FunctionLoader.addFunction("sqrt", sqrtAction);

        ConstantLoader.addConstant("pi", Math.PI);
        ConstantLoader.addConstant("e", Math.E);
        ConstantLoader.addConstant("G", 6.67E-11);


    }

    @Test
    public void testFixMinus(){

        //Circle: x^2+y^2=1 -> y=-sqrt(1-pow(x,2))

        String expression="-sqrt(1-pow(x/2,2))";

        List<ExpressionToken> analyzed=ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled=ExpressionCompiler.compile(analyzed);

        ExpressionExecutor.check(compiled,false);

        String latex=ExpressionLatexTranslator.translateToLatex(compiled);
        System.out.println(latex);

        //double result=ExpressionExecutor.executeCompiledExpression(compiled);
        //System.out.println(result+"");
    }

}
