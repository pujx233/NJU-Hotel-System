package com.example.hotel.data.order;

import com.example.hotel.po.Order;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Mapper
@Repository
public interface OrderMapper {

    int addOrder(Order order);//要求返回插入的记录的id

    List<Order> getAllOrders();

    List<Order> getUserOrders(@Param("userid") int userid);

    Order getOrderById(@Param("orderid") int orderid);

    int annulOrder(@Param("orderid") int orderid);//搜索条件只有orderid

    /*作业改动 by檀潮*/ //添加
    int abnormalOrder(@Param("orderid") int orderid);//搜索条件只有orderid
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    int executeOrder(@Param("orderid") int orderid);//搜索条件只有orderid
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    int updateOrder(@Param("orderid") Integer orderId,@Param("checkOutDate") String checkOutDate,@Param("price") Double price);//搜索条件只有orderid
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加at 6/15，将状态从异常置为过期异常
    int solveAbnormalOrder(@Param("orderid") int orderid);//搜索条件只有orderid
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加at 6/27，将订单置为已评论
    int orderRAR(@Param("orderid") int orderid);//搜索条件只有orderid
    /*作业改动*/
}
