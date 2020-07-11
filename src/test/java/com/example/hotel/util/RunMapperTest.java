package com.example.hotel.util;

import com.example.hotel.data.admin.AdminMapperTest;
import com.example.hotel.data.coupon.CouponMapperTest;
import com.example.hotel.data.hotel.HotelAndRoomMapperTest;
import com.example.hotel.data.marketer.MarketerMapperTest;
import com.example.hotel.data.order.OrderMapperTest;
import com.example.hotel.data.rar.RarMapperTest;
import com.example.hotel.data.user.AccountMapperTest;
import com.example.hotel.data.vip.VIPMapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AdminMapperTest.class, AccountMapperTest.class,
        CouponMapperTest.class, HotelAndRoomMapperTest.class,
        MarketerMapperTest.class, OrderMapperTest.class,
        RarMapperTest.class, VIPMapperTest.class})
public class RunMapperTest {
    /*不能写任何代码*/
}
