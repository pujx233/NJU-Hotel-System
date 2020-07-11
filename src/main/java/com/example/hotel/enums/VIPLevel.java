package com.example.hotel.enums;

public enum VIPLevel {
    Hamon("1"),
    HermitPurple("2"),
    StarPlatinum("3"),
    CrazyDiamond("4"),
    GoldExperience("5");
    private String value;

    VIPLevel(String value) {
        this.value = value;
    }

    @Override
    public String toString() { return value; }

    public static VIPLevel parse(String value){
        for(VIPLevel type:VIPLevel.values()){
            if(type.toString().equals(value)){
                return type;
            }
        }
        return null;
    }
}
