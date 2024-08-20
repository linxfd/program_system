package com.program.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class PageResponse<T> {

    private List<T> data;

    private long total;
    
    // 自定义传回元素
    private Map<String, Object> map;
}
