package com.example.hotel.blImpl.admin;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.admin.AdminMapper;
import com.example.hotel.data.hotel.HotelMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.ServiceOfHotel;
import com.example.hotel.enums.UserType;
import com.example.hotel.po.Hotel;
import com.example.hotel.po.User;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Service
public class AdminServiceImpl implements AdminService {
    private static final String EMAIL_FORM_1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static final String EMAIL_FORM_2 = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private final static String ACCOUNT_EXIST = "账号已存在";

    @Autowired
    AdminMapper adminMapper;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    HotelService hotelService;


    @Override//暂时不提供hotel名字不重复功能，版权问题让商家们线下争吵
    public ResponseVO addHotel(HotelVO hotelVO,int adminId) {
        try{
            User manager = accountMapper.getAccountById(adminId);
            if(manager == null || !manager.getUserType().equals(UserType.Administrator)){
                throw new ServiceException("管理员不存在或者无权限！");
            }
            return ResponseVO.buildSuccess(hotelService.addHotel(hotelVO));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("在创建酒店任务中发生错误: "+e.getMessage());
        }
    }
    //依赖data.user.AccountMapper.getAccountById(int id)
    //依赖bl.hotel.HotelService.addHotel(HotelVO hotelVO)

    @Override
    public ResponseVO removeHotel(Integer hotelId, Integer adminId) {
        try{
            User manager = accountMapper.getAccountById(adminId);
            if(manager == null || !manager.getUserType().equals(UserType.Administrator)){
                throw new ServiceException("管理员不存在或者无权限！创建酒店失败！");
            }
            Integer managerId = hotelService.retrieveHotelDetails(hotelId).getManagerId();
            if(managerId!=null){
                accountMapper.deleteAccountById(managerId);
            }
            hotelService.removeHotel(hotelId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("在管理人员删除酒店任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.user.AccountMapper.getAccountById(int id)
    //依赖bl.hotel.HotelService.removeHotel(Integer hotelId)
    //依赖bl.hotel.HotelService.retrieveHotelDetails(Integer hotelId)


    @Override
    public ResponseVO addManager(UserForm userForm, Integer hotelId) {
        ResponseVO responseVO = null;
        try{
//            User admin = accountMapper.getAccountById(adminId);
//            if(admin==null||!admin.getUserType().equals(UserType.Administrator)){
//                throw new ServiceException("管理员不存在或者无权限！");
//            }
            if(!userForm.getEmail().matches(EMAIL_FORM_1)){
                throw new ServiceException("作为账号的邮箱格式不正确");
            }
            User possibleUser = accountMapper.getAccountByName(userForm.getEmail());
            if(possibleUser!=null){
                throw new ServiceException(ACCOUNT_EXIST);
            }
            if(userForm.getPassword().length()<6){
                throw new ServiceException("密码长度不足6");
            }
            HotelVO hotelVO = hotelService.retrieveHotelDetails(hotelId);
            if(hotelVO==null){
                throw new ServiceException("酒店不存在");
            }
            if(hotelVO.getManagerId()!=null){
                throw new ServiceException("工作人员已存在");
            }
            User user = new User();
            user.setEmail(userForm.getEmail());
            user.setPassword(userForm.getPassword());
            user.setUserType(UserType.HotelManager);
            adminMapper.addManager(user);
            Integer managerId = user.getId();//id会映射到po中
            responseVO = hotelService.addHotelManager(hotelId,managerId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("网站管理人员添加酒店工作人员任务中发生错误: "+e.getMessage());
        }
        return responseVO;
    }
    //依赖data.user.AccountMapper.getAccountById(int id)检查管理员身份 X
    //依赖bl.hotel.HotelService.retrieveHotelDetails(Integer hotelId)，由hotelId获取managerId，若不为空则返回添加失败
    //依赖data.user.AccountMapper.getAccountByName(String email)检查邮箱是否已被注册
    //依赖data.admin.AdminMapper.addManager(User user)，获取插入新user项并获取id
    //依赖bl.hotel.HotelService.addHotelManager(Integer hotelId, Integer managerId)，将该id更新到用hotelId查找到的酒店信息中的managerId中

    @Override
    public List<User> getAllManagers() {
        return adminMapper.getAllManagers();
    }
    //依赖data.admin.AdminMapper.getAllManagers()

    @Override
    public ResponseVO addMarketer(UserForm userForm) {
        try {
//            User admin = accountMapper.getAccountById(adminId);
//            if(admin==null||!admin.getUserType().equals(UserType.Administrator)){
//                throw new ServiceException("管理员不存在或者无权限！");
//            }
            if(!userForm.getEmail().matches(EMAIL_FORM_1)){
                throw new ServiceException("作为账号的邮箱格式不正确");
            }
            User possibleUser = accountMapper.getAccountByName(userForm.getEmail());
            if(possibleUser!=null){
                throw new ServiceException(ACCOUNT_EXIST);
            }
            if(userForm.getPassword().length()<6){
                throw new ServiceException("密码长度不足6");
            }
            User user = new User();
            user.setEmail(userForm.getEmail());
            user.setPassword(userForm.getPassword());
            user.setUserType(UserType.Marketer);
            adminMapper.addMarketer(user);
            return ResponseVO.buildSuccess(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("网站管理人员添加网站营销人员任务中发生错误: "+e.getMessage());
        }
    }
    //依赖data.user.AccountMapper.getAccountById(int id)检查管理员身份 X
    //依赖data.user.AccountMapper.getAccountByName(String email)检查邮箱是否已被注册
    //依赖data.admin.AdminMapper.addMarketer(User user)

    @Override
    public List<User> getAllMarketers() {
        return adminMapper.getAllMarketers();
    }
    //依赖data.admin.AdminMapper.getAllMarketers()

    @Override
    public List<User> getAllClients() {
        return adminMapper.getAllClients();
    }
    //依赖data.admin.AdminMapper.getAllClients()

    @Override
    public ResponseVO updateUserInfo(int userId, String password, String username, String phonenumber) {
        try{
//            User user = accountMapper.getAccountById(adminId);
//            if(user==null||!user.getUserType().equals(UserType.Administrator)){
//                throw new ServiceException("管理员不存在或者无权限！");
//            }
            accountMapper.updateAccount(userId, password, username, phonenumber);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("管理人员查看用户信息任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.user.AccountMapper.getAccountById(int id)检查管理员身份 X
    //依赖data.user.AccountMapper.updateAccount(int id, String password, String username, String phonenumber)

    @Override
    public ResponseVO removeUser(int userId) {
        try{
//            User user = accountMapper.getAccountById(adminId);
//            if(user==null||!user.getUserType().equals(UserType.Administrator)){
//                throw new ServiceException("管理员不存在或者无权限！");
//            }
            User user = accountMapper.getAccountById(userId);
            if(user.getUserType().equals(UserType.HotelManager)||user.getUserType().equals(UserType.Marketer)){
                if(user.getUserType().equals(UserType.HotelManager)){
                    List<HotelVO> hotelVOS = hotelService.retrieveHotels();
                    for(HotelVO hotelVO:hotelVOS){
                        if(hotelVO.getManagerId()==userId){
                            hotelService.addHotelManager(hotelVO.getId(),null);
                            break;
                        }
                    }
                }
                accountMapper.deleteAccountById(userId);
            }else {
                throw new ServiceException("管理人员只能删除酒店工作人员以及网站营销人员的账号！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("管理人员删除用户任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.user.AccountMapper.getAccountById(int id)检查管理员身份 X
    //依赖data.user.AccountMapper.getAccountById(int id)检查用户类型
    //依赖bl.hotel.HotelService.retrieveHotels()获取所以酒店
    //依赖bl.hotel.HotelService.addHotelManager(Integer hotelId,Integer adminId)删除酒店工作人员对应酒店的工作人员
    //依赖data.user.AccountMapper.deleteAccountById(int id)
}
