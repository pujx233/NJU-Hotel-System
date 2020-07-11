package com.example.hotel.bl.vip;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.logic.user.ClientLogic;
import com.example.hotel.logic.vip.VIPLogic;
import com.example.hotel.po.User;
import com.example.hotel.po.VIP;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.VIPVO;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Random;

import static org.junit.Assert.*;

public class VIPServiceTest extends BaseTest {//主要是为了继承BaseTest的注释来注入依赖等等，详见该类

    @Autowired
    AdminService adminService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    VIPService vipService;

    public void testService(int testNum){
        ClientLogic clientLogic = new ClientLogic(accountMapper,accountService,adminService);
        VIPLogic vipLogic = new VIPLogic(vipService);

        Random r = new Random(testNum);
        for(int i=0;i<testNum;i++){
            User user = clientLogic.generateUser(i);
            int userId = (Integer)clientLogic.addUser(user).getContent();
            double credit = accountService.getUserInfo(userId).getCredit();
            assertNull(vipService.getVIPInfo(user.getId()));
            //利用vipService.getVIPInfo(user.getId())检验刚注册的用户没有会员

            accountService.updateUserCredit(userId,-credit);
            int iniCredit = r.nextInt(6500)-500;
            accountService.updateUserCredit(userId,iniCredit);
            System.out.println(vipLogic.mapLevel(iniCredit));
            //为用户赋值-500到6000内随机的信用值

            VIPVO vipVO = vipLogic.generateVIPVO(userId);
            assertTrue(vipService.registerVIP(vipVO).getSuccess());//第一次注册必定成功
            assertFalse(vipService.registerVIP(vipVO).getSuccess());//注册后不能再注册
            VIP vip = vipService.getVIPInfo(userId);
            assertEquals(vipVO.getUserId(),vip.getUserId());//检验存取前后数据相同
            assertEquals(vipVO.getBirthday(),vip.getBirthday());//检验存取前后数据相同
            assertEquals(vipLogic.mapLevel(iniCredit),vipService.getVIPInfo(userId).getLevel());//检验等级随信用值初始化成功
        }
        //检验vipService.registerVIP与vipService.getVIPInfo以及vipService根据信用值初始化等级的功能

        User user = clientLogic.generateUser(testNum);
        int userId = (Integer)clientLogic.addUser(user).getContent();
        double oldCredit = accountService.getUserInfo(userId).getCredit();
        accountService.updateUserCredit(userId,-oldCredit);
        VIPVO vipVO = vipLogic.generateVIPVO(userId);
        int vipId = (Integer)vipService.registerVIP(vipVO).getContent();
        //初始化信用值为0的用户以及该用户的会员

        for(int i=0;i<testNum;i++){
            double credit = r.nextInt(6500)-500;
            VIPLevel vipLevel = vipLogic.mapLevel(credit);
//            System.out.println(vipLevel);
            vipService.setVIPLevel(vipId,vipLevel);
            assertEquals(vipLevel,vipService.getVIPInfo(userId).getLevel());
        }
        //验证vipService.setVIPLevel

        for(int i=0;i<testNum;i++){
            double credit = r.nextInt(6500)-500;
//            System.out.println(vipLogic.mapLevel(credit));
            accountService.updateUserCredit(userId,credit);
            vipService.updateVIPLevel(userId);
            assertEquals(vipLogic.mapLevel(credit),vipService.getVIPInfo(userId).getLevel());
            accountService.updateUserCredit(userId,-credit);
            assertEquals(0,accountService.getUserInfo(userId).getCredit(),0.0001);
            //为是会员的用户改变信用值使得结果为-500到6000内随机的信用值，检验会员等级是否更新成功，再把信用值清理以待下回合的测试
        }
        //验证vipService.updateUserCredit（updateUserCredit使用setVIPLevel）

        for(int i=0;i<testNum;i++){
            double credit = r.nextInt(6500)-500;
//            System.out.println(vipLogic.mapLevel(credit));
            try{
                vipService.updateClientCredit(userId,credit);
                assertEquals(vipLogic.mapLevel(credit),vipService.getVIPInfo(userId).getLevel());
                vipService.updateClientCredit(userId,-credit);
                assertEquals(vipLogic.mapLevel(0),vipService.getVIPInfo(userId).getLevel());
            }catch (Exception e){
                fail();
            }
            //为是会员的用户改变信用值使得结果为-500到6000内随机的信用值，检验会员等级是否更新成功，再把信用值清理以待下回合的测试
        }
        //验证vipService.updateClientCredit（updateClientCredit使用updateUserCredit）

    }



    @BeforeClass//因为没能解决被删除的序号空悬的问题，故每次测试前都重新执行一次sql，防止序号增加至过大
    public static void testInit(){
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }

    @After//VIP表没有删除方法，故重新执行sql文件清空现场
    public void tearDown(){
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }

    @Test
    public void testAltogether(){
        testService(23);
    }


}


