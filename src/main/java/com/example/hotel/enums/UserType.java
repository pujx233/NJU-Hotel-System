package com.example.hotel.enums;

/**
 * @Author: chenyizong
 * @Date: 2020-02-29
 */
public enum UserType {
    Client("1"),
    HotelManager("2"),
    Marketer("3"),
    Administrator("4");
    private String value;

    UserType(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }

    public static UserType parse(String value){
        for(UserType type:UserType.values()){
            if(type.toString().equals(value)){
                return type;
            }
        }
        return null;
    }
}
