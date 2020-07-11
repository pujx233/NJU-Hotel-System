/*作业改动 by檀潮*/ //添加
package com.example.hotel.data.marketer;

import com.example.hotel.po.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MarketerMapper {

    List<Order> getAllAbnormalOrders();

}
/*作业改动*/












