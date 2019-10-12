package com.qzero.executor.function;

import com.qzero.executor.token.ExecutableAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Global function loader to hold runtime functions
 * @author QZero
 * @version 1.0
 */
public class FunctionLoader {

    //A map to hold global functions
    private static Map<String, ExecutableAction> functionMap=new HashMap<>();

    /**
     * To check whether the string is a function name or not
     * @param functionName function name(maybe)
     * @return if it's a function name return true,otherwise false
     */
    public static boolean isFunctionName(String functionName){
        if(functionMap==null || functionMap.isEmpty())
            return false;

        return functionMap.containsKey(functionName);
    }

    /**
     * To get the function
     * @param functionName the name of the function(like pi)
     * @return the function(if so,otherwise null)
     */
    public static ExecutableAction getFunction(String functionName){
        ExecutableAction function=functionMap.get(functionName);
        return function;
    }

    public static void addFunction(String functionName,ExecutableAction action){
        functionMap.put(functionName,action);
    }

    public static void removeFunction(String functionName){
        functionMap.remove(functionName);
    }

}
