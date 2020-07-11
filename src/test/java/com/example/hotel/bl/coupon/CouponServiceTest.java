package com.example.hotel.bl.coupon;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.coupon.CouponService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.logic.coupon.*;
import com.example.hotel.logic.hotel.HotelLogic;
import com.example.hotel.logic.user.*;
import com.example.hotel.logic.vip.VIPLogic;
import com.example.hotel.po.Coupon;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

public class CouponServiceTest extends BaseTest {//主要是为了继承BaseTest的注释来注入依赖等等，详见该类

    @Autowired
    CouponService couponService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountService accountService;
    @Autowired
    AdminService adminService;
    @Autowired
    VIPService vipService;


    private CouponLogic couponLogic;


    public void testService(int hotelTest,int webTest){
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        HotelLogic hotelLogic = new HotelLogic(adminService);
        int adminId = adminLogic.administrator();
        int hotelId = hotelLogic.addHotel(hotelLogic.generateHotel(),adminId);
        int oldNum = couponLogic.getCoupons(-1).size();
        //创建管理人员，获取adminId用来创建酒店，获取hotelId用来插入优惠策略,oldNum现有网站优惠数量

        List<Coupon> coupons = couponLogic.getCoupons(hotelId);
        assertEquals(0,coupons.size());
        //酒店新创建，优惠策略数量为0

        List<CouponVO> couHotel = couponLogic.insertCoupons(hotelTest,hotelId);
        List<CouponVO> couWeb = couponLogic.insertCoupons(hotelTest,-1);
        //以上的插入在奇数序数上都故意设置会插入失败的couponVO，验证couponService.addTargetMoneyCoupon,
        //couponService.addTimeCoupon,couponService.addRoomsCoupon,couponService.addVIPCoupon
        //对正确和错误参数的分别处理，详见logic.coupon包内的类


        coupons = couponLogic.getCoupons(hotelId);
        assertEquals((int)Math.ceil(couHotel.size()/2.0),coupons.size());
        //用插入数量验证bl层的优惠插入（包括CouponService.add……）,只有(int)Math.ceil(couHotel.size()/2)个成功插入
        for (CouponVO couponVO:couHotel) {
            if(couponVO.getId()!=null){
                boolean match = false;
                for(Coupon coupon:coupons){
                    if(couponVO.getId()==coupon.getId()){
                        match = true;
                        couponLogic.equalAfterInsert(couponVO,coupon);
                    }
                }
                assertTrue(match);
            }
        }//couponVO插入成功的时候id不会是null，必定能找到id相同的匹配，并进行属性的比较，
        // 检验存取的数据格式变化是否符合预期，详见logic.coupon类中

        coupons = couponLogic.getCoupons(-1);
        assertEquals(oldNum+(int)Math.ceil(couWeb.size()/2.0),coupons.size());
        //用插入数量验证bl层的优惠插入（包括CouponService.add……）,只有(int)Math.ceil(couHotel.size()/2)个成功插入
        for (CouponVO couponVO:couWeb) {
            if(couponVO.getId()!=null){
                boolean match = false;
                for(Coupon coupon:coupons){
                    if(couponVO.getId()==coupon.getId()){
                        match = true;
                        couponLogic.equalAfterInsert(couponVO,coupon);
                    }
                }
                assertTrue(match);
            }
        }//couponVO插入成功的时候id不会是null，必定能找到id相同的匹配，并进行属性的比较，
        // 检验存取的数据格式变化是否符合预期，详见logic.coupon类中


        for (CouponVO couponVO:couHotel) {
            if(couponVO.getId()!=null){
                couponLogic.deleteById(couponVO.getId());
            }
        }
        coupons = couponLogic.getCoupons(hotelId);
        assertEquals(0,coupons.size());
        //用前后记录对比验证bl层的用户删除，检验couponService.deleteCoupon

        for (CouponVO couponVO:couWeb) {
            if(couponVO.getId()!=null){
                couponLogic.deleteById(couponVO.getId());
            }
        }
        coupons = couponLogic.getCoupons(hotelId);
        assertEquals(0,coupons.size());
        //用前后记录对比验证bl层的用户删除，检验couponService.deleteCoupon

        hotelLogic.removeHotel(hotelId,adminId);
        accountMapper.deleteAccountById(adminId);
        //删除酒店和人员，清理现场
    }



    @BeforeClass//因为没能解决被删除的序号空悬的问题，故每次测试前都重新执行一次sql，防止序号增加至过大
    public static void setUp(){
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @AfterClass//清理现场
    public static void tearDown(){
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }

    @Test
    public void testWithTargetMoney(){
        this.couponLogic = new TargetMoneyLogic(couponService);
        testService(19,17);
    }

    @Test
    public void testWithTime(){
        this.couponLogic = new TimeLogic(couponService);
        testService(23,7);
    }

    @Test
    public void testWithRooms(){
        this.couponLogic = new RoomsLogic(couponService);
        testService(13,23);
    }

    @Test
    public void testWithVIP(){
        this.couponLogic = new RoomsLogic(couponService);
        testService(23,17);
    }

    @Test
    public void testWithCouponMatch(){
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();

        HotelVO hotelVO = new HotelVO();
        hotelVO.setName("tmpHotel");
        hotelVO.setBizRegion(BizRegion.WangFuJing.toString());
        hotelVO.setHotelStar(HotelStar.Four.toString());
        ResponseVO responseVO = adminService.addHotel(hotelVO,adminId);
        assertTrue(responseVO.getSuccess());
        int hotelId = (Integer)responseVO.getContent();
        //因为要根据hotelVO的属性进行后续测试，故显式添加HotelVO，获取hotelId

        ClientLogic clientLogic = new ClientLogic(accountMapper,accountService,adminService);
        User user = clientLogic.generateUser(0);
        clientLogic.addUser(user);
        int userId = user.getId();
        //因为不需要使用user的id与credit以外的属性，故用clientLogic创造（credit初始100）

        OrderVO orderVO = new OrderVO();
        orderVO.setHotelId(hotelId);
        orderVO.setUserId(userId);
        orderVO.setCheckInDate("2070-02-01");
        orderVO.setCheckOutDate("2100-03-01");
        orderVO.setRoomNum(3);
        orderVO.setPrice(500.0);

        CouponVO couponTrue = null;//能被匹配到的coupon
        CouponVO couponFalse = null;//不能被匹配到的coupon

        TargetMoneyLogic targetMoneyLogic = new TargetMoneyLogic(couponService);
        couponTrue =  targetMoneyLogic.generateCoupon(0,hotelId,400,100);
        couponFalse = targetMoneyLogic.generateCoupon(0,hotelId,600,100);
        targetMoneyLogic.matchCouponTrue(orderVO,couponTrue);
        targetMoneyLogic.matchCouponFalse(orderVO,couponFalse);
        couponTrue.setHotelId(-1);
        couponFalse.setHotelId(-1);
        targetMoneyLogic.matchCouponTrue(orderVO,couponTrue);
        targetMoneyLogic.matchCouponFalse(orderVO,couponFalse);
        //测试满减优惠的匹配

        TimeLogic timeLogic = new TimeLogic(couponService);
        DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeStart = LocalDateTime.parse("2000-01-01 00:00:00",df_toSecond);
        LocalDateTime timeEndTrue = LocalDateTime.parse("2080-01-01 00:00:00",df_toSecond);
        LocalDateTime timeEndFalse = LocalDateTime.parse("2060-01-01 00:00:00",df_toSecond);
        couponTrue = timeLogic.generateCoupon(0,hotelId,timeStart,timeEndTrue,0.1,-1);
        couponFalse = timeLogic.generateCoupon(0,hotelId,timeStart,timeEndFalse,0.1,-1);
        timeLogic.matchCouponTrue(orderVO,couponTrue);
        timeLogic.matchCouponFalse(orderVO,couponFalse);
        couponTrue = couponFalse;
        couponTrue.setHotelId(-1);
        timeEndFalse = LocalDateTime.parse("2010-01-01 00:00:00",df_toSecond);
        couponFalse = timeLogic.generateCoupon(0,-1,timeStart,timeEndFalse,0.1,-1);
        timeLogic.matchCouponTrue(orderVO,couponTrue);
        timeLogic.matchCouponFalse(orderVO,couponFalse);
        //测试限时优惠的匹配

        RoomsLogic roomsLogic = new RoomsLogic(couponService);
        int roomNumTrue = 2;
        int roomNumFalse = 8;
        couponTrue = roomsLogic.generateCoupon(0,hotelId,roomNumTrue,-1,3);
        couponFalse = roomsLogic.generateCoupon(0,hotelId,roomNumFalse,-1,3);
        roomsLogic.matchCouponTrue(orderVO,couponTrue);
        roomsLogic.matchCouponFalse(orderVO,couponFalse);
        couponTrue.setHotelId(-1);
        couponFalse.setHotelId(-1);
        roomsLogic.matchCouponTrue(orderVO,couponTrue);
        roomsLogic.matchCouponFalse(orderVO,couponFalse);
        //测试多间优惠的匹配

        VIPCouponLogic vipCouponLogic = new VIPCouponLogic(couponService);
        VIPLogic vipLogic = new VIPLogic(vipService);
        while(true){//生成的生日是今天，为了防止在凌晨附近测试时由于日期变化而使得测试失败，检测日期到变化就循环后再测试
            LocalDateTime timeBefore = LocalDateTime.now();

            User tmpUser = clientLogic.generateUser(0);
            clientLogic.addUser(tmpUser);
            orderVO.setUserId(tmpUser.getId());//将订单对应用户置为当前所要用来测试的用户
            VIPVO vipvo = vipLogic.generateVIPVO(tmpUser.getId(),df_toSecond.format(timeBefore));
            couponTrue = vipCouponLogic.generateCoupon(0,hotelId,BizRegion.WangFuJing,VIPLevel.Hamon,0.1,-1);
            vipCouponLogic.matchCouponFalse(orderVO,couponTrue);
            //未注册会员时无法匹配成功

            assertTrue(vipService.registerVIP(vipvo).getSuccess());
            int oldNum = couponService.getMatchOrderCoupon(orderVO).size();
            try{
                couponService.addVIPCoupon((VIPCouponVO) couponTrue);
            }catch (Exception e){fail();}
            int newNum = couponService.getMatchOrderCoupon(orderVO).size();

            LocalDateTime timeAfter = LocalDateTime.now();
            if(!timeAfter.getMonth().equals(timeBefore.getMonth())||timeAfter.getDayOfMonth()!=timeBefore.getDayOfMonth()){
                continue;
            }
            assertEquals(oldNum+1,newNum);
            break;
        }
        LocalDateTime timeBefore = LocalDateTime.now();

        User tmpUser = clientLogic.generateUser(0);
        clientLogic.addUser(tmpUser);
        orderVO.setUserId(tmpUser.getId());//将订单对应用户置为当前所要用来测试的用户
        VIPVO tmpvipvo = vipLogic.generateVIPVO(tmpUser.getId(),"2000-"+(timeBefore.getMonth().getValue()==7?"08":"07")+"-15 00:00:00");
        assertTrue(vipService.registerVIP(tmpvipvo).getSuccess());

        couponFalse = vipCouponLogic.generateCoupon(0,hotelId,BizRegion.WangFuJing,VIPLevel.Hamon,0.1,-1);
        int oldNum = couponService.getMatchOrderCoupon(orderVO).size();
        try{
            couponService.addVIPCoupon((VIPCouponVO) couponFalse);
        }catch (Exception e){fail();}
        int newNum = couponService.getMatchOrderCoupon(orderVO).size();
        assertEquals(oldNum,newNum);
        //验证酒店制定的VIP优惠（生日优惠）的匹配

        orderVO.setUserId(userId);//将订单对应用户置为当前所要用来测试的用户
        VIPVO vipvo = vipLogic.generateVIPVO(userId);
        couponTrue = vipCouponLogic.generateCoupon(0,-1,BizRegion.WangFuJing,VIPLevel.Hamon,0.1,-1);
        vipCouponLogic.matchCouponFalse(orderVO,couponTrue);
        //未注册会员时无法匹配成功

        assertTrue(vipService.registerVIP(vipvo).getSuccess());
        vipService.setVIPLevel(vipService.getVIPInfo(user.getId()).getId(),VIPLevel.Hamon);
        couponTrue = vipCouponLogic.generateCoupon(0,-1,BizRegion.WangFuJing,VIPLevel.Hamon,0.1,-1);
        vipCouponLogic.matchCouponTrue(orderVO,couponTrue);

        couponFalse = vipCouponLogic.generateCoupon(0,-1,BizRegion.DaZhaLan,VIPLevel.Hamon,0.1,-1);
        vipCouponLogic.matchCouponFalse(orderVO,couponFalse);
        couponFalse = vipCouponLogic.generateCoupon(0,-1,BizRegion.WangFuJing,VIPLevel.GoldExperience,0.1,-1);
        vipCouponLogic.matchCouponFalse(orderVO,couponFalse);
        //测试网站制定的VIP优惠（商圈、会员等级）的匹配
    }
}


