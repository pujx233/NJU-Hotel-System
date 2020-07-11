/*作业改动 by檀潮*/ //添加
package com.example.hotel.vo;

public class VIPCouponVO extends CouponVO{
    //自行决定是打折还是直接减金额 如果选择打折就把discountMoney变为负数 否则就把discount变为负数
    //noted by 檀潮：从前端传来时discount与discountMoney中已经有一个是负数了
    private double discount;
    private Integer discountMoney;
    private String bizRegion;
    private String vipLevel;
    //根据传来的bizRegion与level的值来作筛选条件，无论bizRegion还是level只要不是null就代表起作用，参见blImpl.coupon.VIPCouponStrategyImpl
    //注意，生日优惠只有酒店才指定，所以当hotelId取-1以外的值时才进入酒店生日优惠的逻辑（此时后端没有用到bizRegion和vipLevel），取-1时按上述逻辑

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

    public String getBizRegion() {
        return bizRegion;
    }

    public void setBizRegion(String bizRegion) {
        this.bizRegion = bizRegion;
    }

    public String getVIPLevel() {
        return vipLevel;
    }

    public void setVIPLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }
}
/*作业改动*/
