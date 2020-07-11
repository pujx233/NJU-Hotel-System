package com.example.hotel.blImpl.marketer;

import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.data.marketer.MarketerMapper;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.OrderState;
import com.example.hotel.po.Order;
import com.example.hotel.po.User;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketerServiceImpl implements MarketerService {
    @Autowired
    MarketerMapper marketerMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    VIPService vipService;
    @Autowired
    OrderService orderService;
    @Autowired
    AccountMapper accountMapper;

    @Override
    public List<Order> getAbnormalOrders() {
        return marketerMapper.getAllAbnormalOrders();
    }
    //依赖data.marketer.MarketerMapper.getAllAbnormalOrders()

    @Override
    public ResponseVO annulAbnormalOrder(int orderid, double percent) {
        try{
            Order order = orderMapper.getOrderById(orderid);
            if(order==null||!order.getOrderState().equals(OrderState.Abnormal.toString())){
                throw new SecurityException("输入的待撤销的异常订单不是有效输入");
            }
            orderService.annulOrder(orderid);
            vipService.updateClientCredit(order.getUserId(),order.getPrice()*percent);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("网站营销人员撤销异常订单任务中发生错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.order.OrderMapper.getOrderById(int orderid)获取订单价格value
    //依赖bl.vip.VIPService.updateClientCredit(int userId, double credit)增减信用值(value*percent)
    //依赖bl.order.OrderService.annulOrder(int orderid)撤销订单以及恢复房间

    @Override
    public ResponseVO buyCredit(String email, double credit) {
        try{
            User user = accountMapper.getAccountByName(email);
            if(user==null){
                throw new ServiceException("账号不存在");
            }
            if (credit <= 0) {
                throw new ServiceException("只能执行充值信用值操作");
            }
            vipService.updateClientCredit(user.getId(),credit);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("网站营销人员充值信用值任务中发生错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.user.AccountMapper.getAccountByName(String email)来获取用户id
    //依赖bl.vip.VIPService.updateClientCredit(int userId, double credit)来增减信用值
}
