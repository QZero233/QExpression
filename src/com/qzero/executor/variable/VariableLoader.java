package com.qzero.executor.variable;

import com.qzero.executor.BaseDataMate;

import java.util.HashMap;
import java.util.Map;

/**
 * Global variable loader to hold runtime variables
 * @author QZero
 * @version 1.0
 */
public class VariableLoader {

    //A map to hold values of global variables
    private static Map<String,BaseDataMate> variableMap=new HashMap<>();

    public static BaseDataMate getVariableValue(String variableName){
        if(variableMap==null)
            return null;
        return variableMap.get(variableName);
    }

    public static void addOrEditVariable(String variableName,BaseDataMate value){
        variableMap.put(variableName,value);
    }

    public static void removeVariable(String variableName){
        variableMap.remove(variableName);
    }

    public static void clearVariable(){
        variableMap.clear();
    }

}
