package com.example.hotel.bl.marketer;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.bl.vip.VIPService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.OrderState;
import com.example.hotel.enums.RoomType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MarketerServiceTest extends BaseTest {
    @Autowired
    MarketerService marketerService;
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
    public void getAbnormalOrders() {
        int testNum = 50;

        Order tmpOrder = new Order();
        tmpOrder.setUserId(1);
        tmpOrder.setClientName("1");
        tmpOrder.setClientEmail("1@qq.com");
        tmpOrder.setPeopleNum(3);
        tmpOrder.setPhoneNumber("10086");
        tmpOrder.setHotelId(1);
        tmpOrder.setHotelName("1");
        tmpOrder.setRoomType(RoomType.BigBed.toString());
        tmpOrder.setRoomNum(3);
        tmpOrder.setPrice(1.0);
        tmpOrder.setCheckInDate("2060-01-01");
        tmpOrder.setCheckOutDate("2060-02-01");
        tmpOrder.setCreateDate("2000-01-01");
        tmpOrder.setHaveChild(false);
        tmpOrder.setRar(false);

        //随机生成多种订单插入数据库中
        Random random = new Random();
        for(int i=0;i<testNum;i++){
            int any = Math.abs(random.nextInt(5))%5;
            switch (any){
                case 0:
                    tmpOrder.setOrderState(OrderState.Booked.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 1:
                    tmpOrder.setOrderState(OrderState.Executed.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 2:
                    tmpOrder.setOrderState(OrderState.Annulled.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 3:
                    tmpOrder.setOrderState(OrderState.Abnormal.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;
                case 4:
                    tmpOrder.setOrderState(OrderState.OverdueAbnormal.toString());
                    orderMapper.addOrder(tmpOrder);
                    break;

            }
        }

        //通过marketerService.getAbnormalOrders()与orderService.getAllOrder获取异常订单
        //比较获取的异常订单集合，测试marketerService.getAbnormalOrders()的正确性
        //（其中orderService.getAllOrder已在orderServiceTest中测试过）
        List<Order> orders = marketerService.getAbnormalOrders();
        List<Order> orderList = orderService.getAllOrders();
        List<Order> abnormalOrders = new ArrayList<>();
        for(Order order:orderList){
            if(order.getOrderState().equals(OrderState.Abnormal.toString())){
                abnormalOrders.add(order);
            }
        }
        assertEquals(abnormalOrders.size(),orders.size());
        //验证marketerService.getAbnormalOrders取出的异常订单的数量的正确性

        for(Order order1: orders){
            boolean match = false;
            for(Order order2: abnormalOrders){

                //为相匹配的订单比较各个属性，判断是否相同
                if(order1.getId().equals(order2.getId())){
                    match = true;
                    assertEquals(order1.getClientName(),order2.getClientName());
                    assertEquals(order1.getClientEmail(),order2.getClientEmail());
                    assertEquals(order1.getHotelId(),order2.getHotelId());
                    assertEquals(order1.getHotelName(),order2.getHotelName());
                    assertEquals(order1.getCreateDate(),order2.getCreateDate());
                    assertEquals(order1.getCheckInDate(),order2.getCheckInDate());
                    assertEquals(order1.getCheckOutDate(),order2.getCheckOutDate());
                    assertEquals(order1.getPhoneNumber(),order2.getPhoneNumber());
                    assertEquals(order1.getPeopleNum(),order2.getPeopleNum());
                    assertEquals(order1.isHaveChild(),order2.isHaveChild());
                    assertEquals(order1.getPrice(),order2.getPrice(),0.0001);
                    assertEquals(order1.getRoomNum(),order2.getRoomNum());
                    assertEquals(order1.getRoomType(),order2.getRoomType());
                    assertEquals(order1.getOrderState(),order2.getOrderState());
                    assertEquals(order1.isRar(),order2.isRar());
                }
            }
            if(!match){
                fail();//若不能相匹配，说明两个异常订单集合不一样
            }
        }
        //通过存取前后差异比较验证marketerService.getAbnormalOrders取出的异常订单的正确性

    }

    @Test
    public void annulAbnormalOrder() {
        //-----------插入管理人员账号，获取adminId------------
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();

        //---------------插入酒店，获取hotelId----------------
        HotelVO hotelVO = new HotelVO();
        hotelVO.setName("tmpHotel");
        hotelVO.setBizRegion(BizRegion.WangFuJing.toString());
        hotelVO.setHotelStar(HotelStar.Four.toString());
        ResponseVO responseVO = adminService.addHotel(hotelVO,adminId);
        assertTrue(responseVO.getSuccess());
        int hotelId = (Integer)responseVO.getContent();

        //------------------为酒店插入房间----------------
        HotelRoom hotelRoom = new HotelRoom();
        RoomType roomType = RoomType.Family;
        hotelRoom.setRoomType(roomType);
        hotelRoom.setHotelId(hotelId);
        hotelRoom.setCurNum(20);
        hotelRoom.setTotal(20);
        hotelRoom.setPrice(200);
        assertTrue(hotelService.insertRoomInfo(hotelRoom).getSuccess());

        //-------------插入客户并为客户插入会员（初始信用值100，因此初始会员等级为Hamon）--------------
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

        //-----------------插入订单----------------------
        int rightRoomNum = 3;
        int wrongRoomNum = 21;
        double price = 2100;
        OrderVO orderVO = new OrderVO();
        orderVO.setHotelId(hotelId);
        orderVO.setHotelName(hotelVO.getName());
        orderVO.setUserId(user.getId());
        orderVO.setCheckInDate("2060-02-01");
        orderVO.setCheckOutDate("2060-02-04");
        orderVO.setRoomNum(rightRoomNum);
        orderVO.setRoomType(roomType.toString());
        orderVO.setPrice(price);


        //-----------------客户添加订单-----------------
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

    @Test
    public void buyCredit() {
        int testNum = 50;

        ClientLogic clientLogic = new ClientLogic(accountMapper,accountService,adminService);
        VIPLogic vipLogic = new VIPLogic(vipService);
        User user = clientLogic.generateUser(0);
        clientLogic.addUser(user);
        VIPVO vipvo = vipLogic.generateVIPVO(user.getId());
        assertTrue(vipService.registerVIP(vipvo).getSuccess());
        double credit = accountService.getUserInfo(user.getId()).getCredit();
        double percent = 1;

        Random r = new Random();
        for(int i=0;i<testNum;i++){
            int any = Math.abs(r.nextInt())%(8000/testNum)+1;//使得信用值变化可以体现等级变化
            credit += any;

            assertTrue(marketerService.buyCredit(user.getEmail(),any).getSuccess());

            //测试用户的信用随充值数而变化
            assertEquals(credit,accountService.getUserInfo(user.getId()).getCredit(),0.0001);

            //测试用户的会员等级随信用变化
            assertEquals(vipLogic.mapLevel(credit),vipService.getVIPInfo(user.getId()).getLevel());
        }

    }
}
