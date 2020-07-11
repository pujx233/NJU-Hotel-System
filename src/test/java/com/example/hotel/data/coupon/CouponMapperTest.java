package com.example.hotel.data.coupon;

import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.Coupon;
import com.example.hotel.data.coupon.CouponMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import org.junit.BeforeClass;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class CouponMapperTest extends BaseTest {
    @Autowired
    CouponMapper couponMapper;

    public void testMapper(int testNum){
        int hotelId = 5;//任意不违背数据库的hotelId

        List<Coupon> coupons = couponMapper.selectByHotelId(hotelId);
        int oldNum = coupons.size();

        List<Coupon> couponList = new ArrayList<>();


        Random r = new Random();
        for(int i=0;i<testNum;i++){
            Coupon coupon = new Coupon();
            coupon.setDescription("test");
            coupon.setHotelId(hotelId);
            coupon.setBizRegion(BizRegion.WangFuJing);
            coupon.setVIPLevel(VIPLevel.Hamon);

            //数据库精确到秒
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            coupon.setStartTime(LocalDateTime.parse(df.format(LocalDateTime.now()),df));
            coupon.setEndTime(LocalDateTime.parse(df.format(LocalDateTime.now()),df));

            coupon.setTargetMoney(2000);
            coupon.setDiscountMoney(-21);
            coupon.setStatus(1);
            //初始化coupon实例

            coupon.setCouponName("test"+i);
            coupon.setCouponType(i*i%4+1);
            coupon.setRoomNum(i*i%20);
            coupon.setDiscount(r.nextInt(1000)/1000.0);
            couponMapper.insertCoupon(coupon);
            couponList.add(coupon);
        }
        //用couponMapper.insertCoupon插入数据

        coupons = couponMapper.selectByHotelId(hotelId);
        assertEquals(oldNum+testNum,coupons.size());
        //用插入数量验证couponMapper.selectByHotelId与couponMapper.insertCoupon

        for(Coupon coupon1:couponList){
            for(Coupon coupon2:coupons){
                boolean match = false;
                if(coupon1.getId()==coupon2.getId()){
                    assertEquals(coupon1.getId(),coupon2.getId());
                    assertEquals(coupon1.getHotelId(),coupon2.getHotelId());
                    assertEquals(coupon1.getDescription(),coupon2.getDescription());
                    assertEquals(coupon1.getCouponName(),coupon2.getCouponName());
                    assertEquals(coupon1.getCouponType(),coupon2.getCouponType());
                    assertEquals(coupon1.getEndTime(),coupon2.getEndTime());
                    assertEquals(coupon1.getStartTime(),coupon2.getStartTime());
                    assertEquals(coupon1.getDiscountMoney(),coupon2.getDiscountMoney(),0.0001);
                    assertEquals(coupon1.getDiscount(),coupon2.getDiscount(),0.0001);
                    assertEquals(coupon1.getBizRegion(),coupon2.getBizRegion());
                    assertEquals(coupon1.getVIPLevel(),coupon2.getVIPLevel());
                    assertEquals(coupon1.getRoomNum(),coupon2.getRoomNum());
                }
            }
        }
        //用前后记录对比验证插入前后数据的属性是否发生变化
        //检验属性变化不需要区分coupon的类型，全部一起检查即可


        for(int i=0;i<couponList.size();i++){
            couponMapper.deleteCoupon(couponList.get(i).getId());
        }
        coupons = couponMapper.selectByHotelId(hotelId);
        assertEquals(oldNum,coupons.size());
        //删除数据清理现场，验证couponMapper.deleteCoupon、couponMapper.selectByHotelId
    }



    @BeforeClass//因为没能解决被删除的序号空悬的问题，故每次测试前都重新执行一次sql，防止序号增加至过大
    public static void testInit(){
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @Test
    public void testWithCoupon(){
        testMapper(13);
    }

}