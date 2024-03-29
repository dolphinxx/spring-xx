package com.jagsii.springxx.mybatis;


import com.jagsii.springxx.mybatis.annotations.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    @Id
    private Long id;
    private String name;
    private String address;
    private LocalDate birth;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
