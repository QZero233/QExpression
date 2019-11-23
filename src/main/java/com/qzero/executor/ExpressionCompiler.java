package com.qzero.executor;

import com.alibaba.fastjson.JSON;
import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.token.*;

import java.util.*;

/**
 * Compiler to compile analyzed expression
 * @author QZero
 * @version 1.0
 */
public class ExpressionCompiler {

    /**
     * Get the level of an operational token
     * The bigger,the higher
     * +- is 1,*\/ is 2,function is 3
     * @param token Token object
     * @return Level of the token,if it's not an operational token,return -1
     */
    private static int getOperationalTokenLevel(ExpressionToken token){
        switch (token.getTokenType()){
            case TOKEN_TYPE_OPERATOR:
                OperatorToken tokenObject= (OperatorToken) token.getTokenObject();
                return tokenObject.getOperatorLevel();
            case TOKEN_TYPE_FUNCTION:
                return OperatorToken.LEVEL_HIGHEST;
            default:
                return -1;
        }
    }

    /**
     * Compile an analyzed expression into a RPN expression
     * @param tokenList
     * @return
     */
    public static List<ExpressionToken> compile(List<ExpressionToken> tokenList){
        if(tokenList==null || tokenList.isEmpty())
            return null;

        List<ExpressionToken> compileResult=new ArrayList<>();

        Stack<ExpressionToken> operatorStack=new Stack<>();

        for(ExpressionToken token:tokenList){


            if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT){
                //It's a constant just push into result
                compileResult.add(token);
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_SPLIT){

                SplitToken splitToken= (SplitToken) token.getTokenObject();
                if(splitToken.getType()== SplitToken.SplitType.SPLIT_TYPE_BRACKETS_LEFT){
                    //Meet (,just push
                    operatorStack.push(token);
                    continue;
                }else if(splitToken.getType()== SplitToken.SplitType.SPLIT_TYPE_BRACKETS_RIGHT){
                    //Pop all elements into result until meet left bracket
                    while(!operatorStack.isEmpty()){
                        ExpressionToken tmpToken=operatorStack.pop();

                        if(tmpToken.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_SPLIT)
                            break;

                        compileResult.add(tmpToken);
                    }
                }else if(splitToken.getType()== SplitToken.SplitType.SPLIT_TYPE_COMMA){
                    //Pop until meet (
                    //The parameter should always be the highest
                    while (!operatorStack.isEmpty() && !operatorStack.peek().getTokenObject().getTokenString().equals("(")){
                        compileResult.add(operatorStack.pop());
                    }
                }

            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE){
                //Just regard it as a constant
                compileResult.add(token);
                continue;
            }else if(token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR || token.getTokenType()== ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION){
                //Function is regarded as a special operator with highest level

                int level=getOperationalTokenLevel(token);

                if(operatorStack.isEmpty()){
                    operatorStack.push(token);
                    continue;
                }

                ExpressionToken tmpToken=operatorStack.peek();
                if(tmpToken.getTokenObject().getTokenString().equals("(")){
                    operatorStack.push(token);
                    continue;
                }

                int newLevel=getOperationalTokenLevel(tmpToken);
                if(newLevel!=-1 && newLevel<level){
                    operatorStack.push(token);
                    continue;
                }

                while(!operatorStack.isEmpty()){
                    tmpToken=operatorStack.peek();
                    newLevel = getOperationalTokenLevel(tmpToken);
                    if(newLevel==-1 || newLevel<level){
                        break;
                    }
                    compileResult.add(operatorStack.pop());
                }

                operatorStack.push(token);
                continue;
            }
        }

        //If the operatorStack still has something,pop all into result
        while (!operatorStack.isEmpty()){
            compileResult.add(operatorStack.pop());
        }

        return compileResult;
    }

    /**
     * Export a compiled expression into json text form
     * @param compiled Compiled expression
     * @return Json text form
     */
    public static String exportToJsonString(List<ExpressionToken> compiled){
        List<OutputToken> outputTokenList=new ArrayList<>();
        for(ExpressionToken token:compiled){
            OutputToken outputToken=new OutputToken();
            outputToken.setTokenString(token.getTokenObject().getTokenString());

            switch (token.getTokenType()){
                case TOKEN_TYPE_CONSTANT:
                    outputToken.setTokenType(OutputToken.TYPE_CONSTANT);
                    break;
                case TOKEN_TYPE_VARIABLE:
                    outputToken.setTokenType(OutputToken.TYPE_VARIABLE);
                    break;
                case TOKEN_TYPE_FUNCTION:
                    outputToken.setTokenType(OutputToken.TYPE_FUNCTION);
                    break;
                case TOKEN_TYPE_OPERATOR:
                    outputToken.setTokenType(OutputToken.TYPE_OPERATOR);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown token type "+token.getTokenType());
            }

            outputTokenList.add(outputToken);
        }
        return JSON.toJSONString(outputTokenList);
    }

    /**
     * Parse a json string into a compiled expression
     * @param jsonString jsonString
     * @return Compiled expression,null if failed
     */
    public static List<ExpressionToken> parseFromJsonString(String jsonString){
        List<OutputToken> outputTokenList=JSON.parseArray(jsonString,OutputToken.class);
        if(outputTokenList==null || outputTokenList.isEmpty())
            return null;

        List<ExpressionToken> result=new ArrayList<>();
        for(OutputToken token:outputTokenList){
            ExpressionToken expressionToken=new ExpressionToken();

            switch (token.getTokenType()){
                case OutputToken.TYPE_CONSTANT:
                    expressionToken.setTokenType(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT);

                    String constantString=token.getTokenString();
                    if(constantString.startsWith("\"") && constantString.endsWith("\"")){
                        //It's a string constant
                        ConstantToken constantToken=new ConstantToken(constantString, BaseDataMate.DataType.DATA_TYPE_STRING,
                                constantString.substring(1,constantString.length()-1));
                        expressionToken.setTokenObject(constantToken);
                    }else{
                        //It's an integer constant or a global constant
                        if(ExpressionTokenAnalyzer.isDigestOnly(token.getTokenString())){
                            //Integer constant
                            ConstantToken constantToken=new ConstantToken(constantString, BaseDataMate.DataType.DATA_TYPE_DOUBLE,Double.parseDouble(constantString));
                            expressionToken.setTokenObject(constantToken);
                        }else{
                            //Global constant
                            Double value=ConstantLoader.getConstantValue(constantString);
                            if(value==null)
                                throw new IllegalArgumentException("Unknown constant "+constantString);

                            ConstantToken constantToken=new ConstantToken(constantString, BaseDataMate.DataType.DATA_TYPE_DOUBLE,value);
                            expressionToken.setTokenObject(constantToken);
                        }
                    }
                    break;
                case OutputToken.TYPE_VARIABLE:
                    expressionToken.setTokenType(ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE);
                    VariableToken variableToken=new VariableToken(token.getTokenString());
                    expressionToken.setTokenObject(variableToken);
                    break;
                case OutputToken.TYPE_FUNCTION:
                    expressionToken.setTokenType(ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION);

                    String functionName=token.getTokenString();
                    ExecutableAction functionAction= FunctionLoader.getFunction(functionName);
                    if(functionAction==null)
                        throw new IllegalArgumentException("Unknown function "+functionName);

                    FunctionToken functionToken=new FunctionToken(functionName,functionAction);
                    expressionToken.setTokenObject(functionToken);
                    break;
                case OutputToken.TYPE_OPERATOR:
                    expressionToken.setTokenType(ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR);

                    String operatorString=token.getTokenString();
                    OperatorToken operatorToken=OperatorToken.getOperatorToken(operatorString);
                    if(operatorToken==null)
                        throw new IllegalArgumentException("Unknown operator "+token);

                    expressionToken.setTokenObject(operatorToken);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown output token type "+token.getTokenType());
            }

            result.add(expressionToken);
        }

        return result;
    }
}
