package com.example.hotel.data.user;

import com.example.hotel.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface AccountMapper {

    /**
     * 创建一个新的账号
     *
     * @return
     */
     int createNewAccount(User user);

    /**
     * 根据用户名查找账号
     * @param email
     * @return
     */
     User getAccountByName(@Param("email") String email);

     User getAccountById(@Param("id") int id);

    /**
     * 更新用户信息
     * @param id
     * @param password
     * @param username
     * @param phonenumber
     * @return
     */
     int updateAccount(@Param("id") int id, @Param("password") String password,@Param("userName") String username, @Param("phoneNumber") String phonenumber);

    /*作业改动 by檀潮*/ //添加
    /**
     * 更新用户信用值
     * @param id
     * @param varyCredit
     * @return
     */
     int updateAccountCredit(@Param("id")int id, @Param("varyCredit")Double varyCredit);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 删除用户
     * @param id
     * @return
     */
    int deleteAccountById(@Param("id") int id);//目前还无法做到序号重新排序，删除掉的序号会空着不用
    /*作业改动*/

}
