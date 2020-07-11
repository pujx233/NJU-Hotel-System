package com.example.hotel.data.rar;

import com.example.hotel.data.hotel.HotelMapper;
import com.example.hotel.data.rar.RARMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.ServiceOfHotel;
import com.example.hotel.po.Hotel;
import com.example.hotel.po.Judgment;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RarMapperTest extends BaseTest {
    @Autowired
    RARMapper rarMapper ;
    @Autowired
    HotelMapper hotelMapper;


    public void testJudgment(int testNum){
        int hotelId = 5;//任意不违背数据库的值

        List<Judgment> judgments = rarMapper.selectRARByHotelId(hotelId);
        int oldNum = judgments.size();

        Random r = new Random();
        List<Judgment> judgmentList = new ArrayList<>();
        for(int i=0;i<testNum;i++){
            Judgment judgment =new Judgment();
            judgment.setOrderId(i);
            judgment.setCreateDate("2000-10-9");
            judgment.setComment("test"+i);
            judgment.setHotelId(hotelId);
            judgment.setRate(r.nextInt(50)/10.0);
            judgment.setUserId(i*i);

            rarMapper.insertRAR(judgment);
            judgmentList.add(judgment);
        }
        //插入judgment记录

        judgments = rarMapper.selectRARByHotelId(hotelId);
        assertEquals(oldNum+testNum,judgments.size());
        //用插入数据的前后数量差异测试rarMapper.selectRARByHotelId与rarMapper.insertRAR

        for(Judgment judgment1:judgmentList){
            boolean match = false;
            for(Judgment judgment2:judgments){
                match = true;
                if(judgment1.getId().equals(judgment2.getId())){
                    assertEquals(judgment1.getUserId(),judgment2.getUserId());
                    assertEquals(judgment1.getOrderId(),judgment2.getOrderId());
                    assertEquals(judgment1.getHotelId(),judgment2.getHotelId());
                    assertEquals(judgment1.getCreateDate(),judgment2.getCreateDate());
                    assertEquals(judgment1.getRate(),judgment2.getRate());
                    assertEquals(judgment1.getComment(),judgment2.getComment());
                }
            }
            if(!match){
                fail();
            }
        }
        //比较插入前后属性差异，验证rarMapper.insertRAR

    }

    public void testHotelRate(int testNum){
        Hotel hotel = new Hotel();
        hotel.setHotelService(String.valueOf(ServiceOfHotel.HotWater));
        hotel.setAddress("Nanjing");
        hotel.setBizRegion(BizRegion.WangFuJing);
        hotel.setDescription("test");
        hotel.setHotelName("test");
        hotel.setHotelStar(HotelStar.Four);
        hotel.setPhoneNum("233");
        hotel.setRate(5.0);
        hotelMapper.insertHotel(hotel);

        Random r = new Random();
        for(int i=0;i<testNum;i++){
            double any = r.nextInt(50)/10.0;
            rarMapper.updateHotelRate(hotel.getId(),any);
            assertEquals(any,hotelMapper.selectById(hotel.getId()).getRate(),0.0001);
        }
        //检验rarMapper.updateHotelRate

    }

    @Test
    public void testWithJudgment(){
        testJudgment(23);
    }

    @Test
    public void tsetWithHotelRate(){
        testHotelRate(23);
    }


    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }

}