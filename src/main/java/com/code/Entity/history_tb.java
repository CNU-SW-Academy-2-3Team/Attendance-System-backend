package com.code.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
//Group Table과 Mapping 됨
public class history_tb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hid;
    private Integer guid;
    private LocalDateTime enter_time;
    private LocalDateTime exit_time;
    private Character attendance_state;
    private Integer cid;
    private LocalDateTime create_date_time;

}