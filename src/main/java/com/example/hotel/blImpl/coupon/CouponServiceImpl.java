package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.data.coupon.CouponMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.Coupon;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.*;
/*作业改动 by檀潮*/ //修改HotelTargetMoneyCouponVO为TargetMoneyCouponVO
/*作业改动*/
/*作业改动 by檀潮*/ //添加
/*作业改动*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


@Service
public class CouponServiceImpl implements CouponService {

    private final VIPCouponStrategyImpl vipCouponStrategy;
    private final RoomsCouponStrategyImpl roomsCouponStrategy;
    private final TargetMoneyCouponStrategyImpl targetMoneyCouponStrategy;
    private final TimeCouponStrategyImpl timeCouponStrategy;
    private final CouponMapper couponMapper;

    private static List<CouponMatchStrategy> strategyList = new ArrayList<>();

    @Autowired
    public CouponServiceImpl(CouponMapper couponMapper,
                             VIPCouponStrategyImpl vipCouponStrategy,
                             RoomsCouponStrategyImpl roomsCouponStrategy,
                             TargetMoneyCouponStrategyImpl targetMoneyCouponStrategy,
                             TimeCouponStrategyImpl timeCouponStrategy) {
        this.couponMapper = couponMapper;
        this.vipCouponStrategy = vipCouponStrategy;
        this.roomsCouponStrategy = roomsCouponStrategy;
        this.targetMoneyCouponStrategy = targetMoneyCouponStrategy;
        this.timeCouponStrategy = timeCouponStrategy;
        strategyList.add(roomsCouponStrategy);
        strategyList.add(targetMoneyCouponStrategy);
        strategyList.add(timeCouponStrategy);
        strategyList.add(vipCouponStrategy);
    }

    //此方法有待更改或补充
    @Override
    public List<Coupon> getMatchOrderCoupon(OrderVO orderVO) {

        List<Coupon> hotelCoupons = getHotelAllCoupon(orderVO.getHotelId());

        //添加检索网站策略的逻辑如下，若检查无误即可使用
        List<Coupon> websiteCoupons = getHotelAllCoupon(-1);
        if(websiteCoupons!=null){
            hotelCoupons.addAll(websiteCoupons);
        }

        List<Coupon> availAbleCoupons = new ArrayList<>();

        for (int i = 0; i < hotelCoupons.size(); i++) {
            for (CouponMatchStrategy strategy : strategyList) {
                //应对以null为初值的strategy的出现
                if (strategy!=null&&strategy.isMatch(orderVO, hotelCoupons.get(i))) {
                    availAbleCoupons.add(hotelCoupons.get(i));
                    break;//只要没有混合类型的coupon，就可以break
                }
            }
        }

        return availAbleCoupons;
    }
    //依赖CouponService.getHotelAllCoupon(Integer hotelId)
    //依赖CouponMatchStrategy.isMatch(OrderVO orderVO, Coupon coupon)

    @Override
    public List<Coupon> getHotelAllCoupon(Integer hotelId) {//hotelId为-1时搜索网站优惠策略
        List<Coupon> hotelCoupons = couponMapper.selectByHotelId(hotelId);
        return hotelCoupons;
    }
    //依赖data.coupon.CouponMapper.selectByHotelId(Integer hotelId)

    @Override
    public CouponVO addTargetMoneyCoupon(TargetMoneyCouponVO couponVO) throws ServiceException{
        if(couponVO.getTargetMoney()<0||couponVO.getDiscountMoney()<0){
            throw new ServiceException("满减条件或者满减金额不是有效的");
        }
        Coupon coupon = new Coupon();
        coupon.setCouponName(couponVO.getName());
        coupon.setDescription(couponVO.getDescription());
        coupon.setCouponType(3);
        coupon.setTargetMoney(couponVO.getTargetMoney());//按照discount与targetMoney的正负区分类型
        coupon.setDiscountMoney(couponVO.getDiscountMoney());
        coupon.setHotelId(couponVO.getHotelId());
        coupon.setStatus(1);
        couponMapper.insertCoupon(coupon);
        Integer result = coupon.getId();
        couponVO.setId(result);
        return couponVO;
    }
    //依赖data.coupon.CouponMapper.insertCoupon(Coupon coupon)

    @Override
    public CouponVO addTimeCoupon(TimeCouponVO couponVO) throws ServiceException{
        boolean validDiscount = couponVO.getDiscount()>=0&&couponVO.getDiscount()<=1;
        boolean validDiscountMoney = couponVO.getDiscountMoney()>=0;
        if(couponVO.getStartTime()==null||couponVO.getEndTime()==null||
                couponVO.getStartTime().compareTo(couponVO.getEndTime())>=0){
            throw new ServiceException("优惠时间区间不是有效的");
        }
        if((validDiscount&&validDiscountMoney)||
                (!validDiscount&&!validDiscountMoney)){
            throw new ServiceException("优惠金额与优惠折扣不能同时有效或同时无效");
        }
        Coupon coupon = new Coupon();
        coupon.setCouponName(couponVO.getName());
        coupon.setDescription(couponVO.getDescription());
        coupon.setCouponType(4);
        if(validDiscount){
            couponVO.setDiscount((int)(couponVO.getDiscount()*1000+0.5)/1000.0);
        }
        coupon.setDiscount(couponVO.getDiscount());//按照discount与discountMoney的正负区分类型
        coupon.setDiscountMoney(couponVO.getDiscountMoney());
        coupon.setHotelId(couponVO.getHotelId());

        //数据库精确到秒，为使时间存入和取出时一致，提前精确到秒
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        coupon.setStartTime(LocalDateTime.parse(df.format(couponVO.getStartTime()),df));
        coupon.setEndTime(LocalDateTime.parse(df.format(couponVO.getEndTime()),df));

        coupon.setStatus(1);
        couponMapper.insertCoupon(coupon);
        Integer result = coupon.getId();
        couponVO.setId(result);
        return couponVO;
    }
    //依赖data.coupon.CouponMapper.insertCoupon(Coupon coupon)

    @Override
    public CouponVO addRoomsCoupon(RoomsCouponVO couponVO) throws ServiceException{
        boolean validDiscount = couponVO.getDiscount()>=0&&couponVO.getDiscount()<=1;
        boolean validDiscountMoney = couponVO.getDiscountMoney()>=0;
        if((validDiscount&&validDiscountMoney)||
                (!validDiscount&&!validDiscountMoney)){
            throw new ServiceException("优惠金额与优惠折扣不能同时有效或同时无效");
        }
        Coupon coupon = new Coupon();
        coupon.setCouponName(couponVO.getName());
        coupon.setDescription(couponVO.getDescription());
        coupon.setCouponType(2);
        if(validDiscount){
            couponVO.setDiscount((int)(couponVO.getDiscount()*1000+0.5)/1000.0);
        }
        coupon.setDiscount(couponVO.getDiscount());//按照discount与discountMoney的正负区分类型
        coupon.setDiscountMoney(couponVO.getDiscountMoney());
        coupon.setHotelId(couponVO.getHotelId());
        coupon.setRoomNum(couponVO.getRoomNum());
        coupon.setStatus(1);
        couponMapper.insertCoupon(coupon);
        Integer result = coupon.getId();
        couponVO.setId(result);
        return couponVO;
    }
    //依赖data.coupon.CouponMapper.insertCoupon(Coupon coupon)

    @Override//当输入的商圈与等级不符格式时，返回id为-1的couponVO
    public CouponVO addVIPCoupon(VIPCouponVO couponVO) throws ServiceException {
        boolean validDiscount = couponVO.getDiscount()>=0&&couponVO.getDiscount()<=1;
        boolean validDiscountMoney = couponVO.getDiscountMoney()>=0;
        if((validDiscount&&validDiscountMoney)||
                (!validDiscount&&!validDiscountMoney)){
            throw new ServiceException("优惠金额与优惠折扣不能同时有效或同时无效");
        }
        Coupon coupon = new Coupon();
        if(couponVO.getHotelId()==-1){
            BizRegion bizRegion = BizRegion.parse(couponVO.getBizRegion());
            if(couponVO.getBizRegion()!=null&&bizRegion==null){
                throw new ServiceException("商圈格式不正确");
            }
            VIPLevel vipLevel = VIPLevel.parse(couponVO.getVIPLevel());
            if(couponVO.getVIPLevel()!=null&&vipLevel==null){
                throw new ServiceException("VIP等级格式不正确");
            }
            coupon.setBizRegion(bizRegion);
            coupon.setVIPLevel(vipLevel);
        }
        coupon.setCouponName(couponVO.getName());
        coupon.setDescription(couponVO.getDescription());
        coupon.setCouponType(1);
        if(validDiscount){
            couponVO.setDiscount((int)(couponVO.getDiscount()*1000+0.5)/1000.0);
        }
        coupon.setDiscount(couponVO.getDiscount());//按照discount与discountMoney的正负区分类型
        coupon.setDiscountMoney(couponVO.getDiscountMoney());
        coupon.setHotelId(couponVO.getHotelId());
        coupon.setStatus(1);
        couponMapper.insertCoupon(coupon);
        Integer result = coupon.getId();
        couponVO.setId(result);
        return couponVO;
    }
    //依赖data.coupon.CouponMapper.insertCoupon(Coupon coupon)

    @Override
    public ResponseVO deleteCoupon(Integer couponId) {
        try{
            couponMapper.deleteCoupon(couponId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("删除优惠策略任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.coupon.CouponMapper.deleteCoupon(Integer id)



}
