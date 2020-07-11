package com.example.hotel.bl.hotel;

import com.example.hotel.enums.RoomType;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.ResponseVO;

import java.util.List;

public interface RoomService {

    /**
     * 获取某个酒店的全部房间信息
     * @param hotelId
     * @return
     */
    List<HotelRoom> retrieveHotelRoomInfo(Integer hotelId);

    /**
     * 添加酒店客房信息
     * @param hotelRoom
     */
    void insertRoomInfo(HotelRoom hotelRoom);

    /**
     * 预订酒店或线下入住、离店后更新客房房间数量
     * @param hotelId
     * @param roomType
     * @param roomNum
     */
    void updateRoomInfo(Integer hotelId, String roomType, Integer roomNum) throws ServiceException;

    /**
     * 酒店工作人员修改酒店房间的信息
     * @param hotelRoom
     */
    void updateRoomBase(HotelRoom hotelRoom) throws ServiceException;

    /**
     * 获取酒店指定房间剩余数量
     * @param hotelId
     * @param roomType
     * @return
     */
    int getRoomCurNum(Integer hotelId, String roomType) throws ServiceException;

    /*作业改动 by檀潮*/ //添加
    /**
     * 删除酒店某类型客房
     * @param hotelId
     * @param roomType
     */
    void deleteRoomInfo(Integer hotelId, String roomType) throws ServiceException;
    /*作业改动*/
}
