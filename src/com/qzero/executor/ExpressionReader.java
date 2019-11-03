package com.qzero.executor;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Token reader
 * @author QZero
 * @version 1.0
 */
public class ExpressionReader {

    private String expression;

    private int lastReadIndex =-1;

    //A regex which can match all supported operators and spilt
    private static final String REGEX_FOR_OPERATORS_AND_SPILT="(\\+|\\-|\\*|/|\\(|\\)|,|\")";

    public ExpressionReader(String expression) {
        this.expression = expression;
    }

    /**
     * Read next char of the expression
     * @return Char read
     */
    private String readNextChar(){
        if(lastReadIndex>=expression.length()-1)
            //Last char had been read
            return null;

        String s=expression.substring(lastReadIndex+1, lastReadIndex +2);
        lastReadIndex++;
        return s;
    }

    /**
     * Read next token in the expression
     * @return Token read,If the end has been read,it'll be null
     */
    public String readNextTokenAsString(){
        if(lastReadIndex>=expression.length()-1)
            //Which means the end of the expression has already been read
            return null;

        StringBuffer tmpStream=new StringBuffer();
        String s;
        //Loop to read until read the last char or get the end of current token
        while((s=readNextChar())!=null){
            tmpStream.append(s);

            if(s.matches(REGEX_FOR_OPERATORS_AND_SPILT)){
                if(tmpStream.length()==1){
                    //If it's only a operator or a split,just let it go,that's the answer
                    break;
                }else{
                    if(tmpStream.charAt(tmpStream.length()-2)=='e' || tmpStream.charAt(tmpStream.length()-2)=='E'){
                        //Which means it's e expression,skip
                        continue;
                    }

                    //Which means we got the start of next token,back a position and exit the loop
                    lastReadIndex--;
                    tmpStream.deleteCharAt(tmpStream.length()-1);
                    break;
                }
            }
        }

        return tmpStream.toString();
    }
}
