package com.example.hotel.logic.user;

import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;

import java.util.List;

public class AdminLogic extends UserLogic{
    public AdminLogic(AccountMapper accountMapper){this.accountMapper = accountMapper;}

    public Integer administrator(){
        User user = new User();
        user.setEmail(validEmail(1,"123@qq.com"));
        user.setPassword("123456");
        user.setUserType(UserType.Administrator);
        accountMapper.createNewAccount(user);
        return user.getId();
    }

    @Override
    public UserType userType() {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public ResponseVO addUser(User user) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }
}
