package com.example.hotel.data.hotel;

import com.example.hotel.data.hotel.HotelMapper;

import com.example.hotel.data.hotel.RoomMapper;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.RoomType;
import com.example.hotel.enums.ServiceOfHotel;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.Hotel;
import com.example.hotel.util.BaseTest;
import com.example.hotel.util.RunSql;
import com.example.hotel.vo.HotelVO;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;


public class HotelAndRoomMapperTest extends BaseTest {
    @Autowired
    HotelMapper hotelMapper;
    @Autowired
    RoomMapper roomMapper;

    public void assertHotel(Hotel hotel,HotelVO newHotel){
        assertEquals(hotel.getHotelName(),newHotel.getName());
        assertEquals(hotel.getAddress(),newHotel.getAddress());

        //存入时用enum存入，存入的是enum名，取出时若用enum取出即可正确赋值，但HotelVO的该属性为String，所以需要转化
        assertEquals(hotel.getBizRegion(),BizRegion.valueOf(newHotel.getBizRegion()));
        assertEquals(hotel.getHotelStar(),HotelStar.valueOf(newHotel.getHotelStar()));

        assertEquals(hotel.getRate(),newHotel.getRate());
        assertEquals(hotel.getHotelService(),newHotel.getHotelService());
        assertEquals(hotel.getDescription(),newHotel.getDescription());
        assertEquals(hotel.getPhoneNum(),newHotel.getPhoneNum());
        assertEquals(hotel.getManagerId(),newHotel.getManagerId());
    }

    public void testHotel(int testNum){
        List<HotelVO> hotels = hotelMapper.selectAllHotel();
        int oldNum = hotels.size();

        List<Hotel> hotelList = new ArrayList<>();
        Random r = new Random();
        for(int i=0;i<testNum;i++){
            Hotel hotel = new Hotel();
            hotel.setHotelService(String.valueOf(ServiceOfHotel.HotWater));
            hotel.setAddress("Nanjing");
            hotel.setBizRegion(BizRegion.WangFuJing);
            hotel.setDescription("test");
            hotel.setHotelName("test"+i);
            hotel.setHotelStar(HotelStar.Four);
            hotel.setPhoneNum((r.nextInt(10000)*i*i%10000)+"133");
            hotel.setRate(5.0);
            hotelMapper.insertHotel(hotel);
            hotelList.add(hotel);
        }
        hotels = hotelMapper.selectAllHotel();
        assertEquals(oldNum+testNum,hotels.size());
        //用插入数量验证hotelMapper.insertHotel、hotelMapper.selectAllHotel

        for(int i=0;i<hotelList.size();i++){
            Hotel hotel = hotelList.get(i);
            HotelVO newHotel = hotelMapper.selectById(hotel.getId());
            assertHotel(hotel,newHotel);
        }//用前后记录对比验证hotelMapper.selectById


        for(int i=0;i<hotelList.size();i++){
            Hotel oldhotel = hotelList.get(i);

            Hotel hotel = new Hotel();
            hotel.setHotelService(String.valueOf(ServiceOfHotel.WiFi));
            hotel.setAddress("Beijing");
            hotel.setBizRegion(BizRegion.DaZhaLan);
            hotel.setDescription("description");
            hotel.setHotelName(oldhotel.getHotelName());
            hotel.setHotelStar(HotelStar.Three);
            hotel.setPhoneNum((r.nextInt(10000)*i*i%10000)+"133");
            hotel.setRate(oldhotel.getRate());

            hotelMapper.updateHotelDetail(oldhotel.getId(),hotel.getAddress(),hotel.getBizRegion(),
                    hotel.getHotelStar(),hotel.getHotelService(),hotel.getDescription(),hotel.getPhoneNum());

            HotelVO newHotel = hotelMapper.selectById(oldhotel.getId());
            assertHotel(hotel,newHotel);
        }//用前后记录对比验证hotelMapper.updateHotelDetail

        for(int i=0;i<hotelList.size();i++){
            Hotel oldhotel = hotelList.get(i);
            int newId = (int)(new Random(i*i+400).nextInt())%100000;//限制在10万以内
            hotelMapper.updateHotelManager(hotelList.get(i).getId(),newId);
            HotelVO hotelVO = hotelMapper.selectById(hotelList.get(i).getId());
            assertEquals((Integer)newId,hotelVO.getManagerId());
        }//用前后记录对比验证hotelMapper.updateHotelManager

        for(int i=0;i<hotelList.size();i++){
            Hotel oldhotel = hotelList.get(i);
            hotelMapper.deleteHotel(oldhotel.getId());
            assertNull(hotelMapper.selectById(oldhotel.getId()));
        }//用前后记录对比验证hotelMapper.deleteHotel

        hotels = hotelMapper.selectAllHotel();
        assertEquals(oldNum,hotels.size());
        //至此，预期剩余数量与实际剩余数量相等
    }

    public void testRoom(int testNum){
        int hotelId = 5;//任意不违背数据库的hotelId

        List<HotelRoom> rooms = roomMapper.selectRoomsByHotelId(hotelId);
        for(HotelRoom room:rooms){
            roomMapper.deleteRoom(hotelId,room.getRoomType());
        }
        rooms = roomMapper.selectRoomsByHotelId(hotelId);
        assertEquals(0,rooms.size());
        //清空原有room避免影响后续步骤，测试roomMapper.deleteRoom

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


        roomMapper.insertRoom(hotelRoom1);
        List<HotelRoom> roomVOList = roomMapper.selectRoomsByHotelId(hotelId);
        assertEquals(1,roomVOList.size());
        //插入hotelRoom数据检查数量变化，测试roomMapper.insertRoom、roomMapper.selectRoomsByHotelId

        assertEquals(hotelRoom1.getCurNum(),roomVOList.get(0).getCurNum());
        assertEquals(hotelRoom1.getTotal(),roomVOList.get(0).getTotal());
        assertEquals(hotelRoom1.getPrice(),roomVOList.get(0).getPrice(),0.0001);
        //测试插入的数据与取出的数据一致


        roomMapper.updateRoomInfo(hotelId,roomType1,-1);//在有效范围内
        assertEquals(hotelRoom1.getCurNum()-1,roomMapper.getRoomCurNum(hotelId,roomType1));
        //测试roomMapper.updateRoomInfo、roomMapper.getRoomCurNum

        roomMapper.updateRoomBase(hotelRoom2);
        roomVOList = roomMapper.selectRoomsByHotelId(hotelId);
        assertEquals(hotelRoom2.getCurNum(),roomVOList.get(0).getCurNum());
        assertEquals(hotelRoom2.getTotal(),roomVOList.get(0).getTotal());
        assertEquals(hotelRoom2.getPrice(),roomVOList.get(0).getPrice(),0.0001);
        //测试roomMapper.updateRoomBase

        roomMapper.deleteRoom(hotelId,roomType1);
        assertEquals(0,roomMapper.selectRoomsByHotelId(hotelId).size());
        //清理现场，测试roomMapper.deleteRoom、roomMapper.selectRoomsByHotelId
    }

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("--init start--");
        RunSql.runsqlBySpringUtils("sql\\hotel.sql");
        System.out.println("--init end--");
    }


    @Test
    public void testWithHotel(){
        testHotel(23);
    }

    @Test
    public void testWithRoom(){
        testRoom(23);
    }
}