package com.qzero.executor;

import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.*;
import com.qzero.executor.variable.VariableLoader;

import java.util.*;

/**
 * Expression Token analyzer to analyze expression into a list of tokens
 * @author QZero
 * @version 1.0
 */
public class ExpressionTokenAnalyzer {

    /**
     * To check whether the string is a number or not
     * @param string string for checking
     * @return if it's a number(including floating number，negative number),return true,otherwise false
     */
    private static boolean isDigestOnly(String string){
        if(string==null || string.length()==0)
            return false;
        if(string.matches("^[+-]?\\d+(\\.\\d*)?$"))
            return true;
        return false;
    }

    /**
     *
     * @param expression
     * @return
     */
    public static List<ExpressionToken> analyzeExpression(String expression){
        List<ExpressionToken> tokenList=new ArrayList<>();

        //Stack<String> stringConstantStack=new Stack<>();
        Queue<String> stringConstantQueue=new LinkedList<>();

        ExpressionReader reader=new ExpressionReader(expression);
        String tokenString;
        while((tokenString=reader.readNextTokenAsString())!=null){

            if(tokenString.equals("\"")){
                if(stringConstantQueue.isEmpty()){
                    //Which means it's the start of a string
                    stringConstantQueue.add(tokenString);
                    continue;
                }else{
                    //Pop all element as a string constant
                    StringBuffer constantString=new StringBuffer();

                    while(!stringConstantQueue.isEmpty()){
                        String tmp=stringConstantQueue.poll();
                        if(tmp.equals("\""))
                            continue;
                        constantString.append(tmp);
                    }

                    ConstantToken constantToken=new ConstantToken(constantString.toString(), BaseDataMate.DataType.DATA_TYPE_STRING,constantString.toString());
                    ExpressionToken token=new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT,constantToken);
                    tokenList.add(token);
                    continue;
                }
            }

            if(!stringConstantQueue.isEmpty()){
                //Still in a string
                stringConstantQueue.add(tokenString);
                continue;
            }

            if(tokenString.length()==1){
                //The length is 1,which means it's probably an operator
                OperatorToken operatorToken=OperatorToken.getOperatorToken(tokenString);
                if(operatorToken!=null){
                    //It's really an operator
                    ExpressionToken token=new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR,operatorToken);
                    tokenList.add(token);
                    continue;
                }else{
                    //Then it may be a split
                    SplitToken splitToken=null;
                    switch (tokenString){
                        case "(":
                            splitToken=new SplitToken(tokenString, SplitToken.SplitType.SPLIT_TYPE_BRACKETS_LEFT);
                            break;
                        case ")":
                            splitToken=new SplitToken(tokenString, SplitToken.SplitType.SPLIT_TYPE_BRACKETS_RIGHT);
                            break;
                        case ",":
                            splitToken=new SplitToken(tokenString, SplitToken.SplitType.SPLIT_TYPE_COMMA);
                            break;
                    }

                    if(splitToken!=null){
                        //It's really a split
                        ExpressionToken token=new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_SPLIT,splitToken);
                        tokenList.add(token);
                        continue;
                    }
                }

            }

            if(isDigestOnly(tokenString)){
                //It's constant,just a number
                ConstantToken constantToken=new ConstantToken(tokenString, BaseDataMate.DataType.DATA_TYPE_DOUBLE,Double.parseDouble(tokenString));
                ExpressionToken token=new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT,constantToken);
                tokenList.add(token);
                continue;
            }

            if(ConstantLoader.isConstant(tokenString)){
                //It's also a constant such as pi
                ConstantToken constantToken=new ConstantToken(tokenString, BaseDataMate.DataType.DATA_TYPE_DOUBLE,ConstantLoader.getConstantValue(tokenString));
                ExpressionToken token=new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT,constantToken);
                tokenList.add(token);
                continue;
            }

            if(FunctionLoader.isFunctionName(tokenString)){
                //It's a function
                ExecutableAction function=FunctionLoader.getFunction(tokenString);
                FunctionToken functionToken=new FunctionToken(tokenString,function);
                ExpressionToken token=new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION,functionToken);
                tokenList.add(token);
                continue;
            }

            //Then regard it as a variable
            VariableToken variableToken=new VariableToken(tokenString,null);
            ExpressionToken token=new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE,variableToken);
            tokenList.add(token);

        }
        return tokenList;
    }

}
