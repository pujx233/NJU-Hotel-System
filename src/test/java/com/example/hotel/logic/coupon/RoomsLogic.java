package com.example.hotel.logic.coupon;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.RoomsCouponVO;
import com.example.hotel.vo.TargetMoneyCouponVO;
import com.example.hotel.vo.TimeCouponVO;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RoomsLogic extends CouponLogic{

    public RoomsLogic(CouponService couponService){this.couponService = couponService;}
    @Override
    public Integer couponType() {return 2;}

    @Override
    public CouponVO addCoupon(CouponVO couponVO,boolean success) {
        try{
            couponVO = couponService.addRoomsCoupon((RoomsCouponVO) couponVO);
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
        return generateCoupon(flag,hotelId,flag%5,flag%2==0?-1:1.3,flag%2==0?8:-1);
    }

    public CouponVO generateCoupon(int flag, int hotelId, Integer roomNum,double discount,Integer discountMoney){
        RoomsCouponVO roomsCouponVO = new RoomsCouponVO();
        CouponVO couponVO = super.generateCoupon(flag,hotelId,roomsCouponVO);
        roomsCouponVO = (RoomsCouponVO)couponVO;
        roomsCouponVO.setRoomNum(roomNum);
        roomsCouponVO.setDiscount(discount);
        roomsCouponVO.setDiscountMoney(discountMoney);
        return roomsCouponVO;
    }

    @Override
    public void equalAfterInsert(CouponVO couponVO, Coupon coupon){
        RoomsCouponVO roomsCouponVO = (RoomsCouponVO)couponVO;
        super.equalAfterInsert(couponVO,coupon);
        if(((RoomsCouponVO) couponVO).getRoomNum()==null){
            assertEquals(3,coupon.getRoomNum());
        }else {
            assertEquals(((RoomsCouponVO) couponVO).getRoomNum(),(Integer)coupon.getRoomNum());
        }
        assertEquals(roomsCouponVO.getDiscountMoney(),coupon.getDiscountMoney(),0.0001);
        assertEquals(roomsCouponVO.getDiscount(),coupon.getDiscount(),0.0001);
    }
}
