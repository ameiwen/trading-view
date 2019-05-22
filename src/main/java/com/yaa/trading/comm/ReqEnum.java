package com.yaa.trading.comm;

public enum  ReqEnum {

    ONE_MIN("1min",1),
    FIV_MIN("5min",5),
    ONE_FIV_MIN("15min",15),
    ONE_HOU("1hour",1),
    ONE_DAY("1day",1);

    private String name;
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ReqEnum(String name,Integer value) {
        this.value = value;
        this.name = name;
    }
}
