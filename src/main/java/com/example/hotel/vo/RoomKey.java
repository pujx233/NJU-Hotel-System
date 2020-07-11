package com.example.hotel.vo;

import org.springframework.web.bind.annotation.PathVariable;

public class RoomKey {
    private String roomType;
    private Integer hotelId;

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }
}
