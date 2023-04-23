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
public class user_and_history_tb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uhid;
    private Integer hid;
    private Integer guid;

}