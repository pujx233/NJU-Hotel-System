package com.example.hotel.logic.user;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserForm;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class MarketerLogic extends UserLogic {
    AdminService adminService;

    public MarketerLogic(AccountMapper accountMapper, AdminService adminService){
        this.accountMapper = accountMapper;
        this.adminService = adminService;
    }
    @Override
    public UserType userType(){return UserType.Marketer;}
    @Override
    public void deleteById(int id){assertTrue(adminService.removeUser(id).getSuccess());}
    @Override
    public ResponseVO addUser(User user){
        UserForm userForm = new UserForm();
        userForm.setEmail(user.getEmail());
        userForm.setPassword(user.getPassword());
        ResponseVO responseVO = adminService.addMarketer(userForm);
        user.setId((Integer)responseVO.getContent());
        user.setUserType(userType());
        return responseVO;
    }
    @Override
    public List<User> getUsers(){return adminService.getAllMarketers();}
}
