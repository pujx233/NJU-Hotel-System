package com.example.hotel.controller.marketer;

import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/api/marketer")
public class MarketerController {
    @Autowired
    MarketerService marketerService;

    @GetMapping("/retrieveAbnormalOrders")
    public ResponseVO retrieveAbnormalOrders(){
        return ResponseVO.buildSuccess(marketerService.getAbnormalOrders());
    }

    @PostMapping("/annulAbnormalOrder/{orderId}/{percent}")
    public ResponseVO annulAbnormalOrder(@PathVariable Integer orderId, @PathVariable Double percent){
        return marketerService.annulAbnormalOrder(orderId, percent);
    }

    //请先把这里改一下
    @PostMapping("/buyCredit/{email}/{credit}")
    public ResponseVO buyCredit(@PathVariable("email") String email, @PathVariable Double credit){
        return marketerService.buyCredit(email, credit);
    }
}

