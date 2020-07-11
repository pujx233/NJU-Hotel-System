package com.example.hotel.logic.user;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserVO;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static org.junit.Assert.assertEquals;

 public class ClientLogic extends UserLogic {
    AccountService accountService;
    AdminService adminService;

    public ClientLogic(AccountMapper accountMapper, AccountService accountService, AdminService adminService){
        this.accountMapper = accountMapper;
        this.accountService = accountService;
        this.adminService = adminService;
    }
    @Override
    public UserType userType(){return UserType.Client;}
    @Override
    public void deleteById(int id){accountMapper.deleteAccountById(id);}//没有bl层方法，用data层的代替
    @Override
    public ResponseVO addUser(User user){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        ResponseVO responseVO = accountService.registerAccount(userVO);
        user.setId((Integer)responseVO.getContent());
        user.setCredit(Math.floor(user.getCredit()));//将credit向下取整，方便之后比较
        return responseVO;
    }
    @Override
    public List<User> getUsers(){return adminService.getAllClients();}
    @Override
    public User generateUser(int flag){
        User user = super.generateUser(flag);
        user.setUserType(userType());
        user.setUserName(flag%10000+"小明");
        user.setCredit(0.142+40000%(flag*flag+34)/100.0+flag%10000);//此处刻意改为小数，测试service层四舍五入取整的功能
        user.setPhoneNumber(flag%10000+"0001");
        return user;
    }
    @Override
    public void equalAfterInsert(User user1,User user2){
        super.equalAfterInsert(user1,user2);
        assertEquals(user1.getUserName(),user2.getUserName());
        assertEquals(user1.getCredit(),user2.getCredit(),0.0001);//测试service层化整的功能
        assertEquals(user1.getPhoneNumber(),user2.getPhoneNumber());
    }
}
