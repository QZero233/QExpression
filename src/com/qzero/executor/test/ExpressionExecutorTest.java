package com.qzero.executor.test;

import com.qzero.executor.*;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import org.junit.Assert;
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

                double start= (double) parameters[0];
                double end= (double) parameters[1];
                String expression= (String) parameters[2];
                String unknownName= (String) parameters[3];

                List<ExpressionToken> analyzed=ExpressionTokenAnalyzer.analyzeExpression(expression);
                List<ExpressionToken> compiled=ExpressionCompiler.compile(analyzed);

                double sum=0;
                for(int i = (int) start; i<end; i++){
                    double tmp=ExpressionExecutor.executeCompiledExpression(compiled,null);
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
    public void testSigma() throws Exception{
        String expression = "sigma(1,5,\"ln(n)\",\"n\")";

        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);

        double result = ExpressionExecutor.executeCompiledExpression(compiled,null);
        System.out.println(String.format("%.4f", result));

        String json=ExpressionCompiler.exportToJsonString(compiled);
        String latex=ExpressionLatexTranslator.translateToLatex(ExpressionCompiler.parseFromJsonString(json));
        System.out.println(latex);
    }

    @Test
    public void testExecutor() throws Exception {
        String expression = "sin(pi)+2*cos(x)+ln(e)";
        //VariableLoader.addOrEditVariable("y", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_STRING, "233"));
        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);


        ExpressionExecutor.check(compiled);
        double result = ExpressionExecutor.executeCompiledExpression(compiled,null);
        System.out.println(String.format("%.4f", result));
    }

    @Test
    public void testLatex() throws Exception{
        String expression = "sqrt((4*pow(pi,2)*pow(r,3))/(G*M))";
        //VariableLoader.addOrEditVariable("x", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, Math.PI));
        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);

        String latex=ExpressionLatexTranslator.translateToLatex(compiled);
        System.out.println(latex);
    }

    @Test
    public void testCalculate() throws Exception{
        String expression = "sqrt((4*pow(pi,2)*pow(r,3))/(G*M))";

        //VariableLoader.addOrEditVariable("M", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, 5.965E24));
       // VariableLoader.addOrEditVariable("r", new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE, 384403E3));

        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);

        ExpressionExecutor.check(compiled);

        Double result=ExpressionExecutor.executeCompiledExpression(compiled,null);
        System.out.println(result/(60*60*24));
    }

    @Test
    public void testJson(){
        //Compile
        String expression="sin(pi)+2*cos(x)";
       // VariableLoader.addOrEditVariable("x",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,Math.PI));
        List<ExpressionToken> analyzed= ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled= ExpressionCompiler.compile(analyzed);

        String json=ExpressionCompiler.exportToJsonString(compiled);
        System.out.println(json);

        List<ExpressionToken> parsed=ExpressionCompiler.parseFromJsonString(json);
        double result=ExpressionExecutor.executeCompiledExpression(parsed,null);

        System.out.println(result+"");
    }

    @Test
    public void testSimplify(){
        String expression="2+1+x*2*3+pow(x,2)";//3+7+4
        //VariableLoader.addOrEditVariable("x",new BaseDataMate(BaseDataMate.DataType.DATA_TYPE_DOUBLE,2D));
        List<ExpressionToken> analyzed= ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled= ExpressionCompiler.compile(analyzed);
        List<ExpressionToken> simplest=ExpressionCompiler.simplify(compiled);

        double result = ExpressionExecutor.executeCompiledExpression(simplest,null);
        Assert.assertEquals(result,14,0);
        System.out.println(String.format("%.4f", result));
    }

}
