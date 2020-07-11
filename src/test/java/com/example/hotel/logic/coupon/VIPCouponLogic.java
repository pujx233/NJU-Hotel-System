package com.example.hotel.logic.coupon;

import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.TargetMoneyCouponVO;
import com.example.hotel.vo.TimeCouponVO;
import com.example.hotel.vo.VIPCouponVO;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VIPCouponLogic extends CouponLogic{

    public VIPCouponLogic(CouponService couponService){this.couponService = couponService;}
    @Override
    public Integer couponType() {return 1;}

    @Override
    public CouponVO addCoupon(CouponVO couponVO,boolean success) {
        try{
            couponVO = couponService.addVIPCoupon((VIPCouponVO) couponVO);
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
        return generateCoupon(flag,hotelId,BizRegion.DaZhaLan,VIPLevel.GoldExperience,0.34,flag==0?-1:31);
    }

    public CouponVO generateCoupon(int flag, int hotelId, BizRegion bizRegion, VIPLevel vipLevel,
                                   double discount, Integer discountMoney){
        VIPCouponVO vipCouponVO = new VIPCouponVO();
        CouponVO couponVO = super.generateCoupon(flag,hotelId,vipCouponVO);
        vipCouponVO = (VIPCouponVO)couponVO;
        vipCouponVO.setBizRegion(bizRegion.toString());
        vipCouponVO.setVIPLevel(vipLevel.toString());
        vipCouponVO.setDiscount(discount);
        vipCouponVO.setDiscountMoney(discountMoney);
        return vipCouponVO;
    }

    @Override
    public void equalAfterInsert(CouponVO couponVO, Coupon coupon){
        VIPCouponVO vipCouponVO = (VIPCouponVO)couponVO;
        super.equalAfterInsert(couponVO,coupon);
        assertEquals(vipCouponVO.getBizRegion(),coupon.getBizRegion().toString());
        assertEquals(vipCouponVO.getVIPLevel(),coupon.getVIPLevel().toString());
        assertEquals(vipCouponVO.getDiscount(),coupon.getDiscount(),0.0001);
        assertEquals(vipCouponVO.getDiscountMoney(),coupon.getDiscountMoney(),0.0001);
    }
}
