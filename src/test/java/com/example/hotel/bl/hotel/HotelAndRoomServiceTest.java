package com.example.hotel.bl.hotel;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.RoomType;
import com.example.hotel.logic.hotel.HotelLogic;
import com.example.hotel.logic.user.AdminLogic;
import com.example.hotel.logic.user.ManagerLogic;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.User;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class HotelAndRoomServiceTest extends BaseTest {//该测试成功发现roomMapper删除房间的筛选条件hotel_id写成id

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AdminService adminService;
    @Autowired
    HotelService hotelService;
    @Autowired
    RoomService roomService;

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

    public void testHotel(int testNum){
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();

        HotelLogic hotelLogic = new HotelLogic(adminService);
        List<HotelVO> hotelVOs = hotelService.retrieveHotels();
        int oldNum = hotelVOs.size();
        List<HotelVO> hotelVOSRecords = hotelLogic.insertHotels(testNum,adminId);
        hotelVOs = hotelService.retrieveHotels();
        assertEquals(oldNum+testNum,hotelVOs.size());
        //用插入数量验证bl层的hotel插入

        for (HotelVO hotelVO : hotelVOSRecords) {
            HotelVO newHotelVO = hotelService.retrieveHotelDetails(hotelVO.getId());
            hotelLogic.assertHotel(hotelVO,newHotelVO);
        }
        //用前后记录对比验证hotelService.retrieveHotelDetails


        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = hotelVOSRecords.get(i);
            HotelDetail hotelDetail = new HotelDetail();
            hotelDetail.setAddress(i*i+"");
            hotelDetail.setPhoneNum(i*i+"");
            hotelDetail.setBizRegion(BizRegion.WangFuJing.toString());
            hotelDetail.setHotelStar(HotelStar.Four.toString());
            assertTrue(hotelService.updateHotelDetail(hotelDetail,hotelVO.getId()).getSuccess());
            HotelVO changedHotelVO = hotelService.retrieveHotelDetails(hotelVO.getId());
            hotelLogic.assertHotelDetail(hotelDetail,changedHotelVO);
        }
        //用前后记录对比验证hotelService.updateHotelDetail


        ManagerLogic managerLogic = new ManagerLogic(accountMapper,adminService,hotelService);
        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = hotelVOSRecords.get(i);
            Integer hotelId = hotelVO.getId();

            //为酒店添加酒店工作人员1，检验其成功
            User user1 = managerLogic.generateUser(i);
            ResponseVO responseVO = hotelService.addHotelManager(hotelId,user1.getId());
            hotelVO = hotelService.retrieveHotelDetails(hotelId);
            assertEquals(user1.getId(),hotelVO.getManagerId());
            assertTrue(responseVO.getSuccess());

            //为酒店添加酒店工作人员2，发现替换
            User user2 = managerLogic.generateUser(i);
            responseVO = hotelService.addHotelManager(hotelId,user1.getId());
            hotelVO = hotelService.retrieveHotelDetails(hotelId);
            assertEquals(user2.getId(),hotelVO.getManagerId());
            assertTrue(responseVO.getSuccess());
        }
        //验证hotelService.addHotelManager

        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = hotelVOSRecords.get(i);
            ResponseVO responseVO = adminService.removeHotel(hotelVO.getId(),adminId);
            assertNull(hotelService.retrieveHotelDetails(hotelVO.getId()));
            assertTrue(responseVO.getSuccess());
        }//为了清理现场完成酒店的删除

        hotelVOs = hotelService.retrieveHotels();
        assertEquals(oldNum,hotelVOs.size());
        //至此，预期剩余数量与实际剩余数量相等

        accountMapper.deleteAccountById(adminId);
        assertNull(accountMapper.getAccountById(adminId));
        //为了清理现场完成管理人员的删除
    }


    //该测试成功发现roomMapper删除房间的筛选条件hotel_id写成id
    public void testRoom(){//因为每个酒店的房间类型不能重复，所以不用testNum以免超出房间类型数
        AdminLogic adminLogic = new AdminLogic(accountMapper);
        int adminId = adminLogic.administrator();

        HotelLogic hotelLogic = new HotelLogic(adminService);
        int hotelId = hotelLogic.addHotel(hotelLogic.generateHotel(),adminId);
        assertEquals(0,roomService.retrieveHotelRoomInfo(hotelId).size());
        assertEquals(0,hotelService.retrieveHotelDetails(hotelId).getRooms().size());
        //初始房间数量为0

        HotelRoom hotelRoom1 = new HotelRoom();
        hotelRoom1.setCurNum(2);
        hotelRoom1.setTotal(3);
        hotelRoom1.setPrice(11);
        RoomType roomType1 = RoomType.Family;
        hotelRoom1.setRoomType(roomType1);
        hotelRoom1.setHotelId(hotelId);
        //新建hotelRoom1

        HotelRoom hotelRoom2 = new HotelRoom();
        hotelRoom2.setCurNum(4);
        hotelRoom2.setTotal(8);
        hotelRoom2.setPrice(14.845);
        hotelRoom2.setRoomType(roomType1);
        hotelRoom2.setHotelId(hotelId);
        //新建hotelRoom2



        //--------------------以下测试roomService有关房间的方法------------------------

        roomService.insertRoomInfo(hotelRoom1);
        List<HotelRoom> roomVOList = roomService.retrieveHotelRoomInfo(hotelId);
        assertEquals(1,roomVOList.size());
        //插入hotelRoom数据检查数量变化，测试roomService.retrieveHotelRoomInfo、roomService.insertRoomInfo

        assertEquals(hotelRoom1.getCurNum(),roomVOList.get(0).getCurNum());
        assertEquals(hotelRoom1.getTotal(),roomVOList.get(0).getTotal());
        assertEquals(hotelRoom1.getPrice(),roomVOList.get(0).getPrice(),0.0001);
        //测试插入的数据与取出的数据一致


        int curNum = -1;
        try{
            roomService.updateRoomInfo(hotelId,roomType1.toString(),-1);//在有效范围内
            curNum = roomService.getRoomCurNum(hotelId,roomType1.toString());
        }catch (Exception e){fail();}
        assertEquals(hotelRoom1.getCurNum()-1,curNum);
        //测试roomService.updateRoomInfo、测试roomService.getRoomCurNum

        try{
            roomService.updateRoomBase(hotelRoom2);
        }catch (Exception e){
            fail();
        }
        roomVOList = roomService.retrieveHotelRoomInfo(hotelId);
        assertEquals(hotelRoom2.getCurNum(),roomVOList.get(0).getCurNum());
        assertEquals(hotelRoom2.getTotal(),roomVOList.get(0).getTotal());
        assertEquals(hotelRoom2.getPrice(),roomVOList.get(0).getPrice(),0.0001);
        //测试roomService.retrieveHotelRoomInfo

        try{
            roomService.deleteRoomInfo(hotelId,roomType1.toString());
        }catch (Exception e){
            fail();
        }
       assertEquals(0,roomService.retrieveHotelRoomInfo(hotelId).size());

        //------------------------以下测试hotelService有关房间的方法---------------------------

        assertTrue(hotelService.insertRoomInfo(hotelRoom2).getSuccess());
        roomVOList = roomService.retrieveHotelRoomInfo(hotelId);
        assertEquals(1,roomVOList.size());
        //插入hotelRoom数据检查数量变化，测试hotelService.retrieveHotelRoomInfo、hotelService.insertRoomInfo

        assertEquals(hotelRoom2.getCurNum(),roomVOList.get(0).getCurNum());
        assertEquals(hotelRoom2.getTotal(),roomVOList.get(0).getTotal());
        assertEquals(hotelRoom2.getPrice(),roomVOList.get(0).getPrice(),0.0001);
        //测试插入的数据与取出的数据一致

        curNum = -1;
        try{
            hotelService.updateRoomInfo(hotelId,roomType1.toString(),+1);//在有效范围内
            curNum = hotelService.getRoomCurNum(hotelId,roomType1.toString());
        }catch (Exception e){fail();}
        assertEquals(hotelRoom2.getCurNum()+1,curNum);
        //测试hotelService.updateRoomInfo、测试hotelService.getRoomCurNum


        try{
            roomService.deleteRoomInfo(hotelId,roomType1.toString());
        }catch (Exception e){
            fail();
        }
        assertEquals(0,hotelService.retrieveHotelDetails(hotelId).getRooms().size());
        //清理现场
    }

    @Test
    public void testWithHotel(){
        testHotel(17);
    }

    @Test
    public void testWithRoom(){
        testRoom();
    }
}
