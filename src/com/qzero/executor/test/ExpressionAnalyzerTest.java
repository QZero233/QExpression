package com.qzero.executor.test;

import com.qzero.executor.ExpressionReader;
import com.qzero.executor.ExpressionToken;
import com.qzero.executor.ExpressionTokenAnalyzer;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.ExecutableAction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ExpressionAnalyzerTest {

    @Before
    public void loadTestData(){
        ExecutableAction emptyAction=new ExecutableAction() {
            @Override
            public int getParameterCount() {
                return 1;
            }

            @Override
            public double execute(Object[] parameters) {
                return 0;
            }
        };
        FunctionLoader.addFunction("sin",emptyAction);
        FunctionLoader.addFunction("cos",emptyAction);

        ConstantLoader.addConstant("pi",Math.PI);
        ConstantLoader.addConstant("e",Math.E);
    }

    @Test
    public void testReader(){
        //String expression="sin(2*x)+cos(pi*2)+pi*e";
        String expression="integrateX(0,1,\"cos(x)\")";
        //String expression="pi*e";
        ExpressionReader reader=new ExpressionReader(expression);
        String tokenString;
        while((tokenString=reader.readNextTokenAsString())!=null)
            System.out.println(tokenString);
    }

    @Test
    public void testAnalyzer(){
        String expression="integrateX(0,1,\"cos(x)\")";
        List<ExpressionToken> tokenList=ExpressionTokenAnalyzer.analyzeExpression(expression);
        for(ExpressionToken token:tokenList){
            System.out.println(token);
        }
    }

}
