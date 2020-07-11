package com.example.hotel.data.hotel;

import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.po.Hotel;
import com.example.hotel.po.User;
import com.example.hotel.vo.HotelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HotelMapper {

    int insertHotel(Hotel hotel);

    //因为返回值是枚举类变String的VO，故bl层在取得后要做一层转化，即那些方法依赖于这个返回值类型
    List<HotelVO> selectAllHotel();

    //因为返回值是枚举类变String的VO，故bl层在取得后要做一层转化，即那些方法依赖于这个返回值类型
    HotelVO selectById(@Param("id") Integer id);

    /*作业改动 by檀潮*/ //添加
    int updateHotelManager(@Param("hotelId") Integer hotelId,@Param("managerId") Integer managerId);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    int updateHotelDetail(@Param("hotelId")Integer hotelId, @Param("address") String address,
                          @Param("bizRegion") BizRegion bizRegion, @Param("hotelStar") HotelStar hotelStar,
                          @Param("hotelService") String hotelService, @Param("description") String description,
                          @Param("phoneNum") String phoneNum);
    /*作业改动*/

    /*作业改动 by檀潮*/ //添加
    int deleteHotel(@Param("hotelId") Integer hotelId);
    /*作业改动*/
}
