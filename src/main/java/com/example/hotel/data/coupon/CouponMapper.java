package com.example.hotel.data.coupon;

import com.example.hotel.po.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CouponMapper {
    int insertCoupon(Coupon coupon);

    /*作业改动 by檀潮*/ //补上@Param("hotelId")
    List<Coupon> selectByHotelId(@Param("hotelId") Integer hotelId);
    /*作业改动*/

    /*作业改动 by檀潮*/
    int deleteCoupon(@Param("id") Integer id);
    /*作业改动*/
}
