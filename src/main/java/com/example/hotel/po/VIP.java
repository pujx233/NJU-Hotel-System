/*作业改动 by檀潮*/ //添加
package com.example.hotel.po;

import com.example.hotel.enums.VIPLevel;

import java.time.LocalDateTime;

public class VIP {
    private Integer id;
    private Integer userId;
    private LocalDateTime birthday;
    private double points;
    private VIPLevel level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public VIPLevel getLevel() {
        return level;
    }

    public void setLevel(VIPLevel level) {
        this.level = level;
    }
}
/*作业改动*/
