package com.example.hotel.enums;

public enum ServiceOfHotel {
    WiFi("Wifi"),

    HotWater("热水"),

    WakeUp("叫醒服务");

    private String value;

    ServiceOfHotel(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ServiceOfHotel parse(String value){
        for(ServiceOfHotel type:ServiceOfHotel.values()){
            if(type.toString().equals(value)){
                return type;
            }
        }
        return null;
    }
}
