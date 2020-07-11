package com.example.hotel.bl.user;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.logic.user.ClientLogic;
import com.example.hotel.logic.user.ManagerLogic;
import com.example.hotel.logic.user.MarketerLogic;
import com.example.hotel.logic.user.UserLogic;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.UserForm;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class AccountServiceTest extends BaseTest {//主要是为了继承BaseTest的注释来注入依赖等等，详见该类

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
        //用插入数量验证bl层的用户插入（包括AccountService.registerAccount）

        for (User oldUser : userRecords) {
            User newUser = accountService.getUserInfo(oldUser.getId());
            userLogic.equalAfterInsert(oldUser, newUser);
        }//用前后记录对比验证accountService.getUserInfo
        // 以及各类数据插入（包括AccountService.registerAccount）效果

        for (User oldUser : userRecords) {
            UserForm userForm = new UserForm();
            userForm.setEmail(oldUser.getEmail());
            userForm.setPassword(oldUser.getPassword());
            User newUser = accountService.login(userForm);
            userLogic.equalAfterInsert(oldUser, newUser);
        }//用前后记录对比验证accountService.login（其实这就是逻辑层的getAccountByName了）
        // 以及各类数据插入（包括AccountService.registerAccount）效果

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            User newUser = userLogic.generateUser(i);
            assertTrue(accountService.updateUserInfo(oldUser.getId(),newUser.getPassword(),
                    newUser.getUserName(),newUser.getPhoneNumber()).getSuccess());
            User changedUser = accountService.getUserInfo(oldUser.getId());
            userLogic.equalAfterUpdate(newUser,changedUser);
        }//用前后记录对比验证accountService.updateUserInfo

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            double oldCredit = oldUser.getCredit();
//            System.out.println(i+" Uninitiallized Credit: "+oldCredit);
            double credit = (int)(new Random(i*i+400).nextInt())%100000.0/100+0.123;//故意制造小数来验证credite的自动向下取整
//            System.out.println(i+" Change Credit By: "+credit);
            accountService.updateUserCredit(oldUser.getId(),credit);
            User changedUser = accountService.getUserInfo(oldUser.getId());
            assertEquals(oldCredit+Math.floor(credit),changedUser.getCredit(),0.0001);
        }//用前后记录对比验证accountService.updateUserCredit

        for (User oldUser : userRecords) {
            userLogic.deleteById(oldUser.getId());
            assertNull(accountService.getUserInfo(oldUser.getId()));
        }//用前后记录对比验证bl层的用户删除（此处已与AccountService无关，只是清理现场）

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

}


