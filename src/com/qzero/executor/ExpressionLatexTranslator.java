package com.qzero.executor;

import com.qzero.executor.token.*;

import java.util.List;
import java.util.Stack;

public class ExpressionLatexTranslator {

    private static final String REGEX_FOR_EXPRESSION_WITH_OPERATOR = ".*(\\+|\\-|\\*|/).*";

    public static String translateToLatex(String originExpression){
        if(originExpression==null)
            return originExpression;

        List<ExpressionToken> analyzed=ExpressionTokenAnalyzer.analyzeExpression(originExpression);
        if(analyzed==null)
            return originExpression;

        List<ExpressionToken> compiled=ExpressionCompiler.compile(analyzed);
        String latex=translateToLatex(compiled);
        if(latex==null)
            return originExpression;
        return translateToLatex(compiled);
    }

    /**
     * Translate compiled expression into latex to show
     *
     * @param compiled Compiled expression
     * @return Latex expression,if translate failed,return null
     */
    public static String translateToLatex(List<ExpressionToken> compiled) {
        if (compiled == null || compiled.isEmpty())
            return null;

        try {
            ExpressionExecutor.check(compiled);
        } catch (Exception e) {
            e.printStackTrace();
            //Expression check failed,it can not be translated
            return null;
        }

        Stack<String> constantTokenStack = new Stack<>();
        for (ExpressionToken token : compiled) {

            if (token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT) {

                ConstantToken constantToken= (ConstantToken) token.getTokenObject();

                String constant = constantToken.getTokenString();
                if(constantToken.getDataMate().getDataType()== BaseDataMate.DataType.DATA_TYPE_STRING)
                    constant=constantToken.getDataMate().getDataValue().toString();

                if (constant.equals("pi"))
                    constant = "\\pi ";

                constantTokenStack.push(constant);
                continue;
            }else if(token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE){

                VariableToken variableToken= (VariableToken) token.getTokenObject();
                constantTokenStack.push(variableToken.getTokenString());

            } else if (token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR) {

                OperatorToken operatorToken = (OperatorToken) token.getTokenObject();
                int[] parametersCounts=operatorToken.getAction().getParametersCounts();

                //To know how many parameters should the action take
                int count=0;
                for(int i:parametersCounts){
                    //Let count as big as it can
                    if(constantTokenStack.size()>=i && count<i){
                        count=i;
                    }
                }

                String[] args=new String[count];
                for(int i=0;i<count;i++){
                    args[count-1-i]=constantTokenStack.pop();
                }

                constantTokenStack.push(getOperatorLatex(operatorToken.getType(),args));

                continue;
            } else if (token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_FUNCTION) {
                FunctionToken functionToken = (FunctionToken) token.getTokenObject();
                ExecutableAction action = functionToken.getAction();

                String functionName = functionToken.getTokenString();
                int parametersCount = action.getParameterCount();
                String[] parameters = new String[parametersCount];
                for (int i = 0; i < parametersCount; i++) {
                    parameters[parametersCount - 1 - i] = constantTokenStack.pop();
                }

                constantTokenStack.push(getFunctionLatex(functionName, parameters));

                continue;
            }
        }

        return constantTokenStack.pop();
    }

    private static String getOperatorLatex(OperatorToken.OperatorType type,String[] args){
        int count=args.length;
        for(int i=0;i<count;i++){
            String current=args[i];
            //current=translateToLatex(current);

            if(i<2 && type==OperatorToken.OperatorType.OPERATOR_TYPE_MULTIPLY){
                if(current.matches(REGEX_FOR_EXPRESSION_WITH_OPERATOR))
                    current="("+current+")";
            }

            current="{"+current+"}";

            args[i]=current;
        }

        String arg1=args[0];
        String arg2=null;
        if(count>=2)
            arg2=args[1];

        switch (type) {
            case OPERATOR_TYPE_ADD:
                if(arg2==null)
                    return "+"+arg1;
                if(arg1.equals("{0}"))
                    arg1="";
                return arg1 + "+" + arg2;
            case OPERATOR_TYPE_MINUS:
                if(arg2==null)
                    return "-"+arg1;
                if(arg1.equals("{0}"))
                    arg1="";
                return arg1 + "-" + arg2;
            case OPERATOR_TYPE_DIVIDE:
                return "\\frac{" + arg1 + "}{" + arg2 + "}";
            case OPERATOR_TYPE_MULTIPLY:
                if(arg1.endsWith(")") && arg2.startsWith("("))
                    return arg1+arg2;
                if(!arg1.matches(REGEX_FOR_EXPRESSION_WITH_OPERATOR) && !arg2.matches(REGEX_FOR_EXPRESSION_WITH_OPERATOR))
                    return arg1+arg2;

                return arg1 + "*" + arg2;
            default:
                return "";
        }
    }

    private static String insertBrackets(String parameter){
        //If it's not pure number or pure letters it may need brackets
        if(parameter==null)
            return "";//TODO THROW EXCEPTION TO SHOW TRANSLATION FAILED

        if(!parameter.matches("\\d.*") && !parameter.matches("[A-Za-z].*")
                && !parameter.equals("\\pi "))
            parameter="("+parameter+")";
        return parameter;
    }

    private static String getFunctionLatex(String functionName, String[] parameters) {
        /*for(int i=0;i<parameters.length;i++){
            parameters[i]=translateToLatex(parameters[i]);
        }*/

        switch (functionName) {
            case "integrate":
                String iStart = parameters[0];
                String iEnd = parameters[1];
                String iExpression = parameters[2];
                String iUnknown = parameters[3];

                iExpression=translateToLatex(iExpression);
                iExpression += "d" + iUnknown;

                return "\\int_{" + iStart + "}^{" + iEnd + "}{" + iExpression + "}";
            case "integrateX":
                String iStartX = parameters[0];
                String iEndX = parameters[1];
                String iExpressionX = parameters[2];

                iExpressionX=translateToLatex(iExpressionX);
                iExpressionX += "dX";

                return "\\int_{" + iStartX + "}^{" + iEndX + "}{" + iExpressionX + "}";
            case "sigma":
                String sStart = parameters[0];
                String sEnd = parameters[1];
                String sExpression = parameters[2];
                String sUnknown = parameters[3];

                sExpression=translateToLatex(sExpression);
                sExpression=insertBrackets(sExpression);

                sStart = sUnknown + "=" + sStart;
                return "\\sum_{" + sStart + "}^{" + sEnd + "}{" + sExpression + "}";
            case "sigmaN":
                String sStartN = parameters[0];
                String sEndN = parameters[1];
                String sExpressionN = parameters[2];

                sExpressionN=translateToLatex(sExpressionN);
                sExpressionN=insertBrackets(sExpressionN);

                sStartN = "n=" + sStartN;
                return "\\sum_{" + sStartN + "}^{" + sEndN + "}{" + sExpressionN + "}";
            case "derivative":
                String dExpression = parameters[0];
                String dUnknown = parameters[1];

                dExpression=translateToLatex(dExpression);

                return "\\frac{d}{d" + dUnknown + "}{" + dExpression + "}";
            case "derivativeX":
                String dExpressionX = parameters[0];

                dExpressionX=translateToLatex(dExpressionX);

                return "\\frac{d}{dx}{" + dExpressionX + "}";
            case "pow":
                String powerBase = parameters[0];
                String powerIndex = parameters[1];

                powerBase=insertBrackets(powerBase);

                return "{"+powerBase + "}^{" + powerIndex + "}";
            case "log":
                String logBase = parameters[0];
                String realNumber = parameters[1];

                realNumber=insertBrackets(realNumber);

                return "log_{" + logBase + "}{" + realNumber + "}";
            case "sqrt":
                return "\\sqrt{" + parameters[0] + "}";
            case "cbrt":
                return "\\sqrt[3]{" + parameters[0] + "}";
            default:
                StringBuffer functionResult = new StringBuffer();
                functionResult.append(functionName);
                functionResult.append("(");

                for (String parameter : parameters) {
                    functionResult.append(parameter);
                    functionResult.append(",");
                }

                functionResult.deleteCharAt(functionResult.length() - 1);

                functionResult.append(")");
                return functionResult.toString();
        }
    }

}
