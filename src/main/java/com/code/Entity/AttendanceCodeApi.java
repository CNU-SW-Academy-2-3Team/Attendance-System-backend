package com.code.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttendanceCodeApi {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gid;
    private LocalDateTime acceptStartTime;
    private LocalDateTime acceptEndTime;


    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer GID) {
        this.gid = GID;
    }


    public LocalDateTime getAcceptStartTime() {
        return acceptStartTime;
    }
    public void setAcceptStartTime(LocalDateTime acceptStartTime) {
        this.acceptStartTime = acceptStartTime;
    }


    public LocalDateTime getAcceptEndTime() {
        return acceptEndTime;
    }
    public void setAcceptEndTime(LocalDateTime acceptEndTime) {
        this.acceptEndTime = acceptEndTime;
    }

}
