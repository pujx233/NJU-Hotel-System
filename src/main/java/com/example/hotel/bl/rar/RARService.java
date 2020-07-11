/*作业改动 by檀潮*/ //添加
package com.example.hotel.bl.rar;

import com.example.hotel.po.Judgment;
import com.example.hotel.vo.ResponseVO;

import java.util.List;

public interface RARService {

    /**
     * 为每个订单添加评论
     * @param orderId
     * @param rate
     * @param review
     * @return
     */
    ResponseVO rateAndReview(Integer orderId, double rate, String review);

    /**
     * 更新酒店评分
     * @param hotelId
     * @return
     */
    void calHotelRate(Integer hotelId);

    /**
     * 获取酒店的评论列表
     * @param hotelId
     * @return
     */
    List<Judgment> getHotelRAR(Integer hotelId);
}
/*作业改动*/