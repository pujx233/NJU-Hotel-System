package com.example.hotel.bl.coupon;

import com.example.hotel.po.Coupon;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.*;

import javax.xml.ws.Service;
import java.util.List;

public interface CouponService {
    /**
     * 返回某一订单可用的优惠策略列表
     * @param orderVO
     * @return
     */
    List<Coupon> getMatchOrderCoupon(OrderVO orderVO);

    /**
     * 查看某个酒店提供的所有优惠策略（包括失效的）
     * @param hotelId
     * @return
     */
    List<Coupon> getHotelAllCoupon(Integer hotelId);

    /*作业改动 by檀潮*/ //修改
    /**
     * 添加满减优惠策略 //由于网站也可以有满减优惠，故删去注释中“酒店满减”中的“酒店”二字
     * @param couponVO
     * @return
     */
    //由于网站也可以有满减优惠，故修改接口名addHotelTargetMoneyCoupon为addTargetMoneyCoupon
    CouponVO addTargetMoneyCoupon(TargetMoneyCouponVO couponVO) throws ServiceException;
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 添加限时优惠策略
     * @param couponVO
     * @return
     */
    CouponVO addTimeCoupon(TimeCouponVO couponVO) throws ServiceException;
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 添加多间优惠策略
     * @param couponVO
     * @return
     */
    CouponVO addRoomsCoupon(RoomsCouponVO couponVO) throws ServiceException;
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 添加VIP优惠策略
     * @param couponVO
     * @return
     */
    CouponVO addVIPCoupon(VIPCouponVO couponVO) throws ServiceException;
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 删除优惠策略
     * @param couponId
     * @return
     */
    ResponseVO deleteCoupon(Integer couponId);
    /*作业改动*/
}
