package com.example.hotel.controller.rar;

import com.example.hotel.bl.rar.RARService;
import com.example.hotel.vo.JudgmentVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/rar")
public class RARController {
    @Autowired
    RARService rarService;

    @PostMapping("/rateAndReview")
    public ResponseVO rateAndReview(@RequestBody JudgmentVO judgmentVO){
        return rarService.rateAndReview(judgmentVO.getOrderId(), judgmentVO.getRate(), judgmentVO.getReview());
    }

    @GetMapping("/{hotelId}/getHotelRAR")
    public ResponseVO getHotelRAR(@PathVariable Integer hotelId){
        return ResponseVO.buildSuccess(rarService.getHotelRAR(hotelId));
    }
}
