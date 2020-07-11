package com.example.hotel.blImpl.order;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.data.admin.AdminMapper;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.OrderState;
import com.example.hotel.po.Hotel;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.Order;
import com.example.hotel.po.User;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.TimeCouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: chenyizong
 * @Date: 2020-03-04
 */
@Service
public class OrderServiceImpl implements OrderService {
    private final static String ORDER_NOT_EXIST = "查找不到订单";

    @Autowired
    OrderMapper orderMapper;

    /*作业改动 by檀潮*/ //删除
//    @Autowired
//    AccountMapper accountMapper;
    /*作业改动*/

    @Autowired
    HotelService hotelService;
    @Autowired
    AccountService accountService;
    @Autowired
    VIPService vipService;


    @Override
    public ResponseVO addOrder(OrderVO orderVO) {//此处开始异常订单的计时，超过最晚入住时间时检查是否已执行，若否则调用异常订单方法(abnormalOrder）
        try {
            int reserveRoomNum = orderVO.getRoomNum();
//            System.out.println(reserveRoomNum);
            int curNum = hotelService.getRoomCurNum(orderVO.getHotelId(),orderVO.getRoomType());
            if(reserveRoomNum>curNum){
                throw new ServiceException("预订房间数量剩余不足");
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime inTime = LocalDateTime.parse(orderVO.getCheckInDate()+" 00:00:00",df);
            LocalDateTime lastInTime = LocalDateTime.parse(orderVO.getCheckInDate()+" 23:59:59",df);
            LocalDateTime outTime = LocalDateTime.parse(orderVO.getCheckOutDate()+" 00:00:00",df);
            LocalDateTime now = LocalDateTime.now();
            if(now.compareTo(lastInTime)>0){
                throw new ServiceException("预订入住时间必须在今日及以后");
            }
            if(inTime.compareTo(outTime)>=0){
                throw new ServiceException("离店日期必须在入住日期的次日及以后");
            }
            User user = accountService.getUserInfo(orderVO.getUserId());
            if(user.getCredit()<0){
                throw new ServiceException("信用值小于0时无法预订");
            }
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String curdate = sf.format(date);
            orderVO.setCreateDate(curdate);
            orderVO.setOrderState(OrderState.Booked.toString());
            orderVO.setClientName(user.getUserName());
            orderVO.setClientEmail(user.getEmail());
            orderVO.setPhoneNumber(user.getPhoneNumber());
            Order order = new Order();
            BeanUtils.copyProperties(orderVO,order);
            order.setRar(false);
            order.setPrice((int)(order.getPrice()*100+0.5)/100.0);
            orderMapper.addOrder(order);
            Integer orderId = order.getId();                                   //将参数roomNum置负，同时修改dataImpl中符号为正
            hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),-orderVO.getRoomNum());

            //自动撤销订单有两种实现方法：
            // 1延时，开一个线程在给定延时时间后做任务
            // 2定时，开一个线程时刻检查当前时刻是否为给定时刻，若是则做任务
            //以下决定用延时实现方法
            long delay = Duration.between(now,inTime).toMillis();
            delay += 24*60*60*1000;
            //以下为ScheduledExecutorService方法，未经过充分测试，因为听说比Timer屌，所以用这个
            //一个newSingleThreadScheduledExecutor()只有一个线程，但由于每次执行自动撤销任务初始化以此，也算是多线程
            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try{
                        abnormalOrder(orderId);
                        System.out.println(df.format(now)+"预订的订单(id:"+orderId
                                +")自动置为异常任务成功");
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println(df.format(now)+"预订的订单(id:"+orderId
                                +")自动置为异常任务中出现错误: "+e.getMessage());
                    }
                }
            };
            service.schedule(runnable,delay,TimeUnit.MILLISECONDS);


            return ResponseVO.buildSuccess(orderId);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("生成订单任务中发生错误: "+e.getMessage());
        }
    }
    //依赖bl.hotel.HotelService.getRoomCurNum(Integer hotelId,String roomType)来检查余房是否足够
    //依赖bl.user.AccountService.getUserInfo(int id)获取用户名与电话属性（若前端不负责信用值>=0的判断，则此处需要取credit判断）
    //依赖data.order.OrderMapper.addOrder(Order order)添加订单
    //依赖bl.hotel.HotelService.updateRoomInfo(Integer hotelId, String roomType,Integer rooms)更新房间信息，rooms正加负减
    //依赖data.order.OrderMapper.getOrderById(int orderid)在定时结束后检查订单是否已执行
    //依赖OrderService.abnormalOrder(int orderid)在超时未执行的情况下将订单置为异常

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }
    //依赖data.order.OrderMapper.getAllOrders()

    @Override
    public List<Order> getUserOrders(int userid) {
        return orderMapper.getUserOrders(userid);
    }
    //依赖data.order.OrderMapper.getUserOrders(int userid)

    //此方法待更改或补充
    @Override
    public ResponseVO annulBookedOrder(int orderid) {
        //取消预订的订单逻辑的具体实现（注意可能有和别的业务类之间的交互）

        try{
            Order order = orderMapper.getOrderById(orderid);
            if(order==null){
                throw new ServiceException(ORDER_NOT_EXIST);
            }
            if(!order.getOrderState().equals(OrderState.Booked.toString())){
                throw new ServiceException("客户只能撤销已预订的订单");
            }
            DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime lastInTime = LocalDateTime.parse(order.getCheckInDate()+" 23:59:59",df_toSecond);
            LocalDateTime now = LocalDateTime.now();
            if(order.getOrderState().equals(OrderState.Abnormal.toString())){
                throw new ServiceException("订单已经进入异常状态");
            }
            if(now.compareTo(lastInTime)>0){
                throw new ServiceException("错误！订单无法被撤销！订单应当已经进入异常状态，但订单状态仍未置为异常！");
            }
            annulOrder(orderid);
            long distance = Duration.between(now,lastInTime).toHours();
            System.out.println("撤销订单时间与最迟订单执行时间相距小时数为: "+distance);//测试用，有机会记得改掉
            if(distance<6){
                vipService.updateClientCredit(order.getUserId(),-order.getPrice()*0.5);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("客户撤销已预订订单任务中出现错误 "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.order.OrderMapper.getOrderById(int orderid)获取入住日期，该天23:59:59为最晚订单执行时间；若离之不足6h，
    //依赖OrderService.annulOrder撤销订单与恢复房间
    //依赖bl.vip.VIPService.updateClientCredit(int userId, double credit)增减用户信用值；


    public void annulOrder(int orderid) throws ServiceException{
        Order order = orderMapper.getOrderById(orderid);
        hotelService.updateRoomInfo(order.getHotelId(),order.getRoomType(),order.getRoomNum());
        orderMapper.annulOrder(orderid);
    }
    //依赖data.order.OrderMapper.annulOrder(int orderid)撤销订单
    //依赖bl.hotel.HotelService.updateRoomInfo(Integer hotelId, String roomType,Integer rooms)更新房间信息，rooms正加负减

    /**
     * @param hotelId
     * @return
     */
    @Override
    public List<Order> getHotelOrders(Integer hotelId) {
        List<Order> orders = getAllOrders();
        return orders.stream().filter(order -> order.getHotelId().equals(hotelId)).collect(Collectors.toList());
    }
    //依赖OrderService.getAllOrders()

    @Override
    public void abnormalOrder(int orderid) throws ServiceException{
        //由addOrder定时调用，利用orderMapper.getOrderById(orderid)获取订单用户的userid与总价值value，
        //更改已预订的订单状态为异常，并调用VIPServiceImpl.updateClientCredit(userid,-value)来扣除信用值value
        Order order = orderMapper.getOrderById(orderid);
        if(order.getOrderState().equals(OrderState.Booked.toString())){
            orderMapper.abnormalOrder(orderid);
            vipService.updateClientCredit(order.getUserId(),-order.getPrice());
        }else {
            //此处不需要做任何事
//            throw new ServiceException("订单状态为"+order.getOrderState()+"，不能被置为异常");
        }
    }
    //依赖data.order.OrderMapper.getOrderById(int orderid)
    //依赖data.order.OrderMapper.abnormalOrder(int orderid)
    //依赖bl.vip.VIPService.updateClientCredit(int userId, double credit)

    @Override
    public ResponseVO solveAbnormalOrder(int orderid) {
        try{
            Order order = orderMapper.getOrderById(orderid);
            if(order==null){
                throw new ServiceException(ORDER_NOT_EXIST);
            }
            if(!order.getOrderState().equals(OrderState.Abnormal.toString())){
                throw new ServiceException("给定的订单不能被置为过期异常");
            }
            orderMapper.solveAbnormalOrder(orderid);
            hotelService.updateRoomInfo(order.getHotelId(),order.getRoomType(),order.getRoomNum());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("解决过期异常订单任务中出现错误 "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.order.OrderMapper.getOrderById(int orderid)判断订单状态爱是否为异常，不是则返回
    //依赖data.order.OrderMapper.solveAbnormalOrder(int orderid)将异常状态置为过期异常
    //依赖bl.hotel.HotelService.updateRoomInfo(Integer hotelId, String roomType,Integer rooms)更新房间信息，rooms正加负减

    @Override
    public ResponseVO executeOrder(int orderid) {
        //由controller.order调用，利用orderMapper.getOrderById(orderid)获取订单用户的userid与总价值value，
        //更改已预订或异常的订单状态为已执行（房间信息在addOrder更新过，此处不要动），
        //并调用VIPServiceImpl.updateClientCredit(userid,value)来增加信用值value

        try{
            Order order = orderMapper.getOrderById(orderid);
            if(order==null){
                throw new ServiceException(ORDER_NOT_EXIST);
            }
            if(!order.getOrderState().equals(OrderState.Booked.toString())&&
                    !order.getOrderState().equals(OrderState.Abnormal.toString())){
                throw new ServiceException("给定的订单不能被执行");
            }
            orderMapper.executeOrder(orderid);
            vipService.updateClientCredit(order.getUserId(),order.getPrice());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("执行订单任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.order.OrderMapper.getOrderById(int orderid)
    //依赖data.order.OrderMapper.excuteOrder(int orderid)
    //依赖bl.vip.VIPService.updateClientCredit(int userId, double credit)

    @Override
    public ResponseVO leaveEarly(int orderid) {
        //由OrderMapper.getOrderById得到入住日期、离店日期和总价
        //若离店日期大等于当前日期，返回ResponseVO.buildFailure()
        //若离店日期小于当前日期，则计算d1=离店日期-入住日期的天数，修改离店日期为当前日期，再次计算d2=离店日期-入住日期
        //由总价乘以d2/d1得到新总价，替换原总价，若离店日期等于入住日期，则保留价格为一天的价格
        //用OrderMapper.updateOrder及当前算得数据更新Order表
        try{
            Order order = orderMapper.getOrderById(orderid);
            if(order==null){
                throw new ServiceException(ORDER_NOT_EXIST);
            }
            if(!order.getOrderState().equals(OrderState.Executed.toString())){
                throw new ServiceException("只能为已执行的订单办理提前离店");
            }
            DateTimeFormatter df_toSecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime outTime = LocalDateTime.parse(order.getCheckOutDate()+" 00:00:00",df_toSecond);
            LocalDateTime now = LocalDateTime.now();
            if(now.compareTo(outTime)>=0){
                throw new ServiceException("仅当当前日期在离店日期之前时可以提前离店结算");
            }
            DateTimeFormatter df_toDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String leaveDate = df_toDay.format(LocalDateTime.now());
            LocalDateTime thisTime = LocalDateTime.parse(leaveDate+" 00:00:00",df_toSecond);
            LocalDateTime inTime = LocalDateTime.parse(order.getCheckInDate()+" 00:00:00",df_toSecond);
            long oldDistance = Duration.between(inTime,outTime).toDays();
            long newDistance = Duration.between(inTime,thisTime).toDays();
            newDistance = newDistance==0?1:newDistance;
            double newPrice = order.getPrice()*newDistance/oldDistance;
            hotelService.updateRoomInfo(order.getHotelId(),order.getRoomType(),order.getRoomNum());
            vipService.updateClientCredit(order.getUserId(),newPrice-order.getPrice());
            orderMapper.updateOrder(orderid,leaveDate,(int)(newPrice*100+0.5)/100.0);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("提前离店任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.order.OrderMapper.getOrderById(int orderid)
    //依赖bl.hotel.HotelService.updateRoomInfo(Integer hotelId, String roomType,Integer rooms)更新房间信息，rooms正加负减
    //依赖data.order.OrderMapper.updateOrder(Integer orderId, String checkOutDate, Double price)


}
