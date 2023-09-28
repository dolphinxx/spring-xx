package com.jagsii.springxx.mybatis;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EntityTableInfo {
    private String tableName;
    private String tableSchema;
    private String tableCatalog;
    private String id;
    private Map<String, String> columns;
}
