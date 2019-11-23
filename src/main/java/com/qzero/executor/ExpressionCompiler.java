package com.qzero.executor;

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


}
