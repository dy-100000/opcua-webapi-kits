package org.opcfoundation.uawebservicedemo.database.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class EmployeeCheckIn {
    private Integer id;
    private LocalDateTime time;
    private Boolean checkIn;
    private String location;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Boolean getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Boolean checkIn) {
        this.checkIn = checkIn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString()
    {
        String ret = "";
        ret += "Id: ";
        ret += id;

        ret += " Time: ";
        ret += time;

        ret += "  CheckIn: ";
        ret += checkIn;

        ret += "  Location: ";
        ret += location;

        ret += "  Remark: ";
        ret += remark;

        return ret;
    }
}
