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
        Init.init();
    }

    @Test
    public void testSigma() throws Exception{
        String expression = "sigma(1,5,\"ln(n)\",\"n\")";

        List<ExpressionToken> analyzed = ExpressionTokenAnalyzer.analyzeExpression(expression);
        List<ExpressionToken> compiled = ExpressionCompiler.compile(analyzed);

        double result = (double) ExpressionExecutor.executeCompiledExpression(compiled,null).getDataValue();
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
        double result = (double) ExpressionExecutor.executeCompiledExpression(compiled,null).getDataValue();
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

        Double result= (Double) ExpressionExecutor.executeCompiledExpression(compiled,null).getDataValue();
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
        double result= (double) ExpressionExecutor.executeCompiledExpression(parsed,null).getDataValue();

        System.out.println(result+"");
    }


}
