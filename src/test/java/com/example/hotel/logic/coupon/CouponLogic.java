package com.example.hotel.logic.coupon;

import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.data.coupon.CouponMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.Coupon;
import com.example.hotel.po.User;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.TargetMoneyCouponVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public abstract class CouponLogic {
    CouponService couponService;
    public abstract Integer couponType();
    public void deleteById(int id){
        assertTrue(couponService.deleteCoupon(id).getSuccess());
    };
    public abstract CouponVO addCoupon(CouponVO couponVO,boolean success);
    public List<Coupon> getCoupons(int hotelId){
        return couponService.getHotelAllCoupon(hotelId);
    }
    public abstract CouponVO generateCoupon(int flag,int hotelId);
    public CouponVO generateCoupon(int flag,int hotelId,CouponVO couponVO){
        couponVO.setType(couponType());
        couponVO.setHotelId(hotelId);
        couponVO.setName((flag*flag+87)%10000+"优惠");
        return couponVO;
    };
    public List<CouponVO> insertCoupons(int testNum,int hotelId){
        List<CouponVO> couponRecords = new ArrayList<>();
        for(int i=0;i<testNum;i++){
            couponRecords.add(addCoupon(generateCoupon(i,hotelId),i%2==0));//偶数成功，奇数失败，分别检验
        }
        return couponRecords;
    }
    public void equalAfterInsert(CouponVO couponVO,Coupon coupon){
        assertEquals(couponVO.getId(),(Integer)coupon.getId());
        assertEquals(couponVO.getHotelId(),coupon.getHotelId());
        assertEquals(couponVO.getDescription(),coupon.getDescription());
        assertEquals(couponVO.getName(),coupon.getCouponName());
        assertEquals(couponType(),coupon.getCouponType());
        assertEquals((Integer)1,coupon.getStatus());
    }
    public void matchCouponTrue(OrderVO orderVO,CouponVO coupon){
        int oldNum = couponService.getMatchOrderCoupon(orderVO).size();

        addCoupon(coupon,true);//添加不成功则报错
        assertEquals(oldNum+1,couponService.getMatchOrderCoupon(orderVO).size());
    }

    public void matchCouponFalse(OrderVO orderVO,CouponVO coupon){
        int oldNum = couponService.getMatchOrderCoupon(orderVO).size();

        addCoupon(coupon,true);//添加不成功则报错
        assertEquals(oldNum,couponService.getMatchOrderCoupon(orderVO).size());
    }
}
