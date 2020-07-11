/*作业改动 by檀潮*/ //添加
package com.example.hotel.bl.marketer;

import com.example.hotel.po.Order;
import com.example.hotel.vo.ResponseVO;

import java.util.List;

public interface MarketerService {

    /**
     * 查找所有异常订单
     * @return
     */
    List<Order> getAbnormalOrders();

    /**
     * 撤销异常订单并恢复信用值，恢复房间
     * @param orderid
     * @param percent
     * @return
     */
    ResponseVO annulAbnormalOrder(int orderid, double percent);

    /**
     * 根据账号更新用户的信用值
     * @param email
     * @param credit
     * @return
     */
    ResponseVO buyCredit(String email, double credit);

}
/*作业改动*/
