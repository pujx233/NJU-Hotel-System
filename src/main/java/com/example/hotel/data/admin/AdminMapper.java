package com.example.hotel.data.admin;

import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Mapper
@Repository
public interface AdminMapper {

    //注意！按照useGeneratedKeys写法，这里的返回值是插入条数，而id会被映射到实体类中，即此处的user里
    int addManager(User user);//单个参数不要用@param,否则要修改xml文件在参数前加上


    List<User> getAllManagers();

    /*作业改动 by檀潮*/ //添加
    int addMarketer(User user);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    List<User> getAllMarketers();
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    List<User> getAllClients();
    /*作业改动*/
}
