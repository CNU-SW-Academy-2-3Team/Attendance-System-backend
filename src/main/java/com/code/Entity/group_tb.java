package com.code.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import jakarta.persistence.Id;
import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
//Group Table과 Mapping 됨
public class group_tb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gid;
    private String group_title;
    private String group_detail;
    private Integer leader_uid;
    private String invite_code;
    private Character availability;
    private Integer cid;
    private LocalDateTime create_date_time;


}