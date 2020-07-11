package com.example.hotel.logic.user;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.UserType;
import com.example.hotel.logic.hotel.HotelLogic;
import com.example.hotel.po.User;

import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserForm;
import java.util.List;

import static org.junit.Assert.*;

public class ManagerLogic extends UserLogic {
    AdminService adminService;
    HotelService hotelService;

    public ManagerLogic(AccountMapper accountMapper, AdminService adminService, HotelService hotelService){
        this.accountMapper = accountMapper;
        this.adminService = adminService;
        this.hotelService = hotelService;
    }
    @Override
    public UserType userType(){return UserType.HotelManager;}
    @Override
    public void deleteById(int id){
        List<HotelVO> hotelVOS = hotelService.retrieveHotels();
        int hotelId = -1;
        for(HotelVO hotelVO:hotelVOS){
            if(hotelVO.getManagerId()==id){
                hotelId = hotelVO.getId();
                break;
            }
        }
        if(hotelId==-1){
            fail();
        }
        assertTrue(adminService.removeUser(id).getSuccess());
        hotelService.removeHotel(hotelId);
        assertNull(hotelService.retrieveHotelDetails(hotelId));
    }
    @Override//此方法创建管理人员，调用addUserWitnAdminId，然后删除管理人员
    public ResponseVO addUser(User user){//含有hotel的创建，可能影响hotel的测试，慎用
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        Integer adminId = adminLogic.administrator();
        ResponseVO responseVO = addUserWithAdminId(user,adminId);
        accountMapper.deleteAccountById(adminId);
        assertNull(accountMapper.getAccountById(adminId));
        return responseVO;
    }
    //此方法在管理人员id的权限帮助下，添加酒店，借用酒店id添加酒店管理员，然后删除酒店
    public ResponseVO addUserWithAdminId(User user,int adminId){//含有hotel的创建，可能影响hotel的测试，慎用
        HotelLogic hotelLogic = new HotelLogic(adminService);
        int hotelId = hotelLogic.addHotel(hotelLogic.generateHotel(),adminId);
        ResponseVO responseVO = addUserAndBindHotel(user,hotelId);
        return responseVO;
    }
    //此方法传入user和hotelId，调用了adminService.addManager(Userform userform,Integer hotelId)，帮user添上id和userType属性
    public ResponseVO addUserAndBindHotel(User user,int hotelId){
        UserForm userForm = new UserForm();
        userForm.setEmail(user.getEmail());
        userForm.setPassword(user.getPassword());
        ResponseVO responseVO = adminService.addManager(userForm,hotelId);
        user.setId((Integer)responseVO.getContent());
        user.setUserType(userType());
        return responseVO;
    }
    @Override
    public List<User> getUsers(){return adminService.getAllManagers();}
    //    @Override
//    public List<User> insertUsers(int testNum){
//        Integer adminId = administrator();
//        List<User> userRecords = new ArrayList<>();
//        for(int i=0;i<testNum;i++){
//            User user = generateUser(i);
//            assertTrue(addUserWithAdminId(user,adminId).getSuccess());
//            userRecords.add(user);
//        }
//        accountMapper.deleteAccountById(adminId);
//        assertNull(accountMapper.getAccountById(adminId));
//        return userRecords;
//    }

}
