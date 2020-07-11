package com.example.hotel.bl.rar;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.rar.RARService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.rar.RARMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.OrderState;
import com.example.hotel.enums.RoomType;
import com.example.hotel.logic.hotel.HotelLogic;
import com.example.hotel.logic.user.AdminLogic;
import com.example.hotel.po.Judgment;
import com.example.hotel.po.Order;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class RARServiceTest extends BaseTest {
    @Autowired
    AdminService adminService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    HotelService hotelService;
    @Autowired
    RARService rarService;
    @Autowired
    RARMapper rarMapper;
    @Autowired
    OrderMapper orderMapper;

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

    @Test
    public void rateAndReview() {
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();

        HotelLogic hotelLogic = new HotelLogic(adminService);
        int hotelId = hotelLogic.hotel(adminId);

        int defaultUser = 100;

        assertEquals(5.0,hotelService.retrieveHotelDetails(hotelId).getRate(),0.0001);
        //初始评分为5.0

        Order tmpOrder = new Order();
        tmpOrder.setUserId(1);
        tmpOrder.setClientName("1");
        tmpOrder.setClientEmail("1@qq.com");
        tmpOrder.setPeopleNum(3);
        tmpOrder.setPhoneNumber("10086");
        tmpOrder.setHotelId(hotelId);
        tmpOrder.setHotelName("1");
        tmpOrder.setRoomType(RoomType.BigBed.toString());
        tmpOrder.setRoomNum(3);
        tmpOrder.setPrice(1.0);
        tmpOrder.setCheckInDate("2000-01-01");
        tmpOrder.setCheckOutDate("2000-02-01");
        tmpOrder.setCreateDate("2000-01-01");
        tmpOrder.setHaveChild(false);
        tmpOrder.setRar(false);
        tmpOrder.setOrderState(OrderState.Executed.toString());

        Random r = new Random();
        double any = r.nextInt(50)/10.0;
        //原本的订单插入后可以被评论
        orderMapper.addOrder(tmpOrder);
        assertTrue(rarService.rateAndReview(tmpOrder.getId(),any,"1").getSuccess());

        //只能对已执行的订单评论
        tmpOrder.setOrderState(OrderState.Booked.toString());
        orderMapper.addOrder(tmpOrder);
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),2,"1").getSuccess());

        tmpOrder.setOrderState(OrderState.Annulled.toString());
        orderMapper.addOrder(tmpOrder);
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),2,"1").getSuccess());

        tmpOrder.setOrderState(OrderState.Abnormal.toString());
        orderMapper.addOrder(tmpOrder);
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),2,"1").getSuccess());

        tmpOrder.setOrderState(OrderState.OverdueAbnormal.toString());
        orderMapper.addOrder(tmpOrder);
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),2,"1").getSuccess());

        tmpOrder.setOrderState(OrderState.Executed.toString());


        //只能对未被评论过的订单评论
        tmpOrder.setRar(true);
        orderMapper.addOrder(tmpOrder);
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),2,"1").getSuccess());

        tmpOrder.setRar(false);
        orderMapper.addOrder(tmpOrder);


        //评分只能在区间[0,5]
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),-0.1,"1").getSuccess());
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),-5.1,"1").getSuccess());

        //0和5的评分值有效
        assertTrue(rarService.rateAndReview(tmpOrder.getId(),0,"1").getSuccess());
        orderMapper.addOrder(tmpOrder);
        assertTrue(rarService.rateAndReview(tmpOrder.getId(),5,"1").getSuccess());
        orderMapper.addOrder(tmpOrder);


        //只能在离店后评论
        tmpOrder.setCheckOutDate("2060-02-01");
        orderMapper.addOrder(tmpOrder);
        assertFalse(rarService.rateAndReview(tmpOrder.getId(),2,"1").getSuccess());
        tmpOrder.setCheckOutDate("2000-02-01");
        orderMapper.addOrder(tmpOrder);

        List<Judgment> judgmentList = rarService.getHotelRAR(hotelId);
        int oldNum = judgmentList.size();
        int testNum = 100;
        int addNum = 0;
        for(int i=0;i<testNum;i++){

            any = (r.nextInt(100)-25)/10.0;
            if(any>=0&&any<=5){
                assertTrue(rarService.rateAndReview(tmpOrder.getId(),any,"1").getSuccess());
                assertTrue(orderMapper.getOrderById(tmpOrder.getId()).isRar());
                orderMapper.addOrder(tmpOrder);
                ++addNum;

            }else {
                assertFalse(rarService.rateAndReview(tmpOrder.getId(),any,"1").getSuccess());
                assertFalse(orderMapper.getOrderById(tmpOrder.getId()).isRar());
            }

            List<Judgment> judgments = rarService.getHotelRAR(hotelId);
            double rate = 0;
            for(int j=0;j<judgments.size();j++){
                rate += judgments.get(j).getRate();
            }

            //默认初始有100个打5分的人，避免评分刚开始波动过大，影响外界对酒店的判断
            rate = (rate+5*defaultUser)/(judgments.size()+defaultUser);

            //评分机制为酒店评分取所有用户的评分与100个默认用户的满分评分的平均的一位小数近似
            assertEquals((int)(rate*10+0.5)/10.0,hotelService.retrieveHotelDetails(hotelId).getRate(),0.0001);
        }
        assertEquals(oldNum+addNum,rarService.getHotelRAR(hotelId).size());
        //通过比对插入评论的前后评论记录的数量来测试rarService.getHotelRAR
        //每次评论后，order的rar属性被置为true，表示已评论
        //通过测试每次评论后酒店的评分验证rarService.rateAndReview对于改变评分的行为符合预期


    }

    @Test
    public void calHotelRate() {
        int testNum = 50;

        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();

        HotelLogic hotelLogic = new HotelLogic(adminService);
        int hotelId = hotelLogic.hotel(adminId);

        Order tmpOrder = new Order();
        tmpOrder.setUserId(1);
        tmpOrder.setClientName("1");
        tmpOrder.setClientEmail("1@qq.com");
        tmpOrder.setPeopleNum(3);
        tmpOrder.setPhoneNumber("10086");
        tmpOrder.setHotelId(1);
        tmpOrder.setHotelName("1");
        tmpOrder.setRoomType(RoomType.BigBed.toString());
        tmpOrder.setRoomNum(3);
        tmpOrder.setPrice(1.0);
        tmpOrder.setCheckInDate("2060-01-01");
        tmpOrder.setCheckOutDate("2060-02-01");
        tmpOrder.setCreateDate("2000-01-01");
        tmpOrder.setHaveChild(false);
        tmpOrder.setRar(false);
        tmpOrder.setOrderState(OrderState.Executed.toString());

        assertEquals(5.0,hotelService.retrieveHotelDetails(hotelId).getRate(),0.0001);
        //初始评分为5.0

        int defaultUser = 100;
        Random r = new Random();
        double any = -1;


        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Judgment judgment = new Judgment();
        judgment.setHotelId(hotelId);
        judgment.setUserId(tmpOrder.getUserId());
        judgment.setRate(any);
        judgment.setComment("1");
        judgment.setCreateDate(df.format(LocalDateTime.now()));
        for(int i=0;i<testNum;i++){
            orderMapper.addOrder(tmpOrder);
            judgment.setOrderId(tmpOrder.getId());
            rarMapper.insertRAR(judgment);
            rarService.calHotelRate(hotelId);
            any = r.nextInt(50)/10.0;

            List<Judgment> judgments = rarService.getHotelRAR(hotelId);
            double rate = 0;
            for(int j=0;j<judgments.size();j++){
                rate += judgments.get(j).getRate();
            }

            //默认初始有100个打5分的人，避免评分刚开始波动过大，影响外界对酒店的判断
            rate = (rate+5*defaultUser)/(judgments.size()+defaultUser);
            assertEquals((int)(rate*10+0.5)/10.0,hotelService.retrieveHotelDetails(hotelId).getRate(),0.0001);
        }

    }

    @Test
    public void getHotelRAR() {
        int testNum = 50;

        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();

        HotelLogic hotelLogic = new HotelLogic(adminService);
        int hotelId = hotelLogic.hotel(adminId);

        List<Judgment> judgments = rarService.getHotelRAR(hotelId);
        int oldNum = judgments.size();
        List<Judgment> judgmentList = new ArrayList<>();

        for(int i=0;i<testNum;i++){
            Judgment judgment = new Judgment();
            judgment.setCreateDate("2000-01-01");
            judgment.setRate(2.0);
            judgment.setRate(i%5+0.0);
            judgment.setOrderId(i*i);
            judgment.setHotelId(hotelId);
            judgment.setUserId((i*i+100)%47);
            rarMapper.insertRAR(judgment);
            judgmentList.add(judgment);
        }

        judgments = rarService.getHotelRAR(hotelId);

        assertEquals(oldNum+judgmentList.size(),judgments.size());
        //对比数量来验证rarService.getHotelRAR

        for(Judgment judgment1:judgmentList){
            boolean match = false;
            for(Judgment judgment2:judgments){
                if(judgment1.getId().equals(judgment2.getId())){
                    match = true;
                    assertEquals(judgment1.getRate(),judgment2.getRate());
                    assertEquals(judgment1.getComment(),judgment2.getComment());
                    assertEquals(judgment1.getCreateDate(),judgment2.getCreateDate());
                    assertEquals(judgment1.getHotelId(),judgment2.getHotelId());
                    assertEquals(judgment1.getOrderId(),judgment2.getOrderId());
                    assertEquals(judgment1.getUserId(),judgment2.getUserId());


                }
            }
            if(!match){
                fail();
            }
        }
        //插入之前和插入之后的评论集合必定可以一一对应，检验实体类属性是否在数据库存取过程中变化
    }
}
