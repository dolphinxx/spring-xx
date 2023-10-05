package com.jagsii.springxx.mybatis;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class EntityTableInfo {
    private String tableName;
    private String tableSchema;
    private String tableCatalog;
    private String id;
    private Map<String, String> columns;

    public String getColumnByField(String field) {
        for(Map.Entry<String, String> entry : columns.entrySet()) {
            if(Objects.equals(entry.getValue(), field)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
