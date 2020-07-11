/*作业改动 by檀潮*/ //添加
package com.example.hotel.data.rar;

import com.example.hotel.po.Judgment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RARMapper {

    int insertRAR(Judgment judgment);

    int updateHotelRate(@Param("hotelId") Integer hotelId,@Param("rate") Double rate);//此方法直接赋值新的rate，并非在原基础上加减rate

    List<Judgment> selectRARByHotelId(@Param("hotelId") Integer hotelId);

//    /*作业改动 by檀潮*/ //添加
//    int deleteById(@Param("id") Integer id);
//    /*作业改动*/
}
/*作业改动*/
