package com.example.hotel.logic.hotel;

import com.example.hotel.bl.admin.AdminService;
import com.example.hotel.enums.BizRegion;
import com.example.hotel.enums.HotelStar;
import com.example.hotel.enums.ServiceOfHotel;
import com.example.hotel.logic.user.AdminLogic;
import com.example.hotel.po.User;
import com.example.hotel.vo.HotelDetail;
import com.example.hotel.vo.HotelVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HotelLogic{
    AdminService adminService;
    public HotelLogic(AdminService adminService){
        this.adminService = adminService;
    }
    public Integer addHotel(HotelVO hotelVO, int adminId){
        //由于测试中选择在别处直接检验adminService的addHotel的responseVO，故此处无需检验responseVO，直接返回id即可
        return (Integer) adminService.addHotel(hotelVO,adminId).getContent();
    }
    public List<HotelVO> insertHotels(int testNum,int adminId){
        List<HotelVO> hotelRecords = new ArrayList<>();
        for(int i=0;i<testNum;i++){
            HotelVO hotelVO = generateHotel();
            hotelVO.setId(addHotel(hotelVO,adminId));
            hotelRecords.add(hotelVO);
        }
        return hotelRecords;
    }
    public void removeHotel(int hotelId, int adminId){
        //由于测试中选择在别处直接检验adminService的addHotel的responseVO，故此处无需检验responseVO
        adminService.removeHotel(hotelId,adminId);
    }
    public int hotel(int adminId){
        return (Integer)adminService.addHotel(generateHotel(),adminId).getContent();
    }
    public HotelVO generateHotel(){
        return generateHotel(0);
    }
    public HotelVO generateHotel(int flag){
        HotelVO hotelVO = new HotelVO();
        hotelVO.setName((flag*flag%10000)+"tmpHotel");//暂时不提供hotel名字不重复功能，版权问题让商家们线下争吵
        Random r = new Random();
        hotelVO.setBizRegion(BizRegion.WangFuJing.toString());
        hotelVO.setHotelStar(HotelStar.Five.toString());
        int any = (r.nextInt()*flag*flag)%3;
        if(any==0){
            hotelVO.setRate(-3.0);
            hotelVO.setBizRegion(BizRegion.DaZhaLan.toString());
            hotelVO.setHotelStar(HotelStar.Four.toString());
            hotelVO.setHotelService(ServiceOfHotel.HotWater+","+ServiceOfHotel.WakeUp);
        }else if(any==1){
            hotelVO.setRate(9.2);
            hotelVO.setBizRegion(BizRegion.XiDan.toString());
            hotelVO.setHotelStar(HotelStar.Three.toString());
            hotelVO.setHotelService(ServiceOfHotel.WiFi.toString());
        }
        hotelVO.setRate(5.0);//酒店评分初始值为5
        return hotelVO;
    }
    public void assertHotel(HotelVO oldHotelVO,HotelVO newHotelVO){
        assertEquals(oldHotelVO.getName(),newHotelVO.getName());
        assertEquals(oldHotelVO.getAddress(),newHotelVO.getAddress());
        assertEquals(oldHotelVO.getBizRegion(),newHotelVO.getBizRegion());
        assertEquals(oldHotelVO.getHotelStar(),newHotelVO.getHotelStar());
        assertEquals(oldHotelVO.getRate(),newHotelVO.getRate());
        assertEquals(oldHotelVO.getHotelService(),newHotelVO.getHotelService());
        assertEquals(oldHotelVO.getDescription(),newHotelVO.getDescription());
        assertEquals(oldHotelVO.getPhoneNum(),newHotelVO.getPhoneNum());
        assertEquals(oldHotelVO.getManagerId(),newHotelVO.getManagerId());
    }
    public void assertHotelDetail(HotelDetail hotelDetail,HotelVO hotelVO){
        assertEquals(hotelDetail.getAddress(),hotelVO.getAddress());
        assertEquals(hotelDetail.getBizRegion(),hotelVO.getBizRegion());
        assertEquals(hotelDetail.getHotelStar(),hotelVO.getHotelStar());
        assertEquals(hotelDetail.getHotelService(),hotelVO.getHotelService());
        assertEquals(hotelDetail.getDescription(),hotelVO.getDescription());
        assertEquals(hotelDetail.getPhoneNum(),hotelVO.getPhoneNum());
    }
}
