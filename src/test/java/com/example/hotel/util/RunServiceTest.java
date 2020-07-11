package com.example.hotel.util;

import com.example.hotel.bl.admin.AdminServiceTest;
import com.example.hotel.bl.coupon.CouponServiceTest;
import com.example.hotel.bl.hotel.HotelAndRoomServiceTest;
import com.example.hotel.bl.marketer.MarketerServiceTest;
import com.example.hotel.bl.order.OrderServiceTest;
import com.example.hotel.bl.rar.RARServiceTest;
import com.example.hotel.bl.user.AccountServiceTest;
import com.example.hotel.bl.vip.VIPServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({AdminServiceTest.class, CouponServiceTest.class,
        AccountServiceTest.class, VIPServiceTest.class,
        HotelAndRoomServiceTest.class, OrderServiceTest.class,
        MarketerServiceTest.class, RARServiceTest.class})
public class RunServiceTest {
    /*不能写任何代码*/
}
