package com.example.hotel.logic.vip;

import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.VIP;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.VIPVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

public class VIPLogic {
    VIPService vipService;
    public VIPLogic(VIPService vipService){
        this.vipService = vipService;
    }
    public VIPVO generateVIPVO(int userId){
        DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return generateVIPVO(userId,df_toSecond.format(LocalDateTime.now()));
    }
    public VIPVO generateVIPVO(int userId,String dateForm){
        VIPVO vipVO = new VIPVO();
        vipVO.setUserId(userId);
        DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        vipVO.setBirthday(LocalDateTime.parse(dateForm,df_toSecond));
        return vipVO;
    }
    public VIPLevel mapLevel(double credit){
        VIPLevel newLevel = null;
        if(credit<600){
            newLevel = VIPLevel.Hamon;
        }else if(credit>=600&&credit<1500){
            newLevel = VIPLevel.HermitPurple;
        }else if(credit>=1500&&credit<3000){
            newLevel = VIPLevel.StarPlatinum;
        }else if(credit>=3000&&credit<5000){
            newLevel = VIPLevel.CrazyDiamond;
        }else {
            newLevel = VIPLevel.GoldExperience;
        }
        return newLevel;
    }
    public void assertVIP(VIP vip1,VIP vip2){
        assertEquals(vip1.getId(),vip2.getId());
        assertEquals(vip1.getUserId(),vip2.getUserId());
        assertEquals(vip1.getBirthday(),vip2.getBirthday());
        assertEquals(vip1.getLevel(),vip2.getLevel());
        assertEquals(vip1.getPoints(),vip2.getPoints(),0.001);
    }
}
