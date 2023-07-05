package com.datech.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataTablesColumnsJsonVO implements Serializable {
    private String data;
    private String name;
    private boolean searchable;
    private boolean orderable;
//    private DataTablesSearchJsnVO search;
}