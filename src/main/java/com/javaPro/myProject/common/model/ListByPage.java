package com.javaPro.myProject.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListByPage {
    private int code;
    private String msg;
    private long total;
    private List<?> list;
}
