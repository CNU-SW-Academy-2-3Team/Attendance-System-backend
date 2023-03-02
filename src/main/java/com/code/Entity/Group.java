package com.code.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import java.sql.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//Group Table과 Mapping 됨
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer GID;
    private String invite_code;
    private String group_title;
    private String group_detail;
    private Date create_date;
    private Integer master_uid;
    private Integer head_count;
    private String attendance_code;




    public Integer getGID() {
        return GID;
    }

    public void setGID(Integer GID) {
        this.GID = GID;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }

    public String getGroup_detail() {
        return group_detail;
    }

    public void setGroup_detail(String group_detail) {
        this.group_detail = group_detail;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Integer getMaster_uid() {
        return master_uid;
    }

    public void setMaster_uid(Integer master_uid) {
        this.master_uid = master_uid;
    }

    public Integer getHead_count() {
        return head_count;
    }

    public void setHead_count(Integer head_count) {
        this.head_count = head_count;
    }

    public String getAttendance_code() {
        return attendance_code;
    }

    public void setAttendance_code(String attendance_code) {
        this.attendance_code = attendance_code;
    }


}
