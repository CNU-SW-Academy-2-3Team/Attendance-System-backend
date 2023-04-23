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
public class code_tb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cid;
    private String attendance_code;
    private LocalDateTime accept_start_time;
    private LocalDateTime accept_end_time;
    private LocalDateTime create_date_time;

}