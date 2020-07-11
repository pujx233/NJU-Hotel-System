package com.example.hotel.logic.coupon;

import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.TargetMoneyCouponVO;
import com.example.hotel.vo.TimeCouponVO;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TargetMoneyLogic extends CouponLogic{

    public TargetMoneyLogic(CouponService couponService){this.couponService = couponService;}
    @Override
    public Integer couponType() {return 3;}

    @Override
    public CouponVO addCoupon(CouponVO couponVO,boolean success) {
        try{
            couponVO = couponService.addTargetMoneyCoupon((TargetMoneyCouponVO) couponVO);
            if(!success){
                fail();
            }
        }catch (Exception e){
            e.printStackTrace();
            if(success){
                fail();
            }
        }
        return couponVO;
    }

    @Override
    public CouponVO generateCoupon(int flag, int hotelId) {
        return generateCoupon(flag,hotelId,flag%2==0?1:-2,1);
    }

    public CouponVO generateCoupon(int flag, int hotelId, Integer targetMoney, Integer discountMoney){
        TargetMoneyCouponVO targetMoneyCouponVO = new TargetMoneyCouponVO();
        CouponVO couponVO = super.generateCoupon(flag,hotelId,targetMoneyCouponVO);
        targetMoneyCouponVO = (TargetMoneyCouponVO)couponVO;
        targetMoneyCouponVO.setTargetMoney(targetMoney);
        targetMoneyCouponVO.setDiscountMoney(discountMoney);
        return targetMoneyCouponVO;
    }


    @Override
    public void equalAfterInsert(CouponVO couponVO, Coupon coupon){
        TargetMoneyCouponVO targetMoneyCouponVO = (TargetMoneyCouponVO)couponVO;
        super.equalAfterInsert(couponVO,coupon);
        assertEquals(targetMoneyCouponVO.getTargetMoney(),coupon.getTargetMoney(),0.0001);
        assertEquals(targetMoneyCouponVO.getDiscountMoney(),coupon.getDiscountMoney(),0.0001);
    }
}
