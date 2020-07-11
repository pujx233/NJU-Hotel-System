package com.example.hotel.bl.order;

import com.example.hotel.po.Order;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
public interface OrderService {

    /**
     * 预订酒店
     * @param orderVO
     * @return
     */
    ResponseVO addOrder(OrderVO orderVO);

    /**
     * 获得所有订单信息
     * @return
     */
    List<Order> getAllOrders();

    /**
     * 获得指定用户的所有订单信息
     * @param userid
     * @return
     */
    List<Order> getUserOrders(int userid);

    /*作业改动 by檀潮*/ //更改原annulOrder为annulBookedOrder
    /**
     * 撤销已预订订单
     * @param orderid
     * @return
     */
    ResponseVO annulBookedOrder(int orderid);
    /*作业改动*/

    /**
     * 查看酒店的所有订单
     * @param hotelId
     * @return
     */
    List<Order> getHotelOrders(Integer hotelId);

    /*作业改动 by檀潮*/ //添加
    /**
     * 撤销已预订订单
     * @param orderid
     * @return
     */
    void annulOrder(int orderid) throws ServiceException;
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 将订单状态置为异常并扣除信用值
     * @param orderid
     * @return
     */
    void abnormalOrder(int orderid) throws ServiceException;
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 执行订单并更新信用值
     * @param orderid
     * @return
     */
    ResponseVO executeOrder(int orderid);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 提前离店时修改订单的离店日期与总价
     * @param orderid
     * @return
     */
    ResponseVO leaveEarly(int orderid);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加at 6/15
    /**
     * 将异常订单置为过期异常订单并恢复房间
     * @param orderid
     * @return
     */
    ResponseVO solveAbnormalOrder(int orderid);
    /*作业改动*/
}
