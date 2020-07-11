package com.example.hotel.data.order;


import com.example.hotel.data.hotel.RoomMapper;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.enums.OrderState;
import com.example.hotel.enums.RoomType;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.Order;
import org.junit.Test;
import com.example.hotel.util.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.junit.Assert.*;

public class OrderMapperTest extends BaseTest{
    @Autowired
    OrderMapper orderMapper ;
    @Autowired
    RoomMapper roomMapper;


    @Test
    public void testOrder(){
        int hotelId = 5;//任意不违背数据库的hotelId
        int userId = 3;//任意不违背数据库的hotelId

        HotelRoom hotelRoom = new HotelRoom();
        RoomType roomType = RoomType.Family;
        hotelRoom.setRoomType(roomType);
        hotelRoom.setHotelId(hotelId);
        hotelRoom.setCurNum(20);
        hotelRoom.setTotal(20);
        hotelRoom.setPrice(200);
        roomMapper.insertRoom(hotelRoom);
        //为酒店插入房间


        List<Order> orderList = orderMapper.getUserOrders(userId);
        int oldUserOrders = orderList.size();
        orderList = orderMapper.getAllOrders();
        int oldOrderNum = orderList.size();
        //记录初始订单数


        Order order = new Order();
        order.setHotelId(hotelId);
        order.setHotelName("1");
        order.setClientName("nju");
        order.setClientEmail("111@qq.com");
        order.setPhoneNumber("11111");
        order.setUserId(userId);
        order.setCheckInDate("2000-02-01");
        order.setCheckOutDate("2060-02-04");
        order.setCreateDate("2000-02-01");
        order.setPrice(11.0);
        order.setRoomNum(3);
        order.setRoomType(roomType.toString());
        order.setPeopleNum(3);
        order.setHaveChild(false);
        order.setRar(false);
        order.setOrderState(OrderState.Booked.toString());
        order.setPrice(18.0);
        //初始化order实体类


        //添加订单
        orderMapper.addOrder(order);
        orderList = orderMapper.getAllOrders();
        assertEquals(oldOrderNum+1,orderList.size());
        //用orderMapper.getAllOrders检查订单总数前后数量变化1
        orderList = orderMapper.getUserOrders(userId);
        assertEquals(oldUserOrders+1,orderList.size());
        //用orderMapper.getUserOrders检查用户订单总数前后数量变化1

        Order order1 = orderMapper.getOrderById(order.getId());
        assertEquals(order.getClientName(),order1.getClientName());
        assertEquals(order.getClientEmail(),order1.getClientEmail());
        assertEquals(order.getUserId(),order1.getUserId());
        assertEquals((Integer)hotelId,order1.getHotelId());
        assertEquals(order.getHotelName(),order1.getHotelName());
        assertEquals(order.getCheckInDate(),order1.getCheckInDate());
        assertEquals(order.getCheckOutDate(),order1.getCheckOutDate());
        assertEquals(order.getPhoneNumber(),order1.getPhoneNumber());
        assertEquals(order.getPeopleNum(),order1.getPeopleNum());
        assertEquals(order.isHaveChild(),order1.isHaveChild());
        assertEquals(order.getPrice(),order1.getPrice(),0.0001);
        assertEquals(order.getRoomNum(),order1.getRoomNum());
        assertEquals(order.getRoomType(),order1.getRoomType());
        assertEquals(order.getOrderState(),order1.getOrderState());
        assertEquals(order.isRar(),order1.isRar());
        //检查插入后与插入前属性相同



        //撤销订单
        orderMapper.annulOrder(order.getId());
        assertEquals(OrderState.Annulled.toString(),orderMapper.getOrderById(order.getId()).getOrderState());
        //测试状态是否被置为“已撤销”


        //酒店工作人员执行订单
        orderMapper.executeOrder(order.getId());
        assertEquals(OrderState.Executed.toString(),orderMapper.getOrderById(order.getId()).getOrderState());
        //测试状态是否被置为“已执行


        //提前离店时更新订单离店时间与金额
        String outTime = "2020-10-01";
        double price = 36.03;
        orderMapper.updateOrder(order.getId(),outTime,price);
        assertEquals(outTime,orderMapper.getOrderById(order.getId()).getCheckOutDate());
        assertEquals(price,orderMapper.getOrderById(order.getId()).getPrice(),0.0001);


        //把订单置为异常
        orderMapper.abnormalOrder(order.getId());
        assertEquals(OrderState.Abnormal.toString(),orderMapper.getOrderById(order.getId()).getOrderState());
        //测试状态是否被置为“异常”



        //将异常订单状态置为过期异常
        orderMapper.solveAbnormalOrder(order.getId());
        assertEquals(OrderState.OverdueAbnormal.toString(),orderMapper.getOrderById(order.getId()).getOrderState());
        //测试状态是否被置为“过期异常”


        //将订单置为已评论
        assertFalse(orderMapper.getOrderById(order.getId()).isRar());
        orderMapper.orderRAR(order.getId());
        assertTrue(orderMapper.getOrderById(order.getId()).isRar());



    }
}
