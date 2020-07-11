package com.example.hotel.data.vip;

import com.example.hotel.data.vip.VIPMapper;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.VIP;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class VIPMapperTest extends BaseTest   {
    @Autowired
    VIPMapper vipMapper;

    @Test
    public void testVIP() {
        int userId = 1;
        while (vipMapper.getVIPByUserId(userId) != null) {
            userId++;
        }

        List<VIP> vips = new ArrayList<>();
        VIP vip = new VIP();
        vip.setLevel(VIPLevel.Hamon);
        DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        vip.setBirthday(LocalDateTime.parse(df_toSecond.format(LocalDateTime.now()), df_toSecond));
        vip.setUserId(userId);
        vip.setPoints(100.12);//credit在数据库自动取整
        vipMapper.createNewVIP(vip);

        VIP newVIP = vipMapper.getVIPByUserId(userId);
        assertNotNull(newVIP);

        assertEquals(vip.getId(),newVIP.getId());
        assertEquals(vip.getUserId(),newVIP.getUserId());
        assertEquals(vip.getBirthday(),newVIP.getBirthday());
        assertEquals(vip.getLevel(),newVIP.getLevel());
        assertEquals((int)vip.getPoints(),newVIP.getPoints(),0.001);//credit在数据库自动取整
        //通过存取前后属性差异测试vipMapper.createNewVIP(vip)、vipMapper.getVIPByUserId


        vipMapper.setVIPLevel(vip.getId(),VIPLevel.CrazyDiamond);
        assertEquals(VIPLevel.CrazyDiamond,vipMapper.getVIPByUserId(userId).getLevel());
        vipMapper.setVIPLevel(vip.getId(),VIPLevel.StarPlatinum);
        assertEquals(VIPLevel.StarPlatinum,vipMapper.getVIPByUserId(userId).getLevel());
        vipMapper.setVIPLevel(vip.getId(),VIPLevel.GoldExperience);
        assertEquals(VIPLevel.GoldExperience,vipMapper.getVIPByUserId(userId).getLevel());
        vipMapper.setVIPLevel(vip.getId(),VIPLevel.Hamon);
        assertEquals(VIPLevel.Hamon,vipMapper.getVIPByUserId(userId).getLevel());
        vipMapper.setVIPLevel(vip.getId(),VIPLevel.HermitPurple);
        assertEquals(VIPLevel.HermitPurple,vipMapper.getVIPByUserId(userId).getLevel());
        //检验vipMapper.setVIPLevel
    }


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


}