package com.qzero.executor;

import com.qzero.executor.token.ExecutableAction;
import com.qzero.executor.token.FunctionToken;
import com.qzero.executor.token.OperatorToken;

import java.util.List;
import java.util.Stack;

public class ExpressionLatexTranslator {

    private static final String REGEX_FOR_EXPRESSION_WITH_OPERATOR = ".*(\\+|\\-|\\*|/).*";

    public static String translateToLatex(String originExpression){
        if(originExpression==null)
            return null;

        List<ExpressionToken> analyzed=ExpressionTokenAnalyzer.analyzeExpression(originExpression);
        if(analyzed==null)
            return null;

        List<ExpressionToken> compiled=ExpressionCompiler.compile(analyzed);
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
            ExpressionExecutor.check(compiled, false);
        } catch (Exception e) {
            e.printStackTrace();
            //Expression check failed,it can not be translated
            return null;
        }

        Stack<String> constantTokenStack = new Stack<>();
        for (ExpressionToken token : compiled) {

            if (token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_VARIABLE || token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_CONSTANT) {

                String constant = token.getTokenObject().getTokenString();
                if (constant.equals("pi"))
                    constant = "\\pi ";

                constantTokenStack.push(constant);
                continue;
            } else if (token.getTokenType() == ExpressionToken.TokenType.TOKEN_TYPE_OPERATOR) {

                OperatorToken operatorToken = (OperatorToken) token.getTokenObject();

                String arg2 = constantTokenStack.pop();
                String arg1 = constantTokenStack.pop();

                constantTokenStack.push(getOperatorLatex(operatorToken.getType(),arg1,arg2));

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

    private static String getOperatorLatex(OperatorToken.OperatorType type,String arg1,String arg2){

        if(type== OperatorToken.OperatorType.OPERATOR_TYPE_MULTIPLY){
            if(arg1.matches(REGEX_FOR_EXPRESSION_WITH_OPERATOR))
                arg1="("+arg1+")";
            if(arg2.matches(REGEX_FOR_EXPRESSION_WITH_OPERATOR))
                arg2="("+arg2+")";
        }



        arg1="{"+arg1+"}";
        arg2="{"+arg2+"}";


        switch (type) {
            case OPERATOR_TYPE_ADD:
                return arg1 + "+" + arg2;
            case OPERATOR_TYPE_MINUS:
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

    private static String getFunctionLatex(String functionName, String[] parameters) {

        switch (functionName) {
            case "integrate":
                String iStart = parameters[0];
                String iEnd = parameters[1];
                String iExpression = parameters[2];
                String iUnknown = parameters[3];

                iExpression += "d" + iUnknown;

                return "\\int_{" + iStart + "}^{" + iEnd + "}{" + iExpression + "}";
            case "integrateX":
                String iStartX = parameters[0];
                String iEndX = parameters[1];
                String iExpressionX = parameters[2];

                iExpressionX += "dX";

                return "\\int_{" + iStartX + "}^{" + iEndX + "}{" + iExpressionX + "}";
            case "sigma":
                String sStart = parameters[0];
                String sEnd = parameters[1];
                String sExpression = parameters[2];
                String sUnknown = parameters[3];

                sStart = sUnknown + "=" + sStart;
                return "\\sum_{" + sStart + "}^{" + sEnd + "}{" + sExpression + "}";
            case "sigmaN":
                String sStartN = parameters[0];
                String sEndN = parameters[1];
                String sExpressionN = parameters[2];

                sStartN = "n=" + sStartN;
                return "\\sum_{" + sStartN + "}^{" + sEndN + "}{" + sExpressionN + "}";
            case "derivative":
                String dExpression = parameters[0];
                String dUnknown = parameters[1];

                return "\\frac{d}{d" + dUnknown + "}{" + dExpression + "}";
            case "derivativeX":
                String dExpressionX = parameters[0];

                return "\\frac{d}{dx}{" + dExpressionX + "}";
            case "pow":
                String powerBase = parameters[0];
                String powerIndex = parameters[1];

                return powerBase + "^{" + powerIndex + "}";
            case "log":
                String logBase = parameters[0];
                String realNumber = parameters[1];
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
