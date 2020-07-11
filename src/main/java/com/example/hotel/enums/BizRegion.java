package com.example.hotel.enums;

public enum BizRegion {
    XiDan("西单"),
    WangFuJing("王府井"),
    DaZhaLan("大栅栏");

    private String value;

    BizRegion(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static BizRegion parse(String value){
        for(BizRegion type:BizRegion.values()){
            if(type.toString().equals(value)){
                return type;
            }
        }
        return null;
    }
}
