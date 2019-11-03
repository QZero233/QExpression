package com.qzero.executor;

/**
 * Basic data mate to store data
 * @author QZero
 * @version 1.0
 */
public class BaseDataMate {

    public enum DataType {
        DATA_TYPE_STRING,
        DATA_TYPE_DOUBLE,
        DATA_TYPE_BOOLEAN
    }

    private DataType dataType;
    //The certain value of the constant it may be string or double
    private Object dataValue;

    public BaseDataMate(DataType constantType, Object constantValue) {
        if(constantValue==null)
            throw new IllegalArgumentException("Constant value can not be null");

        if(constantType== DataType.DATA_TYPE_STRING && !(constantValue instanceof String))
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be String");

        if(constantType== DataType.DATA_TYPE_DOUBLE && !(constantValue instanceof Double))
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be Double");

        if(constantType==DataType.DATA_TYPE_BOOLEAN && !(constantValue instanceof Boolean))
            throw new IllegalArgumentException("Constant type wrong,it's supposed to be Boolean");

        this.dataType=constantType;
        this.dataValue=constantValue;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Object getDataValue() {
        return dataValue;
    }

    public void setDataValue(Object dataValue) {
        this.dataValue = dataValue;
    }

    @Override
    public String toString() {
        return "BaseDataMate{" +
                "dataType=" + dataType +
                ", dataValue=" + dataValue +
                '}';
    }
}
