package com.jagsii.springxx.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    /**
     * 1 based
     */
    private int page = 1;
    private int size = 25;

    public int getOffset() {
        return (page - 1) * size;
    }
}
