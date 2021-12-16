package com.tk.activedgetask2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SecurityException extends RuntimeException{

    private String message;
}
