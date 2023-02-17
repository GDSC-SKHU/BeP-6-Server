package com.google.bep.dto;

import lombok.Data;

@Data
public class RequestLoginDTO {
    private String email;
    private String password;
}
