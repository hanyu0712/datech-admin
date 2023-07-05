package com.datech.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DataTablesResultJsonVO<T> implements Serializable {
    private Long draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<T> data;
    private String error;
}