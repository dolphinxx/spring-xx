package com.jagsii.springxx.common.pagination;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Page<T> {
    private int totalCount;
    private int totalPages;
    private int page;
    private int size;
    private List<T> items;

    public Page(int page, int size, int totalCount) {
        this.totalCount = totalCount;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalCount / size);
    }

    public Page(PageRequest pageRequest, int totalCount) {
        this(pageRequest.getPage(), pageRequest.getSize(), totalCount);
    }
}
