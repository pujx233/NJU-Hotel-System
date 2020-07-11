/*作业改动 by檀潮*/ //添加

package com.example.hotel.vo;

public class RoomsCouponVO extends CouponVO {
    //自行决定是打折还是直接减金额 如果选择打折就把discountMoney变为负数 否则就把discount变为负数
    //noted by 檀潮：从前端传来时discount与discountMoney中已经有一个是负数了
    private double discount;
    private Integer discountMoney;
    private Integer roomNum;//房间数

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Integer getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Integer discountMoney) {
        this.discountMoney = discountMoney;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }
}

/*作业改动*/