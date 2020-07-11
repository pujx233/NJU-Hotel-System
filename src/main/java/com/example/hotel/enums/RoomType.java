package com.example.hotel.enums;

public enum RoomType {
    BigBed("大床房"),
    DoubleBed("双床房"),
    Family("家庭房");
    private String value;

    RoomType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static RoomType parse(String value){
        for(RoomType type:RoomType.values()){
            if(type.toString().equals(value)){
                return type;
            }
        }
        return null;
    }
}
