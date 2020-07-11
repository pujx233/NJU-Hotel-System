package com.example.hotel.blImpl.hotel;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.hotel.RoomService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.hotel.HotelMapper;
import com.example.hotel.enums.*;
import com.example.hotel.po.Hotel;
import com.example.hotel.po.HotelRoom;
import com.example.hotel.po.User;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.HotelDetail;
import com.example.hotel.vo.HotelVO;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {
    private final static String ADD_MANAGER_FAIL = "为酒店添加工作人员任务中出错";

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private AccountService accountService;

    /*作业改动 by檀潮*/ //删除
//    @Autowired
//    private OrderService orderService;
    /*作业改动*/

    @Autowired
    private RoomService roomService;

    @Override//暂时不提供hotel名字不重复功能，版权问题让商家们线下争吵
    public Integer addHotel(HotelVO hotelVO) throws ServiceException {
        if(hotelVO.getName()==null){
            throw new ServiceException("酒店名不能为空！");
        }
        Hotel hotel = new Hotel();//以及传入的hotelVO并不会被赋值id，被赋值的是里面的hotel
        hotel.setDescription(hotelVO.getDescription());
        hotel.setAddress(hotelVO.getAddress());
        hotel.setHotelName(hotelVO.getName());
        hotel.setPhoneNum(hotelVO.getPhoneNum());
        hotel.setManagerId(null);//确保将hotelVO.getManagerId()置为null（既然是由网站管理人员添加酒店，则managerId应空悬）
        hotel.setRate(5.0);//新加的酒店从5分开始
        BizRegion bizRegion = BizRegion.parse(hotelVO.getBizRegion());
        if(hotelVO.getBizRegion()!=null&&bizRegion==null){
            throw new ServiceException("商圈格式不正确！");
        }
        HotelStar hotelStar = HotelStar.parse(hotelVO.getHotelStar());
        if(hotelVO.getHotelStar()!=null&&hotelStar==null){
            throw new ServiceException("星级格式不正确！");
        }
        hotel.setBizRegion(bizRegion);
        hotel.setHotelStar(hotelStar);
        if(hotelVO.getHotelService()!=null){
            String[] service = hotelVO.getHotelService().split(",");
            for (String s : service) {
                if (ServiceOfHotel.parse(s) == null) {
                    throw new ServiceException("设施服务格式不正确！");
                }
            }
            hotel.setHotelService(hotelVO.getHotelService());
        }else {
            hotel.setHotelService(null);
        }
        hotelMapper.insertHotel(hotel);
        return hotel.getId();
    }
    //依赖data.hotel.HotelMapper.insertHotel(Hotel hotel)

    @Override
    public int removeHotel(Integer hotelId) {
        return hotelMapper.deleteHotel(hotelId);
    }
    //依赖data.hotel.HotelMapper.deleteHotel(Intrger hotelId)

    @Override
    public void updateRoomInfo(Integer hotelId, String roomType, Integer roomNum) throws ServiceException{//rooms正加负减
        roomService.updateRoomInfo(hotelId,roomType,roomNum);
    }
    //依赖RoomService.updateRoomInfo(Integer hotelId, String roomType, Integer rooms)

    @Override
    public int getRoomCurNum(Integer hotelId, String roomType) throws ServiceException{//因为是在后端逻辑内部调用，故而此处不做检查
        return roomService.getRoomCurNum(hotelId,roomType);
    }
    //依赖roomService.getRoomCurNum(nteger hotelId, String roomType)

    @Override
    public ResponseVO addHotelManager(Integer hotelId, Integer managerId) {
        //被blImpl.AdminServiceImpl.addManager调用来实现酒店工作人员所属酒店的managerId的添加
        try{
            hotelMapper.updateHotelManager(hotelId,managerId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure(ADD_MANAGER_FAIL);
        }
        return ResponseVO.buildSuccess(managerId);
    }
    //依赖data.hotel.HotelMapper.updateHotelManager(Integer hotelId, Integer managerId)

    @Override
    public ResponseVO updateHotelDetail(HotelDetail hotelDetail,int hotelId) {
        try {
            BizRegion bizRegion = BizRegion.parse(hotelDetail.getBizRegion());
            if(hotelDetail.getBizRegion()!=null&&bizRegion==null){
                throw new ServiceException("商圈格式不正确！");
            }
            HotelStar hotelStar = HotelStar.parse(hotelDetail.getHotelStar());
            if(hotelDetail.getHotelStar()!=null&&hotelStar==null){
                throw new ServiceException("星级格式不正确！");
            }
            if(hotelDetail.getHotelService()!=null){
                String[] service = hotelDetail.getHotelService().split(",");
                for (String s : service) {
                    if (ServiceOfHotel.parse(s) == null) {
                        throw new ServiceException("设施服务格式不正确！");
                    }
                }
            }
            hotelMapper.updateHotelDetail(hotelId,hotelDetail.getAddress(),bizRegion, hotelStar,
                    hotelDetail.getHotelService(),hotelDetail.getDescription(),hotelDetail.getPhoneNum());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.buildFailure("修改酒店详细信息任务中出现错误: "+e.getMessage());
        }
        return ResponseVO.buildSuccess(true);
    }
    //依赖data.hotel.HotelMapper.updateHotelDetail(Integer hotelId, String address,
    // String bizRegion, String hotelStar, String hotelService, String description, String phoneNum)

    @Override
    public ResponseVO insertRoomInfo(HotelRoom hotelRoom) {
        //若rooms中存在roomType与hotelRoom.getRoomType()相同的项，则返回ResponseVO.buildFailure(ROOM_EXIST)
        //否则调用roomService.insertRoomInfo(hotelRoom)，返回ResponseVO.buildSuccess(true)
        try{
            if(hotelRoom.getCurNum()<0||hotelRoom.getTotal()<hotelRoom.getCurNum()){
                throw new ServiceException("房间数量信息不合理");
            }
            HotelVO hotelVO = hotelMapper.selectById(hotelRoom.getHotelId());
            if(hotelVO==null){
                throw new ServiceException("酒店不存在");
            }
            List<HotelRoom> rooms = roomService.retrieveHotelRoomInfo(hotelRoom.getHotelId());
            RoomType roomType = hotelRoom.getRoomType();
            for(int i=0;i<rooms.size();i++){
                if(rooms.get(i).getRoomType().equals(roomType)){
                    throw new ServiceException("房间类型已存在");
                }
            }
            roomService.insertRoomInfo(hotelRoom);
            return ResponseVO.buildSuccess(hotelRoom.getId());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("添加房间任务中发生错误: "+e.getMessage());
        }
    }
    //依赖data.hotel.HotelMapper.selectById(Integer id)查找酒店
    //依赖RoomService.retrieveHotelRoomInfo(Integer hotelId)
    //依赖RoomService.insertRoomInfo(HotelRoom hotelRoom)

    @Override
    public List<HotelVO> retrieveHotels() {
        List<HotelVO> hotelVOS = hotelMapper.selectAllHotel();
        for(HotelVO hotelVO:hotelVOS){
            hotelVO.setBizRegion(BizRegion.valueOf(hotelVO.getBizRegion()).toString());
            hotelVO.setHotelStar(HotelStar.valueOf(hotelVO.getHotelStar()).toString());
        }//将hotelVO中枚举类的部分从枚举变量名转换为toString()后的值
        return hotelVOS;
    }
    //依赖data.hotel.HotelMapper.selectAllHotel()

    @Override
    public HotelVO retrieveHotelDetails(Integer hotelId) {
        HotelVO hotelVO = hotelMapper.selectById(hotelId);
        if(hotelVO==null){
            return null;
        }
        hotelVO.setBizRegion(BizRegion.valueOf(hotelVO.getBizRegion()).toString());
        hotelVO.setHotelStar(HotelStar.valueOf(hotelVO.getHotelStar()).toString());
        List<HotelRoom> rooms = roomService.retrieveHotelRoomInfo(hotelId);
        List<RoomVO> roomVOS = rooms.stream().map(r -> {
            RoomVO roomVO = new RoomVO();
            roomVO.setId(r.getId());
            roomVO.setPrice(r.getPrice());
            roomVO.setRoomType(r.getRoomType().toString());
            roomVO.setCurNum(r.getCurNum());
            roomVO.setTotal(r.getTotal());
            return roomVO;
        }).collect(Collectors.toList());
        hotelVO.setRooms(roomVOS);

        return hotelVO;
    }
    //依赖data.hotel.HotelMapper.selectById(Integer id)
    //依赖RoomService.retrieveHotelRoomInfo(Integer hotelId)


}
