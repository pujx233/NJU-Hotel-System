package com.example.hotel.data.hotel;

import com.example.hotel.enums.RoomType;
import com.example.hotel.po.Hotel;
import com.example.hotel.po.HotelRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoomMapper {

    int updateRoomInfo(@Param("hotelId") Integer hotelId, @Param("roomType") RoomType roomType,
                       @Param("curNum") Integer curNum);//curCum正加负减

    int updateRoomBase(HotelRoom hotelRoom);

    int insertRoom(HotelRoom hotelRoom);

    List<HotelRoom> selectRoomsByHotelId(@Param("hotelId") Integer hotelId);

    int getRoomCurNum(@Param("hotelId") Integer hotelId,@Param("roomType") RoomType roomType);

    /*作业改动 by檀潮*/ //添加
    int deleteRoom(@Param("hotelId") Integer hotelId,@Param("roomType") RoomType roomType);
    /*作业改动*/
}
