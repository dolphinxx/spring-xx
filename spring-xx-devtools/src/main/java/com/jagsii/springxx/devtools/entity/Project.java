package com.jagsii.springxx.devtools.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Project {
    private String id;
    private String name;
    private String path;
}
