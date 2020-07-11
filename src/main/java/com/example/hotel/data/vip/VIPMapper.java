/*作业改动 by檀潮*/ //添加
package com.example.hotel.data.vip;

import com.example.hotel.enums.VIPLevel;
import com.example.hotel.po.VIP;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VIPMapper {

    int createNewVIP(VIP vip);

    VIP getVIPByUserId(@Param("userId") int userId);

    int setVIPLevel(@Param("vipId") int vipId,@Param("level") VIPLevel level);

//    /*作业改动 by檀潮*/ //添加
//    int deleteById(@Param("id") Integer id);
//    /*作业改动*/

}
/*作业改动*/
