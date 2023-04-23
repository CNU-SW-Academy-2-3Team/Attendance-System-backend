package com.code.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
//Group Table과 Mapping 됨
public class group_and_user_tb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer guid;
    private Integer uid;
    private Integer gid;

}