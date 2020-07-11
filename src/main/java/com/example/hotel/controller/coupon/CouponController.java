package com.example.hotel.controller.coupon;

import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    private final static String COUPON_ADD_ERROR = "添加优惠策略任务中发生错误";

    @Autowired
    private CouponService couponService;

    /*作业改动 by檀潮*/ //修改
    //由于网站也可以有满减优惠，建议修改url"/hotelTargetMoney"为"/targetMoney"，后端相关接口已修改
    @PostMapping("/targetMoney")
    public ResponseVO addTargetMoneyCoupon(@RequestBody TargetMoneyCouponVO targetMoneyCouponVO) {
        try{
            CouponVO couponVO = couponService.addTargetMoneyCoupon(targetMoneyCouponVO);
            if(couponVO.getId()!=null&&couponVO.getId()>0){
                return ResponseVO.buildSuccess(couponVO);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(COUPON_ADD_ERROR+": "+e.getMessage());
        }
        return ResponseVO.buildFailure(COUPON_ADD_ERROR);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/time")
    public ResponseVO addTimeCoupon(@RequestBody TimeCouponVO timeCouponVO) {
        try{
            CouponVO couponVO = couponService.addTimeCoupon(timeCouponVO);
            if(couponVO.getId()!=null&&couponVO.getId()>0){
                return ResponseVO.buildSuccess(couponVO);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(COUPON_ADD_ERROR+": "+e.getMessage());
        }
        return ResponseVO.buildFailure(COUPON_ADD_ERROR);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/rooms")
    public ResponseVO addRoomsCoupon(@RequestBody RoomsCouponVO roomsCouponVO) {
        try{
            CouponVO couponVO = couponService.addRoomsCoupon(roomsCouponVO);
            if(couponVO.getId()!=null&&couponVO.getId()>0){
                return ResponseVO.buildSuccess(couponVO);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(COUPON_ADD_ERROR+": "+e.getMessage());
        }
        return ResponseVO.buildFailure(COUPON_ADD_ERROR);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/vip")
    public ResponseVO addVIPCoupon(@RequestBody VIPCouponVO vipCouponVO) {
        try{
            CouponVO couponVO = couponService.addVIPCoupon(vipCouponVO);
            if(couponVO.getId()!=null&&couponVO.getId()>0){
                return ResponseVO.buildSuccess(couponVO);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(COUPON_ADD_ERROR+": "+e.getMessage());
        }
        return ResponseVO.buildFailure(COUPON_ADD_ERROR);
    }
    /*作业改动*/

    //若要获取网站策略，将hotelId置为-1即可
    @GetMapping("/hotelAllCoupons")
    public ResponseVO getHotelAllCoupons(@RequestParam Integer hotelId) {
        return ResponseVO.buildSuccess(couponService.getHotelAllCoupon(hotelId));
    }


    @GetMapping("/orderMatchCoupons")
    public ResponseVO getOrderMatchCoupons(@RequestParam Integer userId,
                                           @RequestParam Integer hotelId,
                                           @RequestParam Double orderPrice,
                                           @RequestParam Integer roomNum,
                                           @RequestParam String checkIn,
                                           @RequestParam String checkOut) {
        OrderVO requestOrderVO = new OrderVO();
        requestOrderVO.setUserId(userId);
        requestOrderVO.setHotelId(hotelId);
        requestOrderVO.setPrice(orderPrice);
        requestOrderVO.setRoomNum(roomNum);
        requestOrderVO.setCheckInDate(checkIn);
        requestOrderVO.setCheckOutDate(checkOut);
        return ResponseVO.buildSuccess(couponService.getMatchOrderCoupon(requestOrderVO));
    }

    @PostMapping("/{couponId}/remove")
    public ResponseVO removeCoupon(@PathVariable Integer couponId){
        return couponService.deleteCoupon(couponId);
    }

}
