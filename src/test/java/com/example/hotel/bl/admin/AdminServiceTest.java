package com.example.hotel.bl.admin;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.logic.hotel.HotelLogic;
import com.example.hotel.logic.user.*;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.ResponseVO;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AdminServiceTest extends BaseTest {//主要是为了继承BaseTest的注释来注入依赖等等，详见该类

    @Autowired
    AdminService adminService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    HotelService hotelService;

    private UserLogic userLogic;



    public void testService(int testNum){
        List<User> users = userLogic.getUsers();
        int oldNum = users.size();
        List<User> userRecords = userLogic.insertUsers(testNum);
        users = userLogic.getUsers();
        assertEquals(oldNum+testNum,users.size());
        //用插入数量验证bl层的用户插入（userLogic.insertUsers中已包括AdminService.addManager与AdminService.addMarketer）
        //在userLogic.getUsers()中已使用adminService.getAllManagers,adminService.getAllMarketers以及adminService.getAllClients

        for (User oldUser : userRecords) {
            User newUser = accountService.getUserInfo(oldUser.getId());
            userLogic.equalAfterInsert(oldUser, newUser);
        }//用前后记录对比验证各类数据插入（包括AdminService.addManager与AdminService.addMarketer）效果

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            User newUser = userLogic.generateUser(i);
            assertTrue(adminService.updateUserInfo(oldUser.getId(),newUser.getPassword(),
                    newUser.getUserName(),newUser.getPhoneNumber()).getSuccess());
            User changedUser = accountService.getUserInfo(oldUser.getId());
            userLogic.equalAfterUpdate(newUser,changedUser);
        }//用前后记录对比验证adminService.updateUserInfo


        for (User oldUser : userRecords) {
            userLogic.deleteById(oldUser.getId());
            assertNull(accountService.getUserInfo(oldUser.getId()));
        }///用前后记录对比验证bl层的用户删除（包括AdminService.removeUser）

        users = userLogic.getUsers();
        assertEquals(oldNum,users.size());
        //至此，预期剩余数量与实际剩余数量相等
    }



    @BeforeClass//因为没能解决被删除的序号空悬的问题，故每次测试前都重新执行一次sql，防止序号增加至过大
    public static void testInit(){
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @Test
    public void testWithHotelManager(){
        this.userLogic = new ManagerLogic(accountMapper,adminService,hotelService);
        testService(23);
    }

    @Test
    public void testWithMarketer(){
        this.userLogic = new MarketerLogic(accountMapper,adminService);
        testService(23);
    }

    @Test
    public void testWithClient(){
        this.userLogic = new ClientLogic(accountMapper,accountService,adminService);
        testService(23);
    }

    @Test//此为专门检验管理人员与酒店相关操作的测试，下面三个测试则是检验管理人员对三种人员的增删改查
    public void testWithHotel(){
        int testNum = 23;
        List<HotelVO> hotelVOS = hotelService.retrieveHotels();
        int oldNum = hotelVOS.size();

        List<HotelVO> hotelVOSRecords = new ArrayList<>();
        UserLogic[] userLogics = new UserLogic[]{new ManagerLogic(accountMapper,adminService,hotelService),
                new MarketerLogic(accountMapper,adminService),new ClientLogic(accountMapper,accountService,adminService)};
        HotelLogic hotelLogic = new HotelLogic(adminService);
        ManagerLogic managerLogic = (ManagerLogic)userLogics[0];
        AdminLogic adminLogic = new AdminLogic(accountMapper);

        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = hotelLogic.generateHotel();
            for(UserLogic userLogic:userLogics){
                User user = userLogic.generateUser(i);
                assertTrue(userLogic.addUser(user).getSuccess());
                ResponseVO responseVO = adminService.addHotel(hotelVO,user.getId());
                assertFalse(responseVO.getSuccess());

                userLogic.deleteById(user.getId());//删除manager的同时删除之前创建的hotel，不然影响之后hotel判断
                assertNull(accountMapper.getAccountById(user.getId()));
            }

            int adminId = adminLogic.administrator();
            ResponseVO responseVO = adminService.addHotel(hotelVO,adminId);
            Integer hotelId = (Integer)responseVO.getContent();
            assertNotNull(hotelService.retrieveHotelDetails(hotelId));
            assertTrue(responseVO.getSuccess());

            assertTrue(adminService.removeHotel(hotelId,adminId).getSuccess());
            assertNull(hotelService.retrieveHotelDetails(hotelId));
            accountMapper.deleteAccountById(adminId);
            assertNull(accountMapper.getAccountById(adminId));
        }
        //验证管理员以外的账号都不能新建酒店,而管理员可以新建与移除酒店（即验证了AdminService.addHotel和AdminService.removeHotel）

        int adminId = adminLogic.administrator();
        //新建管理人员，获得adminId

        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = hotelLogic.generateHotel();
            hotelVO.setId(hotelLogic.addHotel(hotelVO,adminId));
            hotelVOSRecords.add(hotelVO);
        }//用hotelLogic.addHotel调用AdminService.addHotel完成一系列酒店的添加

        hotelVOS = hotelService.retrieveHotels();
        assertEquals(oldNum+testNum,hotelVOS.size());
        //用插入数量验证AdminService.addHotel

        for(int i=0;i<testNum;i++){
            HotelVO oldHotelVO = hotelVOSRecords.get(i);
            HotelVO newHotelVO = hotelService.retrieveHotelDetails(oldHotelVO.getId());
            hotelLogic.assertHotel(oldHotelVO,newHotelVO);
        }//用前后记录对比验证AdminService.addHotel

        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = hotelVOSRecords.get(i);
            Integer hotelId = hotelVO.getId();

            //为酒店添加酒店工作人员1，检验其成功
            User user1 = managerLogic.generateUser(i);
            ResponseVO responseVO = managerLogic.addUserAndBindHotel(user1,hotelId);
            hotelVO = hotelService.retrieveHotelDetails(hotelId);
            assertEquals(user1.getId(),hotelVO.getManagerId());
            assertTrue(responseVO.getSuccess());

            //为酒店添加酒店工作人员2，检验其失败
            User user2 = managerLogic.generateUser(i);
            responseVO = managerLogic.addUserAndBindHotel(user2,hotelId);
            hotelVO = hotelService.retrieveHotelDetails(hotelId);
            assertEquals(user1.getId(),hotelVO.getManagerId());
            assertFalse(responseVO.getSuccess());

            //删除酒店工作人员1,检验酒店是否与之解除绑定
            assertTrue(adminService.removeUser(user1.getId()).getSuccess());
            assertNull(accountMapper.getAccountById(user1.getId()));
            hotelVO = hotelService.retrieveHotelDetails(hotelId);
            assertNull(hotelVO.getManagerId());

            //为酒店添加酒店工作人员2，检验其成功
            responseVO = managerLogic.addUserAndBindHotel(user2,hotelId);
            hotelVO = hotelService.retrieveHotelDetails(hotelId);
            assertEquals(user2.getId(),hotelVO.getManagerId());
            assertTrue(responseVO.getSuccess());
        }
        //验证AdminService.addManager检测hotelId对应酒店是否已有酒店管理人员的功能（若有则添加失败）
        //验证AdminService.removeUser在删除HotelManager时解除可能的酒店绑定的功能（若检测到则将其managerId置为null）

        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = hotelVOSRecords.get(i);
            ResponseVO responseVO = adminService.removeHotel(hotelVO.getId(),adminId);
            assertNull(hotelService.retrieveHotelDetails(hotelVO.getId()));
            assertTrue(responseVO.getSuccess());
        }//为了清理现场完成酒店的删除

        hotelVOS = hotelService.retrieveHotels();
        assertEquals(oldNum,hotelVOS.size());
        //至此，预期剩余数量与实际剩余数量相等

        accountMapper.deleteAccountById(adminId);
        assertNull(accountMapper.getAccountById(adminId));
        //为了清理现场完成管理人员的删除
    }
}






