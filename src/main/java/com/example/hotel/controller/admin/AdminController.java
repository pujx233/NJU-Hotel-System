package com.example.hotel.controller.admin;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.blImpl.admin.AdminServiceImpl;
import com.example.hotel.po.User;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserForm;
import com.example.hotel.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@RestController()
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/{adminId}/addHotel")     //前端传递的数据包需要扩展hotelService（设施服务）属性，以及对ResponseVO的处理可能要调整
    public ResponseVO createHotel(@RequestBody HotelVO hotelVO, @PathVariable int adminId){//将throws ServiceException去掉
        return adminService.addHotel(hotelVO,adminId);
    }

    @PostMapping("/{hotelId}/{adminId}/removeHotel")
    public ResponseVO removeHotel(@PathVariable int hotelId,@PathVariable int adminId){
        return adminService.removeHotel(hotelId,adminId);
    }

    @PostMapping("/{hotelId}/addManager")
    public ResponseVO addManager(@RequestBody UserForm userForm,
                                 @PathVariable Integer hotelId){
        return adminService.addManager(userForm,hotelId);
    }

    @GetMapping("/getAllManagers")
    public ResponseVO getAllManagers(){
        return ResponseVO.buildSuccess(adminService.getAllManagers());
    }

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/addMarketer")
    public ResponseVO addMarketer(@RequestBody UserForm userForm) {
        return adminService.addMarketer(userForm);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @GetMapping("/getAllMarketers")
    public ResponseVO getAllMarketers() {
        return ResponseVO.buildSuccess(adminService.getAllMarketers());
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @GetMapping("/getAllClients")
    public ResponseVO getAllClients() {
        return ResponseVO.buildSuccess(adminService.getAllClients());
    }
    /*作业改动*/


    /*作业改动 by檀潮*/ //添加
    @PostMapping("/{userId}/updateUserInfo")
    public ResponseVO updateInfo(@RequestBody UserInfoVO userInfoVO,
                                 @PathVariable Integer userId){
        return adminService.updateUserInfo(userId,userInfoVO.getPassword(),userInfoVO.getUserName(),userInfoVO.getPhoneNumber());
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/{userId}/removeUser")
    public ResponseVO removeUser(@PathVariable Integer userId){
        return adminService.removeUser(userId);
    }
    /*作业改动*/
}
