package com.example.hotel.logic.user;

import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public abstract class UserLogic{
    AccountMapper accountMapper;
    public abstract UserType userType();
    public abstract void deleteById(int id);
    public abstract ResponseVO addUser(User user);//managerLogic子类此方法含有hotel的创建，可能影响hotel的测试，慎用
    public abstract List<User> getUsers();
    public User generateUser(int flag){
        int any = new Random().nextInt();
        any = any<0?-any:any;
        User user = new User();
        user.setEmail(validEmail(flag,"999@qq.com"));
        user.setPassword((any*flag*flag%10000)+"123456");//为了使密码不超过11位
        return user;
    }
    public List<User> insertUsers(int testNum){
        List<User> userRecords = new ArrayList<>();
        for(int i=0;i<testNum;i++){
            User user = generateUser(i);
            assertTrue(addUser(user).getSuccess());
            userRecords.add(user);
        }
        return userRecords;
    }
    public void equalAfterInsert(User user1,User user2){
        assertEquals(user1.getId(),user2.getId());
        assertEquals(user1.getEmail(),user2.getEmail());
        assertEquals(user1.getPassword(),user2.getPassword());
        assertEquals(user1.getUserType(),user2.getUserType());
    }
    public void equalAfterUpdate(User user1,User user2){
        assertEquals(user1.getUserName(),user2.getUserName());
        assertEquals(user1.getPassword(),user2.getPassword());
        assertEquals(user1.getPhoneNumber(),user2.getPhoneNumber());
    }
    public String validEmail(int flag, String emailFrame){
        Random r = new Random(flag);
        while(true){
            String tmpEmail = flag+(Math.abs(r.nextInt())+emailFrame);
            assertNotNull(tmpEmail);
            if(null==accountMapper.getAccountByName(tmpEmail)){
                return tmpEmail;
            }
        }
    }
}
