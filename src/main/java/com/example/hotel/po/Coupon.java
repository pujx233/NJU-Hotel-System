package com.example.hotel.po;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.VIPLevel;

import java.time.LocalDateTime;
import java.util.logging.Level;


public class Coupon {
    /*作业改动 by檀潮*/ //添加多间策略中的间数属性，本次暂不实现
    /**
     * 房间数量
     */
//    private int roomNum;
    /*作业改动*/
    /**
     * 优惠券id
     */
    private int id;
    /**
     * 优惠券描述
     */
    private String description;

    /**
     * 如果为-1 代表是网站推出的优惠
     */
    private Integer hotelId;

    /**
     * 优惠券类型 1生日特惠 2多间特惠 3满减优惠 4限时优惠
     */
    private Integer couponType;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 优惠券使用门槛
     */
    private Integer targetMoney;

    /**
     * 折扣
     */
    private double discount;
    /**
     * 优惠券优惠金额
     */
    private Integer discountMoney;
    /**
     * 可用时间
     */
    private LocalDateTime startTime;
    /**
     * 失效时间
     */
    private LocalDateTime endTime;
    /**
     * 房间数
     */
    private int roomNum;
    /**
     * 商圈
     */
    private BizRegion bizRegion;
    /**
     * 等级
     */
    private VIPLevel vipLevel;

    /**
     * 优惠券状态 是否已经失效 1可用 0失效
     */
    private Integer status;
    /*作业改动 by檀潮*/ //添加多间策略中的间数属性，本次暂不实现
//    public int getRoomNum(){return roomNum; }
//
//    public void setRoomNum(int roomNum){this.roomNum = roomNum; }
    /*作业改动*/
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }




    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Coupon() {
    }

    public BizRegion getBizRegion() {
        return bizRegion;
    }

    public void setBizRegion(BizRegion bizRegion) {
        this.bizRegion = bizRegion;
    }

    public VIPLevel getVIPLevel() {
        return vipLevel;
    }

    public void setVIPLevel(VIPLevel vipLevel) {
        this.vipLevel = vipLevel;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Integer discountMoney) {
        this.discountMoney = discountMoney;
    }

    public Integer getTargetMoney() {
        return targetMoney;
    }

    public void setTargetMoney(Integer targetMoney) {
        this.targetMoney = targetMoney;
    }
}
