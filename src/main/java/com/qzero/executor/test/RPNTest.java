package com.qzero.executor.test;

import org.junit.Test;

import java.util.Stack;

public class RPNTest {


    public int getOperatorLevel(String operator) {
        if (operator.equals("+"))
            return 1;
        if (operator.equals("-"))
            return 1;
        if (operator.equals("*"))
            return 2;
        if (operator.equals("/"))
            return 2;
        else
            return -1;
    }

    public String generateRPN(String expression) {

        StringBuffer result = new StringBuffer();

        Stack<String> operatorStack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            String s = expression.substring(i, i + 1);
            if (s.matches("\\d*")) {
                //Only digest
                result.append(s);
                continue;
            } else if (s.equals("(")) {
                //Just push
                operatorStack.push(s);
                continue;
            } else if (s.equals(")")) {
                //Pop all into result

                while (!operatorStack.isEmpty()) {
                    String tmp = operatorStack.pop();
                    if (tmp.equals("("))
                        break;
                    result.append(tmp);
                }

            } else {
                //Consider it as a operator
                int level = getOperatorLevel(s);
                if (level == -1)
                    //Not a operator
                    continue;

                //Now I'm very sure that it's an operator

                if (operatorStack.isEmpty() || operatorStack.peek().equals("(") || level > getOperatorLevel(operatorStack.peek()))
                    operatorStack.push(s);
                else {

                    while (!operatorStack.isEmpty()) {
                        int newLevel = getOperatorLevel(operatorStack.peek());
                        if(newLevel<level){
                            break;
                        }
                        String tmp = operatorStack.pop();
                        result.append(tmp);

                    }

                    operatorStack.push(s);

                }
            }

        }


        while (!operatorStack.isEmpty()) {
            String tmp = operatorStack.pop();
            if (getOperatorLevel(tmp) != -1)
                result.append(tmp);
        }


        return result.toString();
    }

    public double executeRPN(String rnp) {
        Stack<Double> operatorNumber = new Stack<>();

        for (int i = 0; i < rnp.length(); i++) {
            char c = rnp.charAt(i);
            String s = new String(new byte[]{(byte) c});

            if (s.matches("\\d*")) {
                //Only digest
                operatorNumber.push(Double.parseDouble(s));
            } else {
                //Must be a operator
                Double operatorNumber2 = operatorNumber.pop();
                Double operatorNumber1 = operatorNumber.pop();

                Double result;
                if (c == '+')
                    result = operatorNumber1 + operatorNumber2;
                else if (c == '-')
                    result = operatorNumber1 - operatorNumber2;
                else if (c == '*')
                    result = operatorNumber1 * operatorNumber2;
                else if (c == '/')
                    result = operatorNumber1 / operatorNumber2;
                else
                    continue;

                operatorNumber.push(result);
            }
        }


        return operatorNumber.pop();
    }

    @Test
    public void testRPN() {
        //String expression = "1-2+3*(1/2)*(1+3)";
        String expression = "(2+3*4)*6";
        //String expression="1-2+3";

        String rpn = generateRPN(expression);
        System.out.println(rpn);

        double result = executeRPN(rpn);
        System.out.println(result + "");

    }

}
