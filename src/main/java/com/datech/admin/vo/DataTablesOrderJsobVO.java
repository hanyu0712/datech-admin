package com.datech.admin.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataTablesOrderJsobVO implements Serializable {
    private Long column;
    private String dir;
}