package com.example.hotel.bl.order;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.*;
import com.example.hotel.logic.user.AdminLogic;
import com.example.hotel.logic.user.ClientLogic;
import com.example.hotel.logic.vip.VIPLogic;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.Order;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.VIPVO;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class OrderServiceTest extends BaseTest {
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountService accountService;
    @Autowired
    AdminService adminService;
    @Autowired
    VIPService vipService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderService orderService;
    @Autowired
    HotelService hotelService;
    @Autowired
    MarketerService marketerService;

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
    }

    @Test
    public void testOrder(){
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();
        //插入管理人员账号，获取adminId

        HotelVO hotelVO = new HotelVO();
        hotelVO.setName("tmpHotel");
        hotelVO.setBizRegion(BizRegion.WangFuJing.toString());
        hotelVO.setHotelStar(HotelStar.Four.toString());
        ResponseVO responseVO = adminService.addHotel(hotelVO,adminId);
        assertTrue(responseVO.getSuccess());
        int hotelId = (Integer)responseVO.getContent();
        //插入酒店，获取hotelId

        HotelRoom hotelRoom = new HotelRoom();
        RoomType roomType = RoomType.Family;
        hotelRoom.setRoomType(roomType);
        hotelRoom.setHotelId(hotelId);
        hotelRoom.setCurNum(20);
        hotelRoom.setTotal(20);
        hotelRoom.setPrice(200);
        assertTrue(hotelService.insertRoomInfo(hotelRoom).getSuccess());
        //为酒店插入房间

        ClientLogic clientLogic = new ClientLogic(accountMapper,accountService,adminService);
        VIPLogic vipLogic = new VIPLogic(vipService);
        User user = clientLogic.generateUser(0);
        clientLogic.addUser(user);
        VIPVO vipvo = vipLogic.generateVIPVO(user.getId());
        assertTrue(vipService.registerVIP(vipvo).getSuccess());

        double stdCredit = 100.0;
        double credit = accountService.getUserInfo(user.getId()).getCredit();
        accountMapper.updateAccountCredit(user.getId(),-credit);
        accountMapper.updateAccountCredit(user.getId(),stdCredit);
        credit = stdCredit;
        //插入客户并为客户插入会员（初始信用值100，因此初始会员等级为Hamon）

        List<Order> orderList = orderService.getAllOrders();
        int oldOrderNum = orderList.size();
        assertEquals(0,orderService.getUserOrders(user.getId()).size());
        assertEquals(0,orderService.getHotelOrders(hotelId).size());
        //记录总订单数，测试酒店与用户的初始订单数为0

        int rightRoomNum = 3;
        int wrongRoomNum = 21;
        double price = 2100;
        OrderVO orderVO = new OrderVO();
        orderVO.setHotelId(hotelId);
        orderVO.setHotelName(hotelVO.getName());
        orderVO.setUserId(user.getId());
        orderVO.setCheckInDate("2000-02-01");
        orderVO.setCheckOutDate("2060-02-04");
        orderVO.setRoomNum(rightRoomNum);
        orderVO.setRoomType(roomType.toString());
        orderVO.setPrice(price);
        assertFalse(orderService.addOrder(orderVO).getSuccess());//入住日期必须在当前日期或以后
        orderVO.setCheckInDate("2060-02-01");
        orderVO.setRoomNum(wrongRoomNum);
        assertFalse(orderService.addOrder(orderVO).getSuccess());//预订房间数量不能多于剩余房间数量
        orderVO.setRoomNum(rightRoomNum);





        //---------------------------测试客户添加订单-----------------------------

        responseVO = orderService.addOrder(orderVO);
        assertTrue(responseVO.getSuccess());
        orderVO.setId((Integer) responseVO.getContent());
        try{

            //测试房间数量变化
            assertEquals(hotelRoom.getCurNum()-rightRoomNum,hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}

        orderList = orderService.getAllOrders();
        assertEquals(oldOrderNum+1,orderList.size());
        //用orderService.getAllOrders检查订单总数前后数量变化1
        assertEquals(1,orderService.getUserOrders(user.getId()).size());
        assertEquals(1,orderService.getHotelOrders(hotelId).size());
        //用orderService.getUserOrders、orderService.getHotelOrders
        //来测试酒店与用户的前后订单数量变化为1

        for(Order order:orderList){
            if(order.getId().equals(orderVO.getId())){
                assertEquals(user.getUserName(),order.getClientName());
                assertEquals(user.getEmail(),order.getClientEmail());
                assertEquals(user.getId(),order.getUserId());
                assertEquals((Integer)hotelId,order.getHotelId());
                assertEquals(orderVO.getHotelName(),order.getHotelName());
                assertEquals(orderVO.getCheckInDate(),order.getCheckInDate());
                assertEquals(orderVO.getCheckOutDate(),order.getCheckOutDate());
                assertEquals(orderVO.getPhoneNumber(),order.getPhoneNumber());
                assertEquals(orderVO.getPeopleNum(),order.getPeopleNum());
                assertEquals(orderVO.isHaveChild(),order.isHaveChild());
                assertEquals(orderVO.getPrice(),order.getPrice(),0.0001);
                assertEquals(orderVO.getRoomNum(),order.getRoomNum());
                assertEquals(orderVO.getRoomType(),order.getRoomType());
                assertEquals(OrderState.Booked.toString(),orderVO.getOrderState());
                assertFalse(order.isRar());
                break;
            }
        }
        //检查插入后与插入前属性相同以及检查一些自动赋值的属性






        //---------------------------撤销订单的基础动作------------------------

        try{
            orderService.annulOrder(orderVO.getId());
            //测试房间数量的恢复
            assertEquals(hotelRoom.getCurNum(),hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){e.printStackTrace();fail();}
        assertEquals(OrderState.Annulled.toString(),orderMapper.getOrderById(orderVO.getId()).getOrderState());
        //测试状态是否被置为“已撤销”

        //重新下订单
        responseVO = orderService.addOrder(orderVO);
        assertTrue(responseVO.getSuccess());
        orderVO.setId((Integer) responseVO.getContent());
        try{

            //测试房间数量变化
            assertEquals(hotelRoom.getCurNum()-rightRoomNum,hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}



        //---------------------------客户撤销订单------------------------

        assertTrue(orderService.annulBookedOrder(orderVO.getId()).getSuccess());
        try{
            //测试房间数量的恢复
            assertEquals(hotelRoom.getCurNum(),hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}
        assertEquals(OrderState.Annulled.toString(),orderMapper.getOrderById(orderVO.getId()).getOrderState());
        //测试状态是否被置为“已撤销”

        //测试会员等级随信用值的变化
        assertEquals(vipLogic.mapLevel(accountService.getUserInfo(user.getId()).getCredit()),vipService.getVIPInfo(user.getId()).getLevel());



        //重新下订单

        //假设撤销异常订单距离最迟订单执行时间6小时内，信用值下降至<0
        accountMapper.updateAccountCredit(user.getId(),-accountService.getUserInfo(user.getId()).getCredit()-1);
        //实现该假设

        assertTrue(credit-price<0);
        assertFalse(orderService.addOrder(orderVO).getSuccess());//信用值<0时无法下单
        accountMapper.updateAccountCredit(user.getId(),-accountService.getUserInfo(user.getId()).getCredit()+stdCredit);

        DateTimeFormatter df_toDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        orderVO.setCheckInDate(df_toDay.format(LocalDateTime.now()));//将入住日期定为今天
        orderVO.setCheckOutDate(df_toDay.format(LocalDateTime.now().plusDays(3)));//将离店日期定为三日后
        responseVO = orderService.addOrder(orderVO);
        assertTrue(responseVO.getSuccess());//下单成功
        orderVO.setId((Integer) responseVO.getContent());
        try{

            //测试房间数量变化
            assertEquals(hotelRoom.getCurNum()-rightRoomNum,hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}




        //---------------------------酒店工作人员执行订单------------------------

        assertTrue(orderService.executeOrder(orderVO.getId()).getSuccess());

        //测试状态是否被置为“已执行”
        assertEquals(OrderState.Executed.toString(),orderMapper.getOrderById(orderVO.getId()).getOrderState());

        //测试用户信用增加
        assertEquals(credit+price,accountService.getUserInfo(user.getId()).getCredit(),0.0001);

        //测试会员等级变化
        assertEquals(vipLogic.mapLevel(credit+price),vipService.getVIPInfo(user.getId()).getLevel());









        //---------------------------酒店工作人员办理提前离店------------------------

        assertTrue(orderService.leaveEarly(orderVO.getId()).getSuccess());

        //测试用户信用会根据实际入住天数与订单记录的入住天数的比例修改订单价格，但至少会保留一天的住宿费用，并根据此更新用户信用值与实际交费相符
        assertEquals(credit+price*1/3,accountService.getUserInfo(user.getId()).getCredit(),0.0001);

        //测试会员等级变化
        assertEquals(vipLogic.mapLevel(credit+price*1/3),vipService.getVIPInfo(user.getId()).getLevel());

        //测试房间数量的恢复
        try{
            assertEquals(hotelRoom.getCurNum(),hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}



        //重新下订单
        responseVO = orderService.addOrder(orderVO);
        assertTrue(responseVO.getSuccess());
        orderVO.setId((Integer) responseVO.getContent());
        try{

            //测试房间数量变化
            assertEquals(hotelRoom.getCurNum()-rightRoomNum,hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}




        //-----------------------把订单置为异常的基础操作------------------------------
        try{
            orderService.abnormalOrder(orderVO.getId());
        }catch (Exception e){fail();}
        assertEquals(OrderState.Abnormal.toString(),orderMapper.getOrderById(orderVO.getId()).getOrderState());
        //测试状态是否被置为“异常”

        //测试用户信用变化
        assertEquals(credit-price*2/3,accountService.getUserInfo(user.getId()).getCredit(),0.0001);

        //测试会员等级变化
        assertEquals(vipLogic.mapLevel(credit-price*2/3),vipService.getVIPInfo(user.getId()).getLevel());




        //-----------------------酒店工作人员解决异常订单------------------------------
        assertTrue(orderService.solveAbnormalOrder(orderVO.getId()).getSuccess());
        try{
            //测试房间数量的恢复
            assertEquals(hotelRoom.getCurNum(),hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}
        assertEquals(OrderState.OverdueAbnormal.toString(),orderMapper.getOrderById(orderVO.getId()).getOrderState());
        //测试状态是否被置为“过期异常”



        //恢复信用值后重新下订单
        accountMapper.updateAccountCredit(user.getId(),-accountService.getUserInfo(user.getId()).getCredit()+stdCredit);
        responseVO = orderService.addOrder(orderVO);
        assertTrue(responseVO.getSuccess());
        orderVO.setId((Integer) responseVO.getContent());
        try{

            //测试房间数量变化
            assertEquals(hotelRoom.getCurNum()-rightRoomNum,hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}

        //将订单置为异常
        try{
            orderService.abnormalOrder(orderVO.getId());
        }catch (Exception e){fail();}
        assertEquals(OrderState.Abnormal.toString(),orderMapper.getOrderById(orderVO.getId()).getOrderState());
        //测试状态是否被置为“异常”

        //测试用户信用被减少
        assertEquals(credit-price,accountService.getUserInfo(user.getId()).getCredit(),0.0001);

        //测试会员等级变化
        assertEquals(vipLogic.mapLevel(credit-price),vipService.getVIPInfo(user.getId()).getLevel());





        //------------------------网站营销人员撤销异常订单----------------------------
        double percent = Math.abs(new Random().nextInt(100))/100.0;
        assertTrue(marketerService.annulAbnormalOrder(orderVO.getId(),percent).getSuccess());
        try{
            //测试房间数量的恢复
            assertEquals(hotelRoom.getCurNum(),hotelService.getRoomCurNum(hotelId,roomType.toString()));
        }catch (Exception e){fail();}
        assertEquals(OrderState.Annulled.toString(),orderMapper.getOrderById(orderVO.getId()).getOrderState());
        //测试状态是否被置为“已撤销”

        //测试用户信用变化
        assertEquals(credit+(percent-1)*price,accountService.getUserInfo(user.getId()).getCredit(),0.0001);

        //测试会员等级变化
        assertEquals(vipLogic.mapLevel(credit+(percent-1)*price),vipService.getVIPInfo(user.getId()).getLevel());

    }

}