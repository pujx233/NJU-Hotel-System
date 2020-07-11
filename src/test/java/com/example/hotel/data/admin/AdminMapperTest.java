package com.example.hotel.data.admin;

import com.example.hotel.data.admin.AdminMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AdminMapperTest extends BaseTest {//主要是为了继承BaseTest的注释来注入依赖等等，详见该类
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    AccountMapper accountMapper;

    //觉得写继承好麻烦就按选择写了
    public List<User> getUsersByType(UserType userType){
        switch (userType){
            case HotelManager:
                return adminMapper.getAllManagers();
            case Marketer:
                return adminMapper.getAllMarketers();
            case Client:
                return adminMapper.getAllClients();
            default:
                return null;
        }
    }
    public int addUsersByType(UserType userType,User user){
        switch (userType){
            case HotelManager:
                return adminMapper.addManager(user);
            case Marketer:
                return adminMapper.addMarketer(user);
            case Client:
                return accountMapper.createNewAccount(user);
            default:
                return 0;
        }
    }


    public void testMapper(UserType userType,int testNum){

        List<User> users = getUsersByType(userType);
        int oldNum = users.size();
        List<User> userRecords = new ArrayList<>();

        for(int i=0;i<testNum;i++){
            User user = new User();
            user.setEmail(validEmail(i,"999@qq.com"));
            user.setPassword(i%10000+"9123456");
            user.setUserType(userType);
            if(userType.equals(UserType.Client)){
                user.setUserName(i+"小明");
                user.setCredit(400%(i+7)+i);
                user.setPhoneNumber(i+"0001");
            }
            addUsersByType(userType,user);//在这个过程里记录的id被映射到user的id属性中

            //此条用来检查runsqlBySpringUtils是否正确运行，若是，则序号不会随用例执行次数增加而增长
//            System.out.println(i+" 返回id: "+user.getId());

            userRecords.add(user);
        }

        users = getUsersByType(userType);
        assertEquals(testNum+oldNum, users.size());//左Expected,右Actual
        //至此，预期插入数量与实际插入数量相等

        for(int i=0;i<userRecords.size();i++){
            User oldUser = userRecords.get(i);
            User newUser = accountMapper.getAccountById(oldUser.getId());
            assertEquals(oldUser.getId(),newUser.getId());//左Expected,右Actual
            assertEquals(oldUser.getEmail(),newUser.getEmail());//左Expected,右Actual
            assertEquals(oldUser.getPassword(),newUser.getPassword());//左Expected,右Actual
            assertEquals(oldUser.getUserType(),newUser.getUserType());//左Expected,右Actual
            if(userType.equals(UserType.Client)){
                assertEquals(oldUser.getUserName(),newUser.getUserName());//左Expected,右Actual
                assertEquals(oldUser.getCredit(),newUser.getCredit(),0.0001);//左Expected,右Actual
                assertEquals(oldUser.getPhoneNumber(),newUser.getPhoneNumber());//左Expected,右Actual
            }
            accountMapper.deleteAccountById(oldUser.getId());
            assertNull(accountMapper.getAccountById(oldUser.getId()));
        }
        //至此，预期插入数据与实际插入数据相同，且所有数据均成功按id删除

        users = getUsersByType(userType);
        assertEquals(oldNum,users.size());//左Expected,右Actual
        //至此，预期剩余数量与实际剩余数量相等
    }

    public String validEmail(int flag, String emailFrame){
        Random r = new Random(flag);
        while(true){
            String tmpEmail = flag+(Math.abs(r.nextInt())+emailFrame);
            if(null==accountMapper.getAccountByName(tmpEmail)){
                return tmpEmail;
            }
        }
    }


    @BeforeClass//因为没能解决被删除的序号空悬的问题，故每次测试前都重新执行一次sql，防止序号增加至过大
    public static void testInit(){
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @Test
    public void testWithHotelManager(){
        testMapper(UserType.HotelManager,19);
    }

    @Test
    public void testWithMarketer(){
        testMapper(UserType.Marketer,34);
    }

    @Test
    public void testWithClient(){
        testMapper(UserType.Client,109);
    }



}
