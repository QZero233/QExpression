package com.qzero.executor.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Global constant loader to hold constants
 * @author QZero
 * @version 1.0
 */
public class ConstantLoader {

    //A map to hold global constant(like pi,e)
    private static Map<String,Double> constantMap=new HashMap<>();

    /**
     * To check whether the string is a constant or not
     * @param constantName constant name(maybe)
     * @return if it's a constant return true,otherwise false
     */
    public static boolean isConstant(String constantName){
        if(constantMap==null || constantMap.isEmpty())
            return false;

        return constantMap.containsKey(constantName);
    }

    /**
     * To get a certain value of a constant
     * @param constantName the name of the constant(like pi)
     * @return the value(if so,otherwise null)
     */
    public static Double getConstantValue(String constantName){
        Double value=constantMap.get(constantName);
        return value;
    }

    public static void addConstant(String constantName,Double value){
        constantMap.put(constantName,value);
    }

    public static void removeConstant(String constantName){
        constantMap.remove(constantName);
    }

}
