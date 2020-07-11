package com.example.hotel.enums;

public enum OrderState {
    Booked("未执行"),
    Executed("已执行"),
    Annulled("已撤销"),
    Abnormal("异常"),
    OverdueAbnormal("过期异常");
    private String value;

    OrderState(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }

    public static OrderState parse(String value){
        for(OrderState type:OrderState.values()){
            if(type.toString().equals(value)){
                return type;
            }
        }
        return null;
    }
}
