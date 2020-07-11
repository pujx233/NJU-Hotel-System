/*作业改动 by檀潮*/ //添加

package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.data.vip.VIPMapper;
import com.example.hotel.po.Coupon;
import com.example.hotel.po.VIP;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VIPCouponStrategyImpl implements CouponMatchStrategy {
    @Autowired
    VIPMapper vipMapper;
    @Autowired
    HotelService hotelService;

    /**
     * 判断某个订单是否满足VIP生日优惠策略
     * @param orderVO
     * @param coupon
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        try{//couponType为1
            if(coupon.getCouponType()==1){

                //利用OrderVO中的userId以及data.vip.VIPMapper.getVIPById(int userId)查找会员信息
                //若返回值为空则说明不是会员，不能使用会员优惠，直接return false
                VIP vip = vipMapper.getVIPByUserId(orderVO.getUserId());
                if(vip==null){
                    return false;
                }

                if(coupon.getHotelId()==-1){
                    //此处为网站提供的VIP特惠，没有生日特惠
                    if(coupon.getBizRegion()!=null){
                        HotelVO hotelVO = hotelService.retrieveHotelDetails(orderVO.getHotelId());
                        if(hotelVO.getBizRegion()==null||!hotelVO.getBizRegion().equals(coupon.getBizRegion().toString())){
                            return false;
                        }
                    }
                    if(coupon.getVIPLevel()!=null){
                        if(Integer.parseInt(vip.getLevel().toString())<Integer.parseInt(coupon.getVIPLevel().toString())){
                            return false;
                        }
                    }
                    return true;
                }else{
                    //此处为酒店提供的VIP特惠，即生日特惠，没有用到bizRegion和vipLevel
                    //从会员信息中取出生日与当前日期进行比对，相符则return true
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime birthday = vip.getBirthday();
                    if(birthday.getMonth().equals(now.getMonth())&&birthday.getDayOfMonth()==now.getDayOfMonth()){
                        return true;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //依赖data.coupon.CouponMapper.insertCoupon(Coupon coupon)
    //依赖data.vip.VIPMapper.getVIPById(int userId)
    //依赖bl.hotel.HotelService.retrieveHotelDetails(Integer hotelId)
}

/*作业改动*/