/*作业改动 by檀潮*/ //添加
package com.example.hotel.vo;

import com.example.hotel.enums.UserType;
import com.example.hotel.enums.VIPLevel;

import java.time.LocalDateTime;

public class VIPVO {
    private Integer userId;
    private LocalDateTime birthday;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }


}
/*作业改动*/
