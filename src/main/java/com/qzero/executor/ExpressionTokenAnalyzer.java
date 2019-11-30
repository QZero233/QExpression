package com.qzero.executor;

import com.qzero.executor.constant.ConstantLoader;
import com.qzero.executor.function.ExecutableAction;
import com.qzero.executor.function.FunctionLoader;
import com.qzero.executor.function.IExecutableAction;
import com.qzero.executor.token.OperatorToken;
import com.qzero.executor.token.*;

import java.util.*;

/**
 * Expression Token analyzer to analyze expression into a list of tokens
 *
 * @author QZero
 * @version 1.0
 */
public class ExpressionTokenAnalyzer {

    private static final String REGEX_FOR_E_EXPRESSION = "(((-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|(([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)))|(([1-9]\\d*)|(-[1-9]\\d*)))(e|E)(([1-9]\\d*)|(-[1-9]\\d*))";

    /**
     * To check whether the string is a number or not
     *
     * @param string string for checking
     * @return if it's a number(including floating number，negative number),return true,otherwise false
     */
    public static boolean isDigestOnly(String string) {
        if (string == null || string.length() == 0)
            return false;
        if (string.matches("^[+-]?\\d+(\\.\\d*)?$"))
            return true;
        return false;
    }

    /**
     * @param expression
     * @return
     */
    public static List<ExpressionToken> analyzeExpression(String expression) {
        List<ExpressionToken> tokenList = new ArrayList<>();

        //Stack<String> stringConstantStack=new Stack<>();
        Queue<String> stringConstantQueue = new LinkedList<>();


        ExpressionReader reader = new ExpressionReader(expression);
        String tokenString;

        while ((tokenString = reader.readNextTokenAsString()) != null) {

            if (tokenString.equals("\"")) {
                if (stringConstantQueue.isEmpty()) {
                    //Which means it's the start of a string
                    stringConstantQueue.add(tokenString);
                    continue;
                } else {
                    //Pop all element as a string constant
                    StringBuffer constantString = new StringBuffer();

                    while (!stringConstantQueue.isEmpty()) {
                        String tmp = stringConstantQueue.poll();
                        if (tmp.equals("\""))
                            continue;
                        constantString.append(tmp);
                    }

                    ConstantToken constantToken = new ConstantToken("\"" + constantString.toString() + "\"", BaseDataMate.DataType.DATA_TYPE_STRING, constantString.toString());
                    ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT, constantToken,reader.getLastReadIndex());
                    tokenList.add(token);
                    continue;
                }
            }

            if (!stringConstantQueue.isEmpty()) {
                //Still in a string
                stringConstantQueue.add(tokenString);
                continue;
            }

            //Test whether it's an operator
            OperatorToken operatorToken = OperatorToken.getOperatorToken(tokenString);
            if (operatorToken != null) {
                //It's really an operator

                //If it's minus or add and there isn't any number in front of it(maybe the former is a spilt(not right bracket) or empty)
                if (operatorToken.getType() == OperatorToken.OperatorType.OPERATOR_TYPE_MINUS || operatorToken.getType() == OperatorToken.OperatorType.OPERATOR_TYPE_ADD) {
                    ConstantToken zero = new ConstantToken("0", BaseDataMate.DataType.DATA_TYPE_DOUBLE, 0D);
                    ExpressionToken zeroToken = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT, zero,-1);

                    if (tokenList.isEmpty()) {
                        //There isn't anything front,add 0
                        tokenList.add(zeroToken);
                    }
                    ExpressionToken former = tokenList.get(tokenList.size() - 1);
                    if (former.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_SPLIT) {
                        SplitToken splitToken = (SplitToken) former.getTokenObject();
                        if (splitToken.getType() != SplitToken.SplitType.SPLIT_TYPE_BRACKETS_RIGHT) {
                            //Also add 0
                            tokenList.add(zeroToken);
                        }
                    } else if (former.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR) {
                        //Also add 0
                        tokenList.add(zeroToken);
                    }
                }


                ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR, operatorToken,reader.getLastReadIndex());
                tokenList.add(token);
                continue;
            }

            if (tokenString.length() == 1) {
                //Then it may be a split
                SplitToken splitToken = null;
                switch (tokenString) {
                    case "(":
                        splitToken = new SplitToken(tokenString, SplitToken.SplitType.SPLIT_TYPE_BRACKETS_LEFT);
                        break;
                    case ")":
                        splitToken = new SplitToken(tokenString, SplitToken.SplitType.SPLIT_TYPE_BRACKETS_RIGHT);
                        break;
                    case ",":
                        splitToken = new SplitToken(tokenString, SplitToken.SplitType.SPLIT_TYPE_COMMA);
                        break;
                }

                if (splitToken != null) {
                    //It's really a split
                    ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_SPLIT, splitToken,reader.getLastReadIndex());
                    tokenList.add(token);
                    continue;
                }


            }

            if (tokenString.matches(REGEX_FOR_E_EXPRESSION)) {
                //It's e expression which means it's a constant
                tokenString = tokenString.replace("e", "E");

                String[] tmp = tokenString.split("E");
                Double base = Double.parseDouble(tmp[0]);
                Integer power = Integer.parseInt(tmp[1]);

                Double result = Math.pow(10, power) * base;

                ConstantToken constantToken = new ConstantToken(tokenString, BaseDataMate.DataType.DATA_TYPE_DOUBLE, result);
                ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT, constantToken,reader.getLastReadIndex());
                tokenList.add(token);
                continue;
            }

            if (isDigestOnly(tokenString)) {
                //It's constant,just a number

                //If the former is minus
                ConstantToken constantToken = new ConstantToken(tokenString, BaseDataMate.DataType.DATA_TYPE_DOUBLE, Double.parseDouble(tokenString));
                ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT, constantToken,reader.getLastReadIndex());
                tokenList.add(token);
                continue;
            }

            if (ConstantLoader.isConstant(tokenString)) {
                //It's also a constant such as pi
                ConstantToken constantToken = new ConstantToken(tokenString, BaseDataMate.DataType.DATA_TYPE_DOUBLE, ConstantLoader.getConstantValue(tokenString));
                ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT, constantToken,reader.getLastReadIndex());
                tokenList.add(token);
                continue;
            }

            if (FunctionLoader.isFunctionName(tokenString)) {
                //It's a function
                ExecutableAction function = FunctionLoader.getFunction(tokenString);
                FunctionToken functionToken = new FunctionToken(tokenString, function);
                ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION, functionToken,reader.getLastReadIndex());
                tokenList.add(token);
                continue;
            }

            //Then regard it as a variable
            VariableToken variableToken = new VariableToken(tokenString);
            ExpressionToken token = new ExpressionToken(ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE, variableToken,reader.getLastReadIndex());
            tokenList.add(token);

        }
        return tokenList;
    }

    /**
     * Get all variables in an expression
     *
     * @param originExpression expression
     * @return A list,which contains all variables
     */
    public static List<VariableToken> getAllVariableInExpression(String originExpression) {
        List<ExpressionToken> analyzed = analyzeExpression(originExpression);
        if (analyzed == null || analyzed.isEmpty())
            return new ArrayList<>();

        List<VariableToken> result = new ArrayList<>();
        for (ExpressionToken token : analyzed) {
            if (token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE)
                result.add((VariableToken) token.getTokenObject());
        }

        return result;
    }

}
