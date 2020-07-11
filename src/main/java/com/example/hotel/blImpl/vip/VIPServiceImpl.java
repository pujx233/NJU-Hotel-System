/*作业改动 by檀潮*/ //添加
package com.example.hotel.blImpl.vip;

import com.example.hotel.bl.user.AccountService;
import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.data.vip.VIPMapper;
import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.User;
import com.example.hotel.po.VIP;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.VIPVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VIPServiceImpl implements VIPService {

    @Autowired
    VIPMapper vipMapper;
    @Autowired
    AccountService accountService;

    @Override
    public ResponseVO registerVIP(VIPVO vipVO) {
        try{
            VIP possibleVIP = vipMapper.getVIPByUserId(vipVO.getUserId());
            if(possibleVIP!=null){
                throw new ServiceException("已注册会员");
            }
            VIP vip = new VIP();
            vip.setUserId(vipVO.getUserId());
            vip.setBirthday(vipVO.getBirthday());
            vip.setLevel(VIPLevel.Hamon);//设一个初值，因为数据库里是NOT NULL
            vipMapper.createNewVIP(vip);
            updateVIPLevel(vipVO.getUserId());
            return ResponseVO.buildSuccess(vip.getId());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("注册会员任务中发生错误: "+e.getMessage());
        }
    }
    //依赖data.vip.VIPMapper.getVIPByUserId(int userId)判断用户是否已注册会员
    //依赖data.vip.VIPMapper.createNewVIP(VIP vip)
    //依赖VIPService.updateVIPLevel(int userId)

    @Override
    public VIP getVIPInfo(int userId) {
        return vipMapper.getVIPByUserId(userId);
    }
    //依赖data.vip.VIPMapper.getVIPByUserId(int userId)

    @Override
    public void updateClientCredit(int userId, double credit) throws ServiceException {
        //此处credit取正负代表原来信用值基础上的加减credit，void返回值代表此方法并非直接提供给操作者、而是供别的方法调用
        try {
            accountService.updateUserCredit(userId,credit);
            updateVIPLevel(userId);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("更新客户信用时发生错误！");
        }
    }
    //依赖bl.user.AccountService.updateUserCredit(int id, double credit)
    //依赖VIPService.updateVIPlevel(int userId)来更新会员等级

    @Override
    public void updateVIPLevel(int userId) {
        //此处包含根据信用值设定会员等级的逻辑
        //可以将信用值-会员等级的关系用判断语句写在这里，也可以查表获得
        VIP vip = vipMapper.getVIPByUserId(userId);
        if(vip==null)return;
        User user = accountService.getUserInfo(userId);
        double credit = user.getCredit();
        VIPLevel newLevel = null;
        if(credit<600){
            newLevel = VIPLevel.Hamon;
        }else if(credit>=600&&credit<1500){
            newLevel = VIPLevel.HermitPurple;
        }else if(credit>=1500&&credit<3000){
            newLevel = VIPLevel.StarPlatinum;
        }else if(credit>=3000&&credit<5000){
            newLevel = VIPLevel.CrazyDiamond;
        }else {
            newLevel = VIPLevel.GoldExperience;
        }
        VIPLevel oldLevel = vip.getLevel();
        if(!oldLevel.equals(newLevel)){
            setVIPLevel(vip.getId(),newLevel);
        }
    }
    //依赖data.vip.VIPMapper.getVIPByUserId(int userId)获取会员信息，若返回值为空则return，否则取出会员号vipId
    //依赖bl.user.AccountService.getUserInfo(int id)获取信用值,在有必要修改的情况下：
    //依赖VIPService.setVIPLevel(int vipId, String level)来根据信用值设定会员等级

    @Override
    public void setVIPLevel(int vipId, VIPLevel level) {
        vipMapper.setVIPLevel(vipId,level);
    }
    //依赖VIPMapper.setVIPLevel(int vipId, VIPLevel level)
}
/*作业改动*/
