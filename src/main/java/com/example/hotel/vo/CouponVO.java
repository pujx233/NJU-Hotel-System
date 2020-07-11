package com.example.hotel.vo;

public class CouponVO {
    /*作业改动 by檀潮*/ //剪切自TargetMonetCouponVO，hotelId为-1时表示网站策略
    private Integer hotelId;
    /*作业改动*/
    private Integer id;
    private String description;
    private Integer status;
    private String name;
    private Integer type;

    /*作业改动 by檀潮*/ //剪切自TargetMonetCouponVO
    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }
    /*作业改动*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
