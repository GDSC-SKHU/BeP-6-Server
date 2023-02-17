package com.google.bep.dto;

import lombok.Data;

@Data
public class RequestAccountDTO {
    private String email;
    private String name;
    private String password;
}
