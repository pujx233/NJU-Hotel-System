package com.example.hotel.bl.admin;

import com.example.hotel.po.User;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.*;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
public interface AdminService {

    /**
     * 添加酒店并返回酒店hotelId
     * @param hotelVO
     * @throws
     */
    ResponseVO addHotel(HotelVO hotelVO, int adminId) ;

    /**
     * 删除酒店
     * @param hotelId
     * @param adminId
     */
    ResponseVO removeHotel(Integer hotelId, Integer adminId);

    /**
     * 添加酒店管理人员账号
     * @param userForm
     * @param hotelId
     * @return
     */                                   /*作业改动 by檀潮*/ //添加参数hotelId
    ResponseVO addManager(UserForm userForm, Integer hotelId);
                                         /*作业改动 */
    /**
     * 获得所有酒店管理人员信息
     * @return
     */
    List<User> getAllManagers();

    /*作业改动 by檀潮*/ //添加
    /**
     * 添加网站营销人员账号
     * @param userForm
     * @return
     */
    ResponseVO addMarketer(UserForm userForm);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 获得所有网站营销人员信息
     * @return
     */
    List<User> getAllMarketers();
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 获得所有客户信息
     * @return
     */
    List<User> getAllClients();
    /*作业改动*/

    /**
     * 更新用户个人信息
     * @param userId
     * @param password
     * @param username
     * @param phonenumber
     * @return
     */
    ResponseVO updateUserInfo(int userId, String password,String username,String phonenumber);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    ResponseVO removeUser(int userId);
}
