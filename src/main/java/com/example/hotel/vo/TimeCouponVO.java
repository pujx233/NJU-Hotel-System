package com.example.hotel.vo;

import java.time.LocalDateTime;

public class TimeCouponVO extends CouponVO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //自行决定是打折还是直接减金额 如果选择打折就把discountMoney变为负数 否则就把discount变为负数

    private double discount;
    /*作业改动 by檀潮*/ //添加
    private Integer discountMoney;//noted by 檀潮：从前端传来时discount与discountMoney中已经有一个是负数了
    /*作业改动*/

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /*作业改动 by檀潮*/ //添加
    public Integer getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Integer discountMoney) {
        this.discountMoney = discountMoney;
    }
    /*作业改动*/
}
