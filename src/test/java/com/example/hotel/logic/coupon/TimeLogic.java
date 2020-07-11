package com.example.hotel.logic.coupon;

import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.RoomsCouponVO;
import com.example.hotel.vo.TimeCouponVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TimeLogic extends CouponLogic{

    public TimeLogic(CouponService couponService){this.couponService = couponService;}
    @Override
    public Integer couponType() { return 4; }

    @Override
    public CouponVO addCoupon(CouponVO couponVO,boolean success) {
        try{
            couponVO = couponService.addTimeCoupon((TimeCouponVO) couponVO);
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
        DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time1 = LocalDateTime.parse("2000-01-01 00:00:00",df_toSecond);
        LocalDateTime time2 = LocalDateTime.parse(df_toSecond.format(LocalDateTime.now()),df_toSecond);
        return generateCoupon(flag,hotelId,time1,flag%2==0?time2:null,0.254,-1);
    }

    public CouponVO generateCoupon(int flag, int hotelId, LocalDateTime startTime, LocalDateTime endTime,
                                   double discount,Integer discountMoney){
        TimeCouponVO timeCouponVO = new TimeCouponVO();
        CouponVO couponVO = super.generateCoupon(flag,hotelId,timeCouponVO);

        timeCouponVO = (TimeCouponVO)couponVO;
        timeCouponVO.setStartTime(startTime);
        timeCouponVO.setEndTime(endTime);
        timeCouponVO.setDiscount(discount);
        timeCouponVO.setDiscountMoney(discountMoney);
        return timeCouponVO;
    }

    @Override
    public void equalAfterInsert(CouponVO couponVO,Coupon coupon){
        TimeCouponVO timeCouponVO = (TimeCouponVO)couponVO;
        super.equalAfterInsert(couponVO,coupon);
        assertEquals(timeCouponVO.getEndTime(),coupon.getEndTime());
        assertEquals(timeCouponVO.getStartTime(),coupon.getStartTime());
        assertEquals(timeCouponVO.getDiscountMoney(),coupon.getDiscountMoney(),0.0001);
        assertEquals(timeCouponVO.getDiscount(),coupon.getDiscount(),0.0001);
    }
}
