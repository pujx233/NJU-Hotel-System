package com.example.hotel.bl.hotel;

import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.Order;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.HotelDetail;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.HotelVO;

import java.util.List;

public interface HotelService {

    /**
     * 添加酒店并返回酒店hotelId
     * @param hotelVO
     */
    Integer addHotel(HotelVO hotelVO) throws ServiceException ;

    /*作业改动 by檀潮*/ //添加
    /**
     * 删除酒店
     * @param hotelId
     */
    int removeHotel(Integer hotelId) ;
    /*作业改动*/


    /**
     * 预订酒店或线下入住、离店后修改剩余客房信息
     * @param hotelId
     * @param roomType
     * @param roomNum
     */
    void updateRoomInfo(Integer hotelId, String roomType,Integer roomNum) throws ServiceException;

    /**
     * 列表获取酒店信息
     * @return
     */
    List<HotelVO> retrieveHotels();

    /**
     * 获取某家酒店详细信息
     * @param hotelId
     * @return
     */
    HotelVO retrieveHotelDetails(Integer hotelId);

    /**
     * 查看酒店剩余某种房间数量
     * @param hotelId
     * @param roomType
     * @return
     */
    int getRoomCurNum(Integer hotelId,String roomType) throws ServiceException;

    /*作业改动 by檀潮*/ //添加
    /**
     * 修改酒店的managerId
     * @param hotelId
     * @param managerId
     * @return
     */
    ResponseVO addHotelManager(Integer hotelId, Integer managerId);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 修改酒店信息
     * @param hotelDetail
     * @param hotelId
     * @return
     */
    ResponseVO updateHotelDetail(HotelDetail hotelDetail,int hotelId);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    /**
     * 添加酒店客房信息
     * @param hotelRoom
     * @return
     */
    ResponseVO insertRoomInfo(HotelRoom hotelRoom);
    /*作业改动*/
}
