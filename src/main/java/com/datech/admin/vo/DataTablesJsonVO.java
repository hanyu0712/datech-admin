package com.datech.admin.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class DataTablesJsonVO implements Serializable{
    private String contract;
    private Long draw;
    private List<DataTablesColumnsJsonVO> columns;
    private List<DataTablesOrderJsobVO> order;
    private Long start;
    private Long length;
    private DataTablesOrderJsobVO search;
}