package com.example.hotel.controller.hotel;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.HotelDetail;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.RoomKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {
    private final static String DELETEROOM_ERROR = "删除房间失败";

    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;


    @GetMapping("/all")
    public ResponseVO retrieveAllHotels(){
        return ResponseVO.buildSuccess(hotelService.retrieveHotels());
    }


    @PostMapping("/roomInfo/add")
    public ResponseVO addRoomInfo(@RequestBody HotelRoom hotelRoom) {
        return hotelService.insertRoomInfo(hotelRoom);
    }

    @GetMapping("/{hotelId}/detail/retrieve")
    public ResponseVO retrieveHotelDetail(@PathVariable Integer hotelId) {
        return ResponseVO.buildSuccess(hotelService.retrieveHotelDetails(hotelId));
    }

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/{hotelId}/detail/update")
    public ResponseVO updateHotelDetail(@RequestBody HotelDetail hotelDetail,@PathVariable int hotelId){
        return hotelService.updateHotelDetail(hotelDetail,hotelId);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/{roomNum}/roomInfo/updateNum")
    public ResponseVO updateRoomInfo(@PathVariable Integer roomNum,
                                     @RequestBody RoomKey roomKey){
        try{
            hotelService.updateRoomInfo(roomKey.getHotelId(), roomKey.getRoomType(), roomNum);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("更新房间数量任务中发生错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/roomInfo/remove")
    public ResponseVO removeRoomInfo(@RequestBody RoomKey roomKey){
        try{
            roomService.deleteRoomInfo(roomKey.getHotelId(), roomKey.getRoomType());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(DELETEROOM_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    @PostMapping("/roomInfo/updateBase")
    public ResponseVO updateRoomBase(@RequestBody HotelRoom hotelRoom){
        try{
            roomService.updateRoomBase(hotelRoom);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("在更新房间信息任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    /*作业改动*/

}
