package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TimeCouponStrategyImpl implements CouponMatchStrategy {


    /**
     * 判断某个订单是否满足某种限时优惠策略
     * @param orderVO
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        /*作业改动 by檀潮*/
        try{//couponType暂定4
            if (coupon.getCouponType() == 4) {
                if(coupon.getHotelId()==-1){
                    LocalDateTime createTime = LocalDateTime.now();
                    if(createTime.compareTo(coupon.getStartTime())>=0&&
                            createTime.compareTo(coupon.getEndTime())<0){
                        return true;
                    }
                }else{
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime inTime = LocalDateTime.parse(orderVO.getCheckInDate()+" 23:59:59",df);
                    if(inTime.compareTo(coupon.getStartTime())>=0&&
                            inTime.compareTo(coupon.getEndTime())<0){
                        return true;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
        /*作业改动*/
    }
    //依赖data.coupon.CouponMapper.insertCoupon(Coupon coupon)
}
