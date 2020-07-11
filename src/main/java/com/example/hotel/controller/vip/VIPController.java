package com.example.hotel.controller.vip;

import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.po.User;
import com.example.hotel.po.VIP;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.VIPVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/vip")
public class VIPController {
    private final static String VIP_NOT_EXIST = "用户不是会员";

    @Autowired
    VIPService vipService;

    @PostMapping("/registerVIP")
    public ResponseVO registerVIP(@RequestBody VIPVO vipVO){
        return vipService.registerVIP(vipVO);
    }

    //由于文档中未提及让用户查看自己的会员信息的部分，故暂不提供getVIPInfo(int userId)方法
    @PostMapping("/{userId}/getVIPInfo")
    public ResponseVO getVIPInfo(@PathVariable int userId) {
        VIP vip = vipService.getVIPInfo(userId);
        if(vip==null){
            return ResponseVO.buildFailure(VIP_NOT_EXIST);
        }
        return ResponseVO.buildSuccess(vip);
    }

}
