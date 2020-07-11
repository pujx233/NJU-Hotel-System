package com.example.hotel.blImpl.user;

import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.po.User;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.UserForm;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl implements AccountService {
    private static final String EMAIL_FORM_1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static final String EMAIL_FORM_2 = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public ResponseVO registerAccount(UserVO userVO) {
        try{
            if(!userVO.getEmail().matches(EMAIL_FORM_1)){
                throw new ServiceException("作为账号的邮箱格式不正确");
            }
            User possibleUser = accountMapper.getAccountByName(userVO.getEmail());
            if(possibleUser!=null){
                throw new ServiceException("账号已存在");
            }
            if(userVO.getPassword().length()<6){
                throw new ServiceException("密码长度不足6");
            }
            User user = new User();
            BeanUtils.copyProperties(userVO,user);
            user.setCredit(Math.floor(user.getCredit()));//信用值要向下舍去到整数，因为插入到数据库时似乎忽略小数部分
            accountMapper.createNewAccount(user);
            return ResponseVO.buildSuccess(user.getId());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("注册账号任务中发生错误: "+e.getMessage());
        }
    }
    //依赖data.user.AccountMapper.getAccountByName(String email)
    //依赖data.user.AccountMapper.createNewAccount(User user)

    @Override
    public User login(UserForm userForm) {
        User user = accountMapper.getAccountByName(userForm.getEmail());
        if (null == user || !user.getPassword().equals(userForm.getPassword())) {
            return null;
        }
        return user;
    }
    //依赖data.user.AccountMapper.getAccountByName(String email)

    @Override
    public User getUserInfo(int id) {
        User user = accountMapper.getAccountById(id);
        if (user == null) {
            return null;
        }
        return user;
    }
    //依赖data.user.AccountMapper.getAccountById(int id)

    @Override
    public ResponseVO updateUserInfo(int id, String password, String username, String phonenumber) {
        try {
            if(password==null||password.length()<6){
                throw new ServiceException("输入的密码不是有效密码");
            }
            accountMapper.updateAccount(id, password, username, phonenumber);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("在修改信息任务中发生错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.user.AccountMapper.updateAccount(int id, String password, String username, String phonenumber)


    @Override
    public void updateUserCredit(int id, double credit){
        //此处credit取正负代表原来信用值基础上的加减credit，void返回值代表此方法并非直接提供给操作者、而是供别的方法调用
        credit = Math.floor(credit);//最终改变的信用值向下舍去到整数，因为插入到数据库时似乎忽略小数部分
        accountMapper.updateAccountCredit(id,credit);
    }
    //依赖data.user.AccountMapper.updateAccountCredit(int id, double credit)
}
