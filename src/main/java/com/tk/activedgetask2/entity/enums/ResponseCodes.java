package com.tk.activedgetask2.entity.enums;

import lombok.Getter;

@Getter
public enum ResponseCodes {

    Successful("00"),
    Bad_Request("AO1"),
    Not_Found("A04"),
    Duplicate_Request("A02");

    private final String code;

    ResponseCodes(String s) {
        code = s;
    }
}
