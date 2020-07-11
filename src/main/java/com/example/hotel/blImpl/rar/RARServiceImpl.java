package com.example.hotel.blImpl.rar;

import com.example.hotel.bl.rar.RARService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.rar.RARMapper;
import com.example.hotel.enums.OrderState;
import com.example.hotel.po.Judgment;
import com.example.hotel.po.Order;
import com.example.hotel.util.ServiceException;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RARServiceImpl implements RARService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    RARMapper rarMapper;

    @Override
    public ResponseVO rateAndReview(Integer orderId, double rate, String review) {
        try{
            Order order = orderMapper.getOrderById(orderId);
            if(order==null||!order.getOrderState().equals(OrderState.Executed.toString())){
                throw new ServiceException("未正确输入已执行订单哦");
            }
            if(order.isRar()){
                throw new ServiceException("该订单已被评论过哦");
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime outTime = LocalDateTime.parse(order.getCheckOutDate()+" 00:00:00",df);
            LocalDateTime now = LocalDateTime.now();
            if(now.compareTo(outTime)<0){
                throw new ServiceException("只能在离店后评订单哦");
            }
            if(rate<0||rate>5){
                throw new ServiceException("评分不是有效的评分哦");
            }
            Judgment judgment = new Judgment();
            judgment.setOrderId(orderId);
            judgment.setUserId(order.getUserId());
            judgment.setHotelId(order.getHotelId());
            judgment.setRate((int)(rate*10+0.5)/10.0);
            judgment.setComment(review);
            judgment.setCreateDate(df.format(now));
            rarMapper.insertRAR(judgment);
            orderMapper.orderRAR(orderId);
            calHotelRate(order.getHotelId());
            return ResponseVO.buildSuccess(judgment.getId());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("添加评论任务中发生错误: "+e.getMessage());
        }
    }
    //依赖data.order.OrderMapper.getOrderById(int orderid)获取userId,hotelId（与orderState：若前端不负责判断是否为已执行订单，那么后端要负责判断）
    //依赖data.rar.RARMapper.insertRAR(Judgment judgment)存储Judgment
    //依赖data.order.orderRAR(int orderId)将订单置为已评论
    //依赖RARService.calHotelRate(Integer hotelId)更新hotel的rate

    @Override
    public void calHotelRate(Integer hotelId) {
        int defaultUser = 100;

        //用getHotelRAR获取List<Judgment>
        //求所有Judgment的rate的平均avg，调用RARMapper.updateHotelRate(hotelId,avg)更新rate（该方法是为数据库rate项直接赋值avg，并非加减avg)
        List<Judgment> judgments = getHotelRAR(hotelId);
        if(judgments==null||judgments.isEmpty())return;
        double rate = 0;
        for(int i=0;i<judgments.size();i++){
            rate += judgments.get(i).getRate();
        }

        //默认初始有100个打5分的人，避免评分刚开始波动过大，影响外界对酒店的判断
        double result = (rate+5*defaultUser)/(judgments.size()+defaultUser);

        rarMapper.updateHotelRate(hotelId,(int)(result*10+0.5)/10.0);
    }
    //依赖RARService.getHotelRAR
    //依赖data.rar.RARMapper.updateHotelRate(Integer hotelId, Double rate)


    @Override
    public List<Judgment> getHotelRAR(Integer hotelId) {
        return rarMapper.selectRARByHotelId(hotelId);
    }
    //依赖data.rar.RARMapper.selectRARByHotelId(Integer hotelId)
}
