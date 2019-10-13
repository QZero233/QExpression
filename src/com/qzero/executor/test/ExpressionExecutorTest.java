package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import com.qzero.executor.variable.VariableLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ExpressionExecutorTest {

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
                return 0;
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
    public void testExecutor() throws Exception {
        String expression = "sin(pi)+2*cos(x)+ln(e)";
        VariableLoader.addOrEditVariable("x", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, Math.PI));
        //VariableLoader.addOrEditVariable("y", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_STRING, "233"));
        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);


        ExpressionExecutor.check(compiled,true);
        double result = ExpressionExecutor.executeCompiledExpression(compiled);
        System.out.println(String.format("%.4f", result));
    }

    @Test
    public void testLatex() throws Exception{
        String expression = "sqrt((4*pow(pi,2)*pow(r,3))/(G*M))";
        VariableLoader.addOrEditVariable("x", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, Math.PI));
        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);

        String latex=ExpressionLatexTranslator.translateToLatex(compiled);
        System.out.println(latex);
    }

    @Test
    public void testCalculate() throws Exception{
        String expression = "sqrt((4*pow(pi,2)*pow(r,3))/(G*M))";

        VariableLoader.addOrEditVariable("M", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, 5.965E24));
        VariableLoader.addOrEditVariable("r", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, 384403E3));

        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);

        ExpressionExecutor.check(compiled,true);

        Double result=ExpressionExecutor.executeCompiledExpression(compiled);
        System.out.println(result/(60*60*24));
    }

}
