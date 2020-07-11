package com.example.hotel.vo;

/**
 * 酒店满减金额优惠 eg 满300减100
 */

/*作业改动 by檀潮*/ //修改
//由于网站也可以有满减优惠，故修改类名HotelTargetMoneyCouponVO为TargetMoneyCouponVO
public class TargetMoneyCouponVO extends CouponVO {
    /*作业改动*/

    /*作业改动 by檀潮*/ //移动至CouponVO
//   private Integer hotelId;
    /*作业改动*/

    private Integer targetMoney;

    private Integer discountMoney;
    /*作业改动 by檀潮*/ //移动至CouponVO
//    public Integer getHotelId() {
//        return hotelId;
//    }
//
//    public void setHotelId(Integer hotelId) {
//        this.hotelId = hotelId;
//    }
    /*作业改动*/
    public Integer getTargetMoney() {
        return targetMoney;
    }

    public void setTargetMoney(Integer targetMoney) {
        this.targetMoney = targetMoney;
    }

    public Integer getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Integer discountMoney) {
        this.discountMoney = discountMoney;
    }

}
