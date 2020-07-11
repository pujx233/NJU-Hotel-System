package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public class RoomsCouponStrategyImpl implements CouponMatchStrategy {


    /**
     * 判断某个订单是否满足某种多间优惠策略
     * @param orderVO
     * @param coupon
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        try{//couponType暂定2
            Integer roomNum = coupon.getRoomNum();
            if(roomNum==null){
                roomNum = 3;//不传入roomNum时默认为3
            }
            if (coupon.getCouponType() == 2 && orderVO.getRoomNum() >= roomNum) {
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //依赖data.coupon.CouponMapper.insertCoupon(Coupon coupon)
}
