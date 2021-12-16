package com.tk.activedgetask2.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Response<T> {
    private String responseCode;
    private String responseMessage;
    private List<Error> errors = new ArrayList<>();
    private List<T> modelList = new ArrayList<>();
    private long count;
}
