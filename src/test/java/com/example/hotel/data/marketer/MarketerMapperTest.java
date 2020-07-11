package com.example.hotel.data.marketer;

import com.example.hotel.data.marketer.MarketerMapper;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.enums.OrderState;

import com.example.hotel.enums.RoomType;
import com.example.hotel.po.Order;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class MarketerMapperTest extends BaseTest{
    @Autowired
    MarketerMapper marketerMapper;
    @Autowired
    OrderMapper orderMapper;

    @Test
    public void getAbnormalOrders(){

        //通过marketerService.getAbnormalOrders()与orderService.getAllOrder获取异常订单
        //比较获取的异常订单集合，测试marketerService.getAbnormalOrders()的正确性
        //（其中orderService.getAllOrder已在orderServiceTest中测试过）
        List<Order> orders = marketerMapper.getAllAbnormalOrders();
        List<Order> orderList = orderMapper.getAllOrders();
        List<Order> abnormalOrders = new ArrayList<>();
        for(Order order:orderList){
            if(order.getOrderState().equals(OrderState.Abnormal.toString())){
                abnormalOrders.add(order);
            }
        }
        assertEquals(abnormalOrders.size(),orders.size());
        //验证marketerMapper.getAllAbnormalOrders取出的异常订单的数量的正确性

        for(Order order1: orders){
            boolean match = false;
            for(Order order2: abnormalOrders){

                //为相匹配的订单比较各个属性，判断是否相同
                if(order1.getId().equals(order2.getId())){
                    match = true;
                    assertEquals(order1.getClientName(),order2.getClientName());
                    assertEquals(order1.getClientEmail(),order2.getClientEmail());
                    assertEquals(order1.getHotelId(),order2.getHotelId());
                    assertEquals(order1.getHotelName(),order2.getHotelName());
                    assertEquals(order1.getCreateDate(),order2.getCreateDate());
                    assertEquals(order1.getCheckInDate(),order2.getCheckInDate());
                    assertEquals(order1.getCheckOutDate(),order2.getCheckOutDate());
                    assertEquals(order1.getPhoneNumber(),order2.getPhoneNumber());
                    assertEquals(order1.getPeopleNum(),order2.getPeopleNum());
                    assertEquals(order1.isHaveChild(),order2.isHaveChild());
                    assertEquals(order1.getPrice(),order2.getPrice(),0.0001);
                    assertEquals(order1.getRoomNum(),order2.getRoomNum());
                    assertEquals(order1.getRoomType(),order2.getRoomType());
                    assertEquals(order1.getOrderState(),order2.getOrderState());
                    assertEquals(order1.isRar(),order2.isRar());
                }
            }
            if(!match){
                fail();//若不能相匹配，说明两个异常订单集合不一样
            }
        }
        //通过存取前后差异比较验证marketerService.getAbnormalOrders取出的异常订单的正确性


    }

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @Before
    public void init(){
        int testNum = 50;

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

        //随机生成多种订单插入数据库中
        Random random = new Random();
        for(int i=0;i<testNum;i++){
            int any = Math.abs(random.nextInt(5))%5;
            switch (any){
                case 0:
                    tmpOrder.setOrderState(OrderState.Booked.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 1:
                    tmpOrder.setOrderState(OrderState.Executed.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 2:
                    tmpOrder.setOrderState(OrderState.Annulled.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 3:
                    tmpOrder.setOrderState(OrderState.Abnormal.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 4:
                    tmpOrder.setOrderState(OrderState.OverdueAbnormal.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;

            }
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }
}
