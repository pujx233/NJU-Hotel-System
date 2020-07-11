/*作业改动 by檀潮*/ //添加
package com.example.hotel.bl.vip;

import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.VIP;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserVO;
import com.example.hotel.vo.VIPVO;

public interface VIPService {

    /**
     * 注册会员
     * @param vipVO
     * @return
     */
    ResponseVO registerVIP(VIPVO vipVO);

    /**
     * 查看会员信息
     * @param userId
     * @return
     */
    VIP getVIPInfo(int userId);

    /**
     * 更新客户信用值同时更新会员等级
     * @param userId
     * @param credit
     * @return
     */                                  //credit正增负减
    void updateClientCredit(int userId, double credit) throws ServiceException;

    /**
     * 根据会员用户当前信用值更新会员等级
     * @param userId
     * @return
     */
    void updateVIPLevel(int userId);

    /**
     * 为会员用户设定会员等级
     * @param vipId
     * @param level
     * @return
     */
    void setVIPLevel(int vipId, VIPLevel level);
}
/*作业改动*/
