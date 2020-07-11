package com.example.hotel.controller.order;

import com.example.hotel.bl.order.OrderService;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: chenyizong
 * @Date: 2020-02-29
 */


@RestController()
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/addOrder")
    public ResponseVO reserveHotel(@RequestBody OrderVO orderVO){
        return orderService.addOrder(orderVO);
    }

    @GetMapping("/getAllOrders")
    public ResponseVO retrieveAllOrders(){
        return ResponseVO.buildSuccess(orderService.getAllOrders());
    }

    @GetMapping("/{userid}/getUserOrders")
    public  ResponseVO retrieveUserOrders(@PathVariable int userid){
        return ResponseVO.buildSuccess(orderService.getUserOrders(userid));
    }

    @PostMapping("/{orderid}/annulBookedOrder")//前端名字最好也改动
    public ResponseVO annulBookedOrder(@PathVariable int orderid){
        return orderService.annulBookedOrder(orderid);
    }

    @GetMapping("/{hotelId}/getHotelOrders")
    public ResponseVO retrieveHotelOrders(@PathVariable Integer hotelId) {
        return ResponseVO.buildSuccess(orderService.getHotelOrders(hotelId));
    }

    /*作业改动 by檀潮*/ //添加，将异常订单置为过期异常并恢复房间
    @PostMapping("/{orderid}/solveAbnormalOrder")
    public ResponseVO solveAbnormalOrder(@PathVariable int orderid){
        return orderService.solveAbnormalOrder(orderid);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加，执行入住（未执行->已执行）或补入住（异常->已执行）的订单，更新房间与信用值
    @PostMapping("/{orderid}/executeOrder")
    public ResponseVO executeOrder(@PathVariable int orderid){
        return orderService.executeOrder(orderid);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加，将订单离店日期置为当前日期并修改订单总价
    @PostMapping("/{orderid}/leaveEarly")
    public ResponseVO leaveEarly(@PathVariable int orderid){
        return orderService.leaveEarly(orderid);
    }
    /*作业改动*/


}
