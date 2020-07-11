package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.data.hotel.RoomMapper;
import com.example.hotel.enums.RoomType;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    private final static String ROOMTYPE_NOT_EXIST = "房间类型不存在";

    @Autowired
    private RoomMapper roomMapper;

    @Override
    public List<HotelRoom> retrieveHotelRoomInfo(Integer hotelId) {
        return roomMapper.selectRoomsByHotelId(hotelId);
    }
    //依赖data.hotel.RoomMapper.selectRoomsByHotelId(Integer hotelId)

    @Override
    public void insertRoomInfo(HotelRoom hotelRoom) {
        roomMapper.insertRoom(hotelRoom);
    }
    //依赖data.hotel.RoomMapper.insertRoom(HotelRoom hotelRoom)

    @Override
    public void updateRoomInfo(Integer hotelId, String roomType, Integer roomNum) throws ServiceException {//rooms正加负减
        RoomType type = RoomType.parse(roomType);
        if(type==null){
            throw new ServiceException(roomType+"的"+ROOMTYPE_NOT_EXIST);
        }
        List<HotelRoom> rooms = retrieveHotelRoomInfo(hotelId);
        for(HotelRoom hotelRoom:rooms){
            if(hotelRoom.getRoomType().toString().equals(roomType)){
                int num = getRoomCurNum(hotelId,roomType);
                if(num+roomNum<0||num+roomNum>hotelRoom.getTotal()){
                    throw new ServiceException("房间更新的数量不合理！");
                }
                break;
            }
        }
        roomMapper.updateRoomInfo(hotelId,type,roomNum);
    }
    //依赖RoomService.retrieveHotelRoomInfo(Integer hotelId)
    //依赖data.hotel.RoomMapper.updateRoomInfo(Integer hotelId, String roomType, Integer curNum)

    @Override
    public void updateRoomBase(HotelRoom hotelRoom) throws ServiceException{
        if(hotelRoom.getCurNum()<0||hotelRoom.getTotal()<0||hotelRoom.getCurNum()>hotelRoom.getTotal()
        ||hotelRoom.getPrice()<0){
            throw new ServiceException("无效的房间更新信息！");
        }
        hotelRoom.setPrice((int)(hotelRoom.getPrice()*100+0.5)/100.0);
        roomMapper.updateRoomBase(hotelRoom);
    }
    //依赖data.hotel.RoomMapper.updateRoomBase(HotelRoom hotelRoom)

    @Override
    public int getRoomCurNum(Integer hotelId, String roomType) throws ServiceException {
        RoomType type = RoomType.parse(roomType);
        if(type==null){
            throw new ServiceException(roomType+"的"+ROOMTYPE_NOT_EXIST);
        }
        return roomMapper.getRoomCurNum(hotelId,type);
    }
    //依赖data.hotel.RoomMapper.getRoomCurNum(Integer hotelId, String roomType)

    @Override
    public void deleteRoomInfo(Integer hotelId, String roomType) throws ServiceException {
        RoomType type = RoomType.parse(roomType);
        if(type==null){
            throw new ServiceException(roomType+"的"+ROOMTYPE_NOT_EXIST);
        }
        roomMapper.deleteRoom(hotelId,type);
    }
    //依赖data.hotel.RoomMapper.deleteRoom(Integer hotelId, String roomType)

}
